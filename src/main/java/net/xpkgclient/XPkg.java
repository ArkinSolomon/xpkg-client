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

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.xpkgclient.gui.MainGUI;

import java.io.File;

/**
 * Main class. XPkg-Client entry point.
 */
@UtilityClass
public final class XPkg {

    /**
     * Main method. XPkg-Client entry point.
     *
     * @param args Client arguments.
     */
    @SneakyThrows
    public static void main(String[] args) {
        Properties.init();
        Configuration.load();

        if (!Configuration.hasConfiguredXPInstallation()) {
            Configuration.setXpPath(new File("/Users/arkinsolomon/Desktop/X-Plane 12"));
            Configuration.save();
        }

        MainGUI.main(args);
    }
}
