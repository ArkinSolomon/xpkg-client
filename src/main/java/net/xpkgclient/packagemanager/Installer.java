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
import net.xpkgclient.versioning.Version;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@UtilityClass
public class Installer {

    /**
     * Install a package at a specified version. Does not check for compatibility.
     *
     * @param pkg            The package to install.
     * @param packageVersion The version of the package to install.
     * @return A future which completes when the package is installed.
     */
    public CompletableFuture<Void> installPackage(Package pkg, Version packageVersion) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // We can't use supplyAsync here since downloading the package already takes a callback
        Remote.downloadPackage(pkg, packageVersion, (err, loc) -> {
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

            try {
                SakuraInterpreter interpreter = new SakuraInterpreter(options);
                interpreter.executeFile(new File(loc, "install.ska"));
                future.complete(null);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            } finally {
                // FileUtils.deleteDirectory(loc.getParentFile());
            }
        });

        return future;
    }
}
