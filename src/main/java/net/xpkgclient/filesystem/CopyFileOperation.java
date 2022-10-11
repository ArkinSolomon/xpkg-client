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

import net.xpkgclient.vars.XPkgFile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

/**
 * This operation copies a file from one location to another.
 */
public final class CopyFileOperation extends Operation {

    private final File file;
    private final File target;

    /**
     * Create a new operation to copy {@code file} from its current position to {@code directory}.
     *
     * @param fileVar   The variable of the file to be copied.
     * @param directory The directory for it to be copied to.
     */
    public CopyFileOperation(XPkgFile fileVar, File directory) {
        super();
        this.file = fileVar.getValue();
        String name = fileVar.getName();
        target = new File(directory, name);
    }

    /**
     * Copy the file from its source directory to its destination directory.
     *
     * @throws IOException Exception thrown if there was an issue copying the file.
     */
    @Override
    public void perform() throws IOException {
        if (performed)
            return;
        if (file.isDirectory())
            FileUtils.copyDirectory(file, target);
        else
            Files.copy(file.toPath(), target.toPath(), NOFOLLOW_LINKS);
        performed = true;
    }

    /**
     * Delete the copied file.
     *
     * @throws IOException Exception thrown if there was an issue copying the file.
     */
    @Override
    public void undo() throws IOException {
        if (!performed)
            return;

        if (target.isDirectory())
            FileUtils.deleteDirectory(target);
        else if (!target.delete())
            throw new IOException("Could not undo operation by deleting target file at " + target);
    }
}
