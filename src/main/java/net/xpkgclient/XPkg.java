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

import lombok.experimental.UtilityClass;
import net.xpkgclient.commands.Command;
import net.xpkgclient.exceptions.XPkgExecutionException;

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
    public static void main(String[] args) throws XPkgExecutionException {

        Configuration.load();
        if (!Configuration.hasConfiguredXPInstallation()) {
            Configuration.setXpPath(new File("/Users/arkinsolomon/Desktop/X-Plane 12"));
            Configuration.save();
        }
        System.out.println("Using X-Plane installation: " + Configuration.getXpPath());


        Command.registerCommands();
        ScriptExecutionHandler.executeFile("/Users/arkinsolomon/Desktop/test.xpkgs");
    }
}