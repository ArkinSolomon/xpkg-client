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

import java.io.File;
import java.util.Scanner;

//This file provides an interface for executing scripts
public class ScriptExecutionHandler {

    // Read and execute the whole file
    public static void executeFile(String path) {
        Scanner reader = null;
        try {
            File f = new File(path);
            reader = new Scanner(f);

            // Read the entire file into s
            StringBuilder s = new StringBuilder();
            while (reader.hasNext())
                s.append(reader.nextLine()).append("\n");

            // Execute the file
            ScriptExecutor file = new ScriptExecutor(s.toString());
            file.execute();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    // Execute some code (for testing)
    public static ExecutionContext executeText(String text) throws Throwable {
        return executeText(text, ExecutionContext.createBlankContext());
    }

    // Execute a some code (for testing) with a provided execution context
    public static ExecutionContext executeText(String text, ExecutionContext context) throws Throwable {

        // Create a context if there is none
        if (context == null)
            return executeText(text);

        ScriptExecutor script = new ScriptExecutor(text, context);
        script.execute();

        // Return context (without closing) no matter what
        return context;
    }
}
