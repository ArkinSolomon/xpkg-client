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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import net.xpkgclient.versioning.InvalidVersionException;
import net.xpkgclient.versioning.Version;
import net.xpkgclient.versioning.VersionRange;
import net.xpkgclient.versioning.VersionSelect;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * The dependency tree for all packages.
 */
public final class DependencyTree {

    /**
     * The directed acyclic graph of the package tree.
     *
     * @returns The directed acyclic graph of the package tree. Do not modify.
     */
    @Getter
    private final DirectedAcyclicGraph<PackageNode, DependencyEdge> graph;

    private final File storeFile;

    // Top level installed packages
    private final Set<PackageNode> installed = new HashSet<>();

    // All incompatibilities currently kept track of, indexed by packageId, with the value of all the versions that it is incompatible with
    private final Map<String, List<Incompatibility>> incompatibilities;

    /**
     * Create a dependency tree at the file location. Load if it exists, or create if it doesn't.
     *
     * @param storeFile The file of the dependency tree.
     */
    @SneakyThrows(InvalidVersionException.class)
    public DependencyTree(File storeFile) {
        this.storeFile = storeFile;

        graph = new DirectedAcyclicGraph<>(DependencyEdge.class);
        incompatibilities = new HashMap<>();

        Random random = new Random();
        List<PackageNode> nodes = new ArrayList<>();
        for (int i = 1; i <= 50; ++i) {
            PackageNode newNode = new PackageNode(UUID.randomUUID().toString(), new Version(random.nextInt(0, 1000), random.nextInt(0, 1000), random.nextInt(1, 1000)));
            nodes.add(newNode);
            graph.addVertex(newNode);
            if (random.nextFloat() < .01 || i < 5) {
                installed.add(newNode);
            } else {
                try {
                    int randIndex1 = random.nextInt(nodes.size());
                    int randIndex2 = random.nextInt(nodes.size());
                    if (randIndex1 == randIndex2 )
                        continue;
                    PackageNode dependency = nodes.get(randIndex1);
                    PackageNode dependent = nodes.get(randIndex2);
                    DependencyEdge edge = new DependencyEdge(dependent, dependency, new VersionSelect("*"));
                    graph.addEdge(dependent, dependency, edge);
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }
        }
    }

    /**
     * Save the dependency tree to the file provided at instantiation.
     */
    public void write() {

    }

    /**
     * Say that a top-level package has been installed.
     *
     * @param packageId The id of the package that has been installed.
     * @param version   The version of the package that has been installed.
     */
    public void addTopLevelPackage(String packageId, Version version) {
        PackageNode newNode = new PackageNode(packageId, version);
        graph.addVertex(newNode);
        installed.add(newNode);
    }

    /**
     * Declare an incompatibility.
     *
     * @param incompatibleId    The id of the package that declares the incompatibility.
     * @param incompatibilityId The id of the package that was declared as an incompatibility.
     * @param versions          The versions of {@code incompatibilityId} that are incompatible, as declared by {@code incompatibleId}.
     */
    public void addIncompatibility(String incompatibleId, String incompatibilityId, VersionSelect versions) {
        Incompatibility incompatibility = new Incompatibility(incompatibleId, versions);
        List<Incompatibility> packageIncompatibilities = incompatibilities.getOrDefault(incompatibilityId, new ArrayList<>());
        packageIncompatibilities.add(incompatibility);
        incompatibilities.put(incompatibilityId, packageIncompatibilities);
    }

    /**
     * Declare a dependency.
     *
     * @param dependentId  The identifier of the dependent package.
     * @param dependencyId The identifier of the package that {@code dependent} depends on.
     * @param versions     The versions of {@code dependencyId} that {@code dependent} depends on.
     */
    public void addDependency(String dependentId, String dependencyId, VersionSelect versions) throws PackageNotInstalledException {
        Version dependentVersion = isInstalled(dependentId);
        Version dependencyVersion = isInstalled(dependencyId);

        if (dependentVersion == null)
            throw new PackageNotInstalledException(dependentId);
        else if (dependencyVersion == null)
            throw new PackageNotInstalledException(dependencyId);

        PackageNode dependent = getPackageNode(dependentId, dependentVersion);
        PackageNode dependency = getPackageNode(dependencyId, dependencyVersion);

        DependencyEdge edge = new DependencyEdge(dependent, dependency, versions);
        graph.addEdge(dependent, dependency, edge);
        installed.add(dependency);
    }

    /**
     * Check if we can install a package with the specified id at the specified version.
     *
     * @param packageId The id of the package to determine if we can install.
     * @param version   The version of the package.
     * @return True if we can install the package
     */
    @SneakyThrows(PackageNotInstalledException.class)
    public boolean canInstall(String packageId, Version version) {
        if (incompatibilities.containsKey(packageId)) {
            List<Incompatibility> packageIncompatibilities = incompatibilities.get(packageId);
            for (Incompatibility incompatibility : packageIncompatibilities) {
                boolean isCompatible = !incompatibility.versions().containsVersion(version);
                if (!isCompatible)
                    return false;
            }
        }

        Version installedVersion = isInstalled(packageId);
        if (installedVersion != null) {
            PackageNode node = getPackageNode(packageId, installedVersion);

            Set<DependencyEdge> dependencies = graph.incomingEdgesOf(node);

            List<VersionRange> ranges = dependencies.stream().flatMap(e -> Arrays.stream(e.selection().getRanges())).toList();
            ranges = VersionSelect.simplify(ranges);
            VersionSelect selection = new VersionSelect(ranges);

            return selection.containsVersion(version);
        }

        return true;
    }

    /**
     * Get the id of the package if it is installed, or null if it's not.
     *
     * @param packageId The id of the package to check if it's installed.
     * @return The version of the installed package, or null if there is none.
     */
    public Version isInstalled(String packageId) {

        // We could check this.installed before checking all vertexes, but then we might just have to check all the edges again, which is just inefficient
        Set<PackageNode> allPackages = graph.vertexSet();
        Optional<PackageNode> pkg = allPackages.stream().findFirst();

        return pkg.isEmpty() ? null : pkg.get().version();
    }

    /**
     * Get an installed package.
     *
     * @param packageId The id of the package to get.
     * @param version   The version of the package to get.
     * @throws PackageNotInstalledException If the package is not installed.
     */
    public PackageNode getPackageNode(String packageId, Version version) throws PackageNotInstalledException {
        Set<PackageNode> allPackages = graph.vertexSet();
        Optional<PackageNode> optPkg = allPackages.stream().findFirst();
        if (optPkg.isEmpty())
            throw new PackageNotInstalledException(packageId);
        PackageNode pkg = optPkg.get();

        if (!pkg.version().equals(version))
            throw new PackageNotInstalledException(packageId, version);

        return pkg;
    }

    /**
     * Print the dependency tree for all packages.
     */
    public void printGraph() {
        for (PackageNode pkg : installed) {
            System.out.printf("%s@%s%n", pkg.packageId(), pkg.version());
            printGraph(pkg, 1);
        }
    }

    /**
     * Print the dependency tree for a specific package.
     *
     * @param pkg         The package to print the dependency of.
     * @param indentCount The indent for the dependency tree.
     */
    private void printGraph(PackageNode pkg, int indentCount) {
        String indent = "\t│".repeat(indentCount - 1) + "\t├ ";

        Set<DependencyEdge> children = graph.outgoingEdgesOf(pkg);
        for (DependencyEdge edge : children) {
            System.out.printf(indent + "requires %s %s, installed: %s@%s%n", edge.dependency().packageId(), edge.selection(), edge.dependency().packageId(), edge.dependency().version());
            printGraph(edge.dependency(), indentCount + 1);
        }
    }

    /**
     * A single incompatibility for a package.
     *
     * @param incompatibleId The id of the package that is not compatible.
     * @param versions       The version selection of the incompatible package.
     */
    private record Incompatibility(String incompatibleId,
                                   VersionSelect versions) {
    }
}
