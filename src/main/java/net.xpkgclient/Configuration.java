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

//All configuration and settings for the client

public class Configuration {

    // Get or set the currently active X-Plane path
    private static File xpPath = null;

    // True if the PRINT command should print using print() instead of println(),
    // used for testing
    private static boolean inlinePrint = false;

    public static File getXpPath() {
        return xpPath;
    }

    public static void setXpPath(File xpPath) {
        Configuration.xpPath = xpPath;
    }

    public static boolean getInlinePrint() {
        return inlinePrint;
    }

    public static void setInlinePrint(boolean inlinePrint) {
        Configuration.inlinePrint = inlinePrint;
    }
}
