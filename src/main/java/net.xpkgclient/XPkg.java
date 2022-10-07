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

import net.xpkgclient.commands.Command;

import java.io.File;

/**
 * Main class. XPkg-Client entry point.
 */
public class XPkg {

    /**
     * Main method. XPkg-Client entry point.
     *
     * @param args Client arguments.
     */
    public static void main(String[] args) {

        Configuration.setXpPath(new File("/Users/arkinsolomon/Desktop/X-Plane 12"));

        try {
            Command.registerCommands();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ScriptExecutionHandler.executeFile("/Users/arkinsolomon/Desktop/test.xpkgs");
    }
}
