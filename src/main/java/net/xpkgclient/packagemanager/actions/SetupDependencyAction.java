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

package net.xpkgclient.packagemanager.actions;

import net.xpkgclient.Configuration;
import net.xpkgclient.packagemanager.DependencyTree;
import net.xpkgclient.packagemanager.PackageNode;
import net.xpkgclient.packagemanager.PackageNotInstalledException;
import net.xpkgclient.versioning.VersionSelect;

/**
 * An action to create a new dependency edge between two packages.
 *
 * @param dependentId  The id of the dependent package.
 * @param dependencyId The id of the dependency.
 * @param selection    The selection that the version of the package with the id of {@code dependencyId} must satisfy.
 */
public record SetupDependencyAction(String dependentId, String dependencyId,
                                    VersionSelect selection) implements InstallerAction {

    @Override
    public void perform() {
        DependencyTree tree = Configuration.getDependencyTree();
        PackageNode dependencyNode;
        PackageNode dependentNode;

        try {
            dependentNode = getNodeOrThrow(tree, dependentId);
            dependencyNode = getNodeOrThrow(tree, dependencyId);
        } catch (PackageNotInstalledException e) {
            throw new RuntimeException("Can not create a link between a non-existent package or packages", e);
        }
        tree.addPackageDependency(dependentNode, dependencyNode, selection);
    }

    @Override
    public String toString() {
        return "Setup " + dependentId + " to be dependent on " + dependencyId + ".";
    }

    /**
     * Get a node from a tree or throw an exception if it does not exist.
     *
     * @param tree      The dependency tree to try to get the node from.
     * @param packageId The id of the package to get the node of.
     * @return The package node of the package with an id of {@code packageId}.
     * @throws PackageNotInstalledException Exception thrown if the package is not installed.
     */
    private PackageNode getNodeOrThrow(DependencyTree tree, String packageId) throws PackageNotInstalledException {
        PackageNode node = tree.getTreeNode(packageId);
        if (node == null)
            throw new PackageNotInstalledException(packageId);
        return node;
    }
}
