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

package net.xpkgclient.filesystem;

import net.xpkgclient.exceptions.XPkgFileExistsException;
import net.xpkgclient.vars.XPkgFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RenameOperation extends Operation {

    private final File originalFile;
    private final Path newFile;

    public RenameOperation(XPkgFile originalFileVar, String newName) throws XPkgFileExistsException {
        originalFile = originalFileVar.getValue();
        newFile = originalFile.toPath().resolveSibling(newName);

        if (Files.exists(newFile))
            throw new XPkgFileExistsException(newFile.toFile());

    }

    @Override
    public void perform() throws IOException {
        if (performed)
            return;
        Files.move(originalFile.toPath(), newFile);
        performed = true;
    }

    @Override
    public void undo() throws IOException {
        if (!performed)
            return;
        Files.move(newFile, originalFile.toPath());
    }
}
