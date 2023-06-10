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

import lombok.experimental.UtilityClass;
import net.arkinsolomon.sakurainterpreter.InterpreterOptions;
import net.arkinsolomon.sakurainterpreter.SakuraInterpreter;
import net.xpkgclient.Configuration;
import net.xpkgclient.packagemanager.actions.InstallerAction;
import net.xpkgclient.versioning.Version;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@UtilityClass
public class Installer {

    /**
     * Install a package at a specified version. Only use for fresh installations, do not use for updates.
     *
     * @param pkg            The package to install.
     * @param packageVersion The version of the package to install.
     * @return A future which completes when the package is installed.
     */
    public CompletableFuture<Void> installPackage(Package pkg, Version packageVersion) {
        DependencyTree dependencyTree = Configuration.getDependencyTree();
        CompletableFuture<Void> future = new CompletableFuture<>();

        Thread t = new Thread(() -> {
            Optional<List<InstallerAction>> actions = dependencyTree.getActions(pkg.getPackageId(), packageVersion);

            if (actions.isEmpty()) {
                future.completeExceptionally(new UnablePackageInstallException(pkg.getPackageId(), packageVersion));
                return;
            }

            for (InstallerAction action : actions.get()) {
                System.out.println(action);
                action.perform();
            }

            future.complete(null);
        });

        t.start();

        return future;
    }

    /**
     * Run the installation script for a specific package.
     *
     * @param pkg                The package to install.
     * @param version            The version of the package to install.
     * @param packageVersionData The data for a package version.
     * @return A future which completes when the installation script is done running.
     */
    private CompletableFuture<Void> runInstallScript(Package pkg, Version version, Remote.VersionData packageVersionData) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        Remote.downloadPackage(pkg, version, packageVersionData, (err, loc) -> {
            if (err != null)
                throw new RuntimeException(err);

            InterpreterOptions options = new InterpreterOptions("xpkg");

            File[] xpChildren = Configuration.getXpPath().listFiles();
            assert xpChildren != null;
            List<File> readableFiles = new ArrayList<>(Arrays.stream(xpChildren).filter(file -> file.isDirectory() && !file.getName().equals("xpkg")).toList());

            File[] resources = loc.listFiles();
            assert resources != null;
            readableFiles.addAll(Arrays.stream(resources).filter(File::isDirectory).toList());
            options.allowRead(readableFiles);

            File tmpFile = new File(Configuration.getTmpDir(), UUID.randomUUID().toString());
            options.allowRead(tmpFile);
            options.allowWrite(tmpFile);

            options.setRoot(Configuration.getXpPath());

            options.defineEnvVar("tmp", tmpFile);
            options.defineEnvVar("default", new File(loc, pkg.getPackageId()));

            options.defineEnvVar("packageName", pkg.getPackageName());
            options.defineEnvVar("packageId", pkg.getPackageId());

            switch (pkg.getPackageType()) {
                case AIRCRAFT -> {
                    File aircraftFile = new File(Configuration.getXpPath(), "Aircraft");
                    options.allowWrite(aircraftFile);
                    options.disallowWrite(new File(aircraftFile, "Laminar Research"));
                }
                case OTHER, EXECUTABLE -> {

                }
            }

            SakuraInterpreter interpreter = new SakuraInterpreter(options);
            interpreter.executeFile(new File(loc, "install.ska"));
            future.complete(null);
            // FileUtils.deleteDirectory(loc.getParentFile());
        });

        return future;
    }

    /**
     * Install the latest possible versions of a package.
     *
     * @param pkg The package to install.
     * @return A future which completes when the package and all of its dependencies have been installed.
     * @throws UnablePackageInstallException Exception thrown when the package or any of its dependencies can not be installed.
     */
    public CompletableFuture<Void> installLatestVersion(Package pkg) throws UnablePackageInstallException {
        return null;
    }
}
