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
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This operation renames a file or directory.
 */
public final class RenameOperation extends Operation {

    private final File originalFile;
    private final Path newFile;

    /**
     * Create a new operation to rename a file or directory at the location of the value of {@code originalFileVar} with the name of {@code newName}.
     * @param originalFileVar The variable that contains the original location of the file.
     * @param newName The new name of the file to rename.
     * @throws XPkgFileExistsException Exception thrown if the original file or directory is being renamed to a file or directory that exists.
     */
    public RenameOperation(@NotNull XPkgFile originalFileVar, String newName) throws XPkgFileExistsException {
        originalFile = originalFileVar.getValue();
        newFile = originalFile.toPath().resolveSibling(newName);

        if (Files.exists(newFile))
            throw new XPkgFileExistsException(newFile.toFile());
    }

    /**
     * Get the path to the new file which the original file was renamed to.
     *
     * @return The path to the renamed file.
     */
    public Path getNewFile() {
        return newFile;
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
