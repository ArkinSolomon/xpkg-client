/*
 * Copyright (c) 2023. Arkin Solomon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied limitations under the License.
 */

package net.xpkgclient.packagemanager;

import lombok.Getter;
import lombok.SneakyThrows;
import net.xpkgclient.packagemanager.actions.InstallAction;
import net.xpkgclient.packagemanager.actions.InstallerAction;
import net.xpkgclient.packagemanager.actions.SetupDependencyAction;
import net.xpkgclient.packagemanager.actions.VersionChangeAction;
import net.xpkgclient.versioning.Version;
import net.xpkgclient.versioning.VersionSelect;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.io.File;
import java.util.*;

/**
 * The dependency tree for all packages.
 */
public final class DependencyTree implements Cloneable {

    /**
     * The directed acyclic graph of the package tree.
     *
     * @returns The directed acyclic graph of the package tree. Do not modify.
     */
    @Getter
    private DirectedAcyclicGraph<PackageNode, PackageDependency> graph;

    private File storeFile;

    // Top level installed packages
    private Set<PackageNode> userInstalledPackages = new HashSet<>();

    // Packages installed automatically as dependencies
    private Set<PackageNode> autoInstalledPackages = new HashSet<>();

    // A map of all the dependencies indexed by the dependency package id, with the PackageDependency objects as values
    private HashMap<String, List<PackageDependency>> dependencies = new HashMap<>();

    // A map of all the package nodes
    private HashMap<String, PackageNode> treeNodes = new HashMap<>();

    /**
     * Create a dependency tree at the file location. Load if it exists, or create if it doesn't.
     *
     * @param storeFile The file of the dependency tree.
     */
    public DependencyTree(File storeFile) {
        this.storeFile = storeFile;

        graph = new DirectedAcyclicGraph<>(PackageDependency.class);
    }

    /**
     * Add a new package node to the tree and to the node index.
     *
     * @param packageId The id of the package to add.
     * @param version   The version of the package to add.
     */
    public PackageNode addPackageNode(String packageId, Version version) {
        PackageNode newNode = new PackageNode(packageId, version);
        graph.addVertex(newNode);
        treeNodes.put(packageId, newNode);
        return newNode;
    }

    /**
     * Change the version of a {@link PackageNode} in the tree.
     *
     * @param packageId  The id of the package to change.
     * @param newVersion The new version of the package.
     * @throws PackageNotInstalledException An exception thrown if the package is not installed.
     */
    public void changeVersion(String packageId, Version newVersion) throws PackageNotInstalledException {
        PackageNode changeNode = treeNodes.get(packageId);
        if (changeNode == null)
            throw new PackageNotInstalledException(packageId);
        changeNode.setVersion(newVersion);
    }

    /**
     * Add a package node to the list of manually installed packages.
     *
     * @param node The node to add.
     */
    public void addUserInstalledPackage(PackageNode node) {
        userInstalledPackages.add(node);
    }

    /**
     * Add a package node to the list of automatically installed packages.
     *
     * @param node The node to add.
     */
    public void addAutoInstalledPackage(PackageNode node) {
        autoInstalledPackages.add(node);
    }

    /**
     * Get the {@link PackageNode} of a package in the dependency tree by its id.
     *
     * @param packageId The id of the package to get the node of.
     * @return A reference to the node, or null if the node does not exist.
     */
    public PackageNode getTreeNode(String packageId) {
        return treeNodes.get(packageId);
    }

    /**
     * Create a new package dependency between two package nodes.
     *
     * @param dependent           The node of the package that is dependent on {@code dependency}.
     * @param dependency          The node of the package that dependent is dependent on.
     * @param dependencySelection The version selection that the installed version of {@code dependency} must satisfy.
     * @return The edge created on the graph.
     */
    public PackageDependency addPackageDependency(PackageNode dependent, PackageNode dependency, VersionSelect dependencySelection) {
        PackageDependency packageDependency = new PackageDependency(dependent, dependency, dependencySelection);
        graph.addEdge(dependent, dependency, packageDependency);

        if (dependencies.containsKey(dependency.getPackageId())) {
            dependencies.get(dependency.getPackageId()).add(packageDependency);
        } else {
            List<PackageDependency> dependencyList = new LinkedList<>();
            dependencyList.add(packageDependency);
            dependencies.put(dependency.getPackageId(), dependencyList);
        }

        return packageDependency;
    }

    /**
     * Get the actions to be performed for the package to be installed.
     *
     * @param packageId      The id of the package to be installed.
     * @param packageVersion The version of the package to be installed.
     * @return An optional, which is present if the package can be installed at the specified version, otherwise it's empty.
     */
    public Optional<List<InstallerAction>> getActions(String packageId, Version packageVersion) {
        DependencyTree tempTree = clone();
        PackageNode newNode = tempTree.addPackageNode(packageId, packageVersion);
        tempTree.userInstalledPackages.add(newNode);

        Optional<List<InstallerAction>> actionsOpt = tempTree.getActions(packageId, packageVersion, new LinkedList<>(), true);
        if (actionsOpt.isEmpty())
            return Optional.empty();

        List<InstallerAction> actions = actionsOpt.get();
        int endIndex = actions.size() - 1;
        for (int i = 0; i <= endIndex; ++i) {
            InstallerAction action = actions.get(i);
            if (action instanceof SetupDependencyAction) {
                actions.remove(action);
                actions.add(action);
                --endIndex;
                --i;
            }
        }
        return Optional.of(actions);
    }

    /**
     * Internally attempt to resolve the dependency tree and get a list of actions to perform in order to install a package at a specified version.
     *
     * @param packageId      The id of the package to install.
     * @param packageVersion The version of the package to install.
     * @param actions        The list of actions to perform, passed on through recursive calls.
     * @return An optional, which is present if the package can be installed at the specified version, otherwise it's empty.
     */
    Optional<List<InstallerAction>> getActions(String packageId, Version packageVersion, List<InstallerAction> actions, boolean manualInstall) {
        Remote.VersionData data = Remote.getVersionData(packageId, packageVersion);

        // The unresolved incompatibilities of the package, indexed by package id, with a value of the version selection
        HashMap<String, VersionSelect> unresolvedIncompatibilities = new HashMap<>();
        for (String[] incompatibility : data.incompatibilities()) {
            unresolvedIncompatibilities.put(incompatibility[0], new VersionSelect(incompatibility[1]));
        }

        for (Map.Entry<String, VersionSelect> incompatibility : unresolvedIncompatibilities.entrySet()) {

            String incompatibilityId = incompatibility.getKey();
            VersionSelect incompatibilitySelection = incompatibility.getValue();

            // Check if the package is installed at the top level
            if (userInstalledPackages.stream().anyMatch(p -> p.getPackageId().equals(incompatibilityId) && incompatibilitySelection.containsVersion(p.getVersion()))) {
                // Unable to continue, incompatible
                return Optional.empty();
            }
            if (autoInstalledPackages.stream().anyMatch(p -> p.getPackageId().equals(incompatibilityId) && incompatibilitySelection.containsVersion(p.getVersion()))) {
                //noinspection OptionalGetWithoutIsPresent
                Version currentIncompatibilityVersion = getInstalledVersion(incompatibilityId).get();

                // Get the selection of packages that depend on the incompatibility
                List<VersionSelect> dependencySelections = new ArrayList<>(dependencies.get(incompatibilityId).stream().map(PackageDependency::selection).toList());
                VersionSelect dependencySelection = VersionSelect.intersection(dependencySelections.toArray(VersionSelect[]::new));

                Package incompatiblePkg = Remote.getPackage(incompatibilityId);
                List<Version> versions = new ArrayList<>(List.of(incompatiblePkg.getVersions()));
                Collections.reverse(versions);

                boolean incompatibilityResolved = false;
                for (Version version : versions) {
                    if (dependencySelection.containsVersion(version) && !incompatibilitySelection.containsVersion(version)) {
                        try {
                            changeVersion(incompatibilityId, version);

                            Optional<List<InstallerAction>> incompatibilityActions = getActions(incompatibilityId, version, actions, false);

                            if (incompatibilityActions.isEmpty()) {
                                incompatibilityResolved = true;

                                actions.set(actions.size() - 1, new VersionChangeAction(incompatibilityId, currentIncompatibilityVersion, version));
                                break;
                            }

                        } catch (PackageNotInstalledException e) {
                            throw new AssertionError("Incompatibility not installed (couldn't change version), but we're trying to resolve an installed incompatibility", e);
                        }

                    }
                }

                if (!incompatibilityResolved) {
                    return Optional.empty();
                }
            }
        }

        // The unresolved dependencies, indexed similarly to unresolvedIncompatibilities
        HashMap<String, VersionSelect> unresolvedDependencies = new HashMap<>();
        for (String[] dependency : data.dependencies()) {
            unresolvedDependencies.put(dependency[0], new VersionSelect(dependency[1]));
        }

        for (Map.Entry<String, VersionSelect> dependency : unresolvedDependencies.entrySet()) {
            String dependencyId = dependency.getKey();
            VersionSelect dependencySelection = dependency.getValue();

            // TODO: can't resolve if it's a user-installed package
            // If the dependency is already installed a version selection is the intersection of all the version selections AND the installed packages
            Optional<Version> currentlyInstalledVersion = getInstalledVersion(dependencyId);
            if (currentlyInstalledVersion.isPresent()) {
                List<VersionSelect> dependencySelections = new ArrayList<>(dependencies.get(dependencyId).stream().map(PackageDependency::selection).toList());
                dependencySelections.add(dependencySelection);
                dependencySelection = VersionSelect.intersection(dependencySelections.toArray(VersionSelect[]::new));

                // If the already installed dependency satisfies all the version selections, move on
                if (dependencySelection.containsVersion(currentlyInstalledVersion.get())) {
                    actions.add(new SetupDependencyAction(packageId, dependencyId, dependency.getValue()));
                    continue;
                }
            }

            Package dependencyPkg = Remote.getPackage(dependencyId);

            // If the package isn't present on the registry, we have a huge problem
            if (dependencyPkg == null)
                throw new RuntimeException("Dependency does not exist " + dependencyId);

            List<Version> availDepVersions = new java.util.ArrayList<>(Arrays.stream(dependencyPkg.getVersions()).toList());
            Collections.reverse(availDepVersions);

            boolean depInstalled = false;
            for (Version depTestVersion : availDepVersions) {
                if (dependencySelection.containsVersion(depTestVersion)) {

                    PackageNode dependentNode = treeNodes.get(packageId);
                    PackageNode dependencyNode = addPackageNode(dependencyId, depTestVersion);
                    PackageDependency dependencyEdge = addPackageDependency(dependentNode, dependencyNode, dependency.getValue());

                    addAutoInstalledPackage(dependencyNode);
                    Optional<List<InstallerAction>> dependencyActions = getActions(dependencyId, depTestVersion, actions, false);
                    actions.add(new SetupDependencyAction(packageId, dependencyId, dependency.getValue()));

                    if (dependencyActions.isPresent()) {
                        depInstalled = true;
                        break;
                    }

                    graph.removeEdge(dependencyEdge);
                    graph.removeVertex(dependencyNode);
                    autoInstalledPackages.remove(dependencyNode);
                }
            }

            if (!depInstalled) {
                return Optional.empty();
            }
        }

        actions.add(new InstallAction(packageId, packageVersion, manualInstall));
        return Optional.of(actions);
    }

    /**
     * Get the version of an installed package.
     *
     * @param packageId The id of the package to get.
     * @return An optional which is present with the installed version of the package if it is installed, or an empty optional otherwise.
     */
    private Optional<Version> getInstalledVersion(String packageId) {
        Optional<PackageNode> userInstalledPackage = userInstalledPackages.stream().filter(i -> i.getPackageId().equals(packageId)).findFirst();
        if (userInstalledPackage.isPresent())
            return Optional.of(userInstalledPackage.get().getVersion());

        Optional<PackageNode> autoInstalledPackage = autoInstalledPackages.stream().filter(i -> i.getPackageId().equals(packageId)).findFirst();
        return autoInstalledPackage.map(PackageNode::getVersion);
    }

    @Override
    @SneakyThrows(CloneNotSupportedException.class)
    protected DependencyTree clone() {
        DependencyTree clone = (DependencyTree) super.clone();

        //noinspection unchecked
        clone.graph = (DirectedAcyclicGraph<PackageNode, PackageDependency>) graph.clone();
        clone.storeFile = storeFile;
        clone.userInstalledPackages = new HashSet<>(userInstalledPackages);
        clone.autoInstalledPackages = new HashSet<>(autoInstalledPackages);

        HashMap<String, List<PackageDependency>> cloneDependencies = new HashMap<>();
        for (Map.Entry<String, List<PackageDependency>> entry : dependencies.entrySet())
            cloneDependencies.put(entry.getKey(), new LinkedList<>(entry.getValue()));

        clone.dependencies = cloneDependencies;

        clone.treeNodes = new HashMap<>();
        for (Map.Entry<String, PackageNode> nodeEntry : treeNodes.entrySet())
            clone.treeNodes.put(nodeEntry.getKey(), nodeEntry.getValue().clone());

        return clone;
    }
}
