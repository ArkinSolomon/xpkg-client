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
import net.xpkgclient.packagemanager.Installer;
import net.xpkgclient.packagemanager.PackageNode;
import net.xpkgclient.packagemanager.Remote;
import net.xpkgclient.versioning.Version;

import java.util.concurrent.ExecutionException;

/**
 * Create an action to install a brand-new package.
 *
 * @param packageId          The id of the package to install.
 * @param packageVersion     The version of the package to install.
 * @param manualInstallation True if the package was installed manually (top-level) or false if the package was installed by a user.
 */
public record InstallAction(String packageId,
                            Version packageVersion, boolean manualInstallation) implements InstallerAction {
    @SneakyThrows({InterruptedException.class, ExecutionException.class})
    @Override
    public void perform() {
        Installer.runInstallScript(Remote.getPackage(packageId), packageVersion, Remote.getVersionData(packageId, packageVersion)).get();

        DependencyTree tree = Configuration.getDependencyTree();
        PackageNode newNode = tree.addPackageNode(packageId, packageVersion);
        if (manualInstallation)
            tree.addUserInstalledPackage(newNode);
        else
            tree.addAutoInstalledPackage(newNode);
    }

    @Override
    public String toString() {
        return "Installs " + packageId + " version " + packageVersion + ".";
    }
}
