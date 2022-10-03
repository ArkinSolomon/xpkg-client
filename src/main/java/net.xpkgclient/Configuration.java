/*
 * Copyright (c) 2022. XPkg-Client Contributors.
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

import java.io.File;

/**
 * This class stores and loads all configuration and settings for the client.
 */
public class Configuration {

    private static File xpPath = null;

    // True if the PRINT command should print using print() instead of println(),
    // used for testing
    private static boolean inlinePrint = false;

    /**
     * Get the location of the currently configured X-Plane directory.
     *
     * @return The File object which refers to the configured X-Plane directory.
     */
    public static File getXpPath() {
        return xpPath;
    }

    /**
     * Set the location of the currently configured X-Plane directory.
     */
    public static void setXpPath(File xpPath) {
        Configuration.xpPath = xpPath;
    }

    /**
     * Get whether printing done by the print command are printed on a single line.
     *
     * @return True if printing done by the print command is printed on a single line.
     */
    public static boolean getInlinePrint() {
        return inlinePrint;
    }

    /**
     * Set whether printing done by the print command are printed on a single line.
     */
    public static void setInlinePrint(boolean inlinePrint) {
        Configuration.inlinePrint = inlinePrint;
    }
}
