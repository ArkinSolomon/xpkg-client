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

package net.xpkgclient.commands;

import net.xpkgclient.ConfigSetupExtension;
import net.xpkgclient.packagemanager.ExecutionContext;
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.vars.XPkgString;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

@ExtendWith(ConfigSetupExtension.class)
public class MkdirCommandTests {

    private ExecutionContext context;

    /**
     * Quickly create an execution context with default paths.
     *
     * @return A new execution context with three pathlike string variables ($path1 to $path3).
     * @throws IOException Thrown if there was an issue creating the temporary files while initializing the execution context.
     */
    private static @NotNull ExecutionContext newCtx() throws IOException {
        try {
            ExecutionContext context = ExecutionContext.createBlankContext();
            context.setVar("$path1", new XPkgString("/things/go/here"));
            context.setVar("$path2", new XPkgString("/oh/look bananas/"));
            context.setVar("$path3", new XPkgString("/file.txt"));
            return context;
        } catch (XPkgException e) {
            throw new IllegalStateException("Exception while setting up a test execution context", e);
        }
    }

    @BeforeEach
    void setupContext() throws IOException {
        context = newCtx();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }
}

