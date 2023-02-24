/*
 * Copyright (c) 2022-2023. Arkin Solomon.
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

package net.xpkgclient;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.xpkgclient.packagemanager.DependencyTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.prefs.Preferences;

/**
 * This class stores and loads all configuration and settings for the client.
 */
@UtilityClass
public final class Configuration {

    private static final String NO_XP_INSTALLATION = "NO_XP_INSTALLATION";

    private static final Preferences prefs = Preferences.userRoot().node("xpkg-client-root");

    /**
     * The currently active X-Plane installation.
     *
     * @param xpPath Set the location of the currently configured X-Plane directory.
     * @return The File object which refers to the configured X-Plane directory.
     */
    @Getter
    @Setter
    private File xpPath = null;

    /**
     * The path to the temporary directory.
     *
     * @return The file which points to the root temporary directory.
     */
    @Getter
    private File tmpDir;

    /**
     * The dependency tree for the current configuration.
     *
     * @returns The dependency tree for this configuration.
     */
    @Getter
    private DependencyTree dependencyTree;

    /**
     * Save the configuration.
     */
    public static void save() {
        prefs.put("xp-config", xpPath.getAbsolutePath());
    }

    /**
     * Load the configuration.
     */
    @SneakyThrows(IOException.class)
    public static void load() {
        String xpPathStr = prefs.get("xp-config", NO_XP_INSTALLATION);
        xpPath = new File(xpPathStr);

        tmpDir = Files.createTempDirectory("xpkg-temp-").toFile();
        tmpDir.deleteOnExit();

        if (xpPathStr.equals(NO_XP_INSTALLATION))
            return;

        File xpkgPath = new File(xpPath, "xpkg");
        dependencyTree = new DependencyTree(new File(xpkgPath, "dependencies.xpkg.json"));
    }

    /**
     * Returns true if the user has configured an X-Plane installation directory.
     */
    public static boolean hasConfiguredXPInstallation() {
        return !xpPath.toString().equals("NO_XP_INSTALLATION");
    }
}
