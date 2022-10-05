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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * An instance of this class tracks all system changes.
 */
public class FileTracker {

    // All operations done by the file tracker
    ArrayList<Operation> operations = new ArrayList<>();

    /**
     * Run an operation, if it fails cat.
     *
     * @param operation The operation to add and run.
     * @throws IOException Exception thrown if there was an issue performing the operation.
     */
    public void runOperation(Operation operation) throws IOException {
        operations.add(operation);
        operation.perform();
    }

    /**
     * If an operation fails undo all operations.
     *
     * @throws IOException Exception thrown if there was an issue undoing the operation.
     */
    public void undoOperations() throws IOException {
        ArrayList<Operation> operationsCopy = new ArrayList<>(operations);
        Collections.reverse(operationsCopy);
        for (Operation operation : operationsCopy) {
            System.out.println("OPERATION UNDO " + operation);
            operation.undo();
        }
    }
}
