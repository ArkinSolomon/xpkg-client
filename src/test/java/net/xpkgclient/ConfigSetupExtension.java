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
import net.xpkgclient.exceptions.XPkgExecutionException;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.File;

//See https://stackoverflow.com/questions/43282798/in-junit-5-how-to-run-code-before-all-tests
public class ConfigSetupExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static boolean started = false;

    @Override
    public void beforeAll(ExtensionContext context) throws XPkgExecutionException {
        if (!started) {
            started = true;
            Configuration.setInlinePrint(true);
            Configuration.setXpPath(new File("/Users/arkinsolomon/Desktop/X-Plane 12"));
            Command.registerCommands();
        }
    }

    @Override
    public void close() {

    }
}