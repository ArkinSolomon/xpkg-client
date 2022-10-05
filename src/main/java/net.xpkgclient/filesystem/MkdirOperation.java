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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Create a directory (or directories).
 */
public class MkdirOperation extends Operation {

    private final File directory;
    private final boolean recursive;
    private Path lastNonExistentParent;

    /**
     * Create a new operation to create a directory at a specified place. Assume the directory intending to be created does not exist.
     *
     * @param directory The directory to create.
     * @param recursive True if parent directories should be created as well.
     */
    public MkdirOperation(File directory, boolean recursive) {
        super();
        this.directory = directory;
        this.recursive = recursive;
    }

    /**
     * Create the directories.
     *
     * @throws IOException Thrown if there was an issue creating the directories.
     */
    @Override
    public void perform() throws IOException {
        if (performed)
            return;
        performed = true;

        lastNonExistentParent = directory.toPath();
        if (recursive) {

            // Find the first directory that is created
            Path lastExistingParent = new File(directory.getParent()).toPath();
            while (!Files.exists(lastExistingParent)) {
                lastNonExistentParent = lastExistingParent;
                lastExistingParent = lastExistingParent.getParent();
            }

            Files.createDirectories(directory.toPath());
        } else
            Files.createDirectory(directory.toPath());
    }

    /**
     * Delete the created directories.
     *
     * @throws IOException Thrown if there was an issue deleting the directories.
     */
    @Override
    public void undo() throws IOException {
        if (!performed)
            return;

        FileUtils.deleteDirectory(lastNonExistentParent.toFile());
    }
}
