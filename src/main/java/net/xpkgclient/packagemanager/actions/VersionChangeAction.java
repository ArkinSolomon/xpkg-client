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

import lombok.SneakyThrows;
import net.xpkgclient.Configuration;
import net.xpkgclient.packagemanager.DependencyTree;
import net.xpkgclient.packagemanager.PackageNode;
import net.xpkgclient.packagemanager.PackageNotInstalledException;
import net.xpkgclient.versioning.Version;

/**
 * An action to change the version of a package.
 *
 * @param packageId         The id of the package to upgrade.
 * @param oldVersion        The old version of the package.
 * @param newVersion        The new version of the package to upgrade to.
 * @param manuallyInstalled True if the package is manually installed.
 */
public record VersionChangeAction(String packageId,
                                  Version oldVersion,
                                  Version newVersion, boolean manuallyInstalled) implements InstallerAction {
    @SneakyThrows(PackageNotInstalledException.class)
    @Override
    public void perform() {
        DependencyTree tree = Configuration.getDependencyTree();
        PackageNode modifiedNode = tree.changeVersion(packageId, newVersion);

        if (manuallyInstalled) {
            tree.tryRemoveAutoInstalledPackage(packageId);
            tree.addUserInstalledPackage(modifiedNode);
        } else {
            tree.tryRemoveUserInstalledPackage(packageId);
            tree.addAutoInstalledPackage(modifiedNode);
        }
    }

    @Override
    public String toString() {
        if (oldVersion.equals(newVersion))
            return "Sets " + packageId + " version " + oldVersion + " to " + (manuallyInstalled ? "user-installed" : "auto-installed") + '.';

        String upgradeOrDowngrade = oldVersion.compareTo(newVersion) < 0 ? "Upgrades" : "Downgrades";
        return upgradeOrDowngrade + " " + packageId + " from version " + oldVersion + " to version " + newVersion + ".";
    }
}
