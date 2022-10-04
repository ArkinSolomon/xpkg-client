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

import net.xpkgclient.enums.PackageType;
import net.xpkgclient.enums.ScriptType;
import net.xpkgclient.exceptions.XPkgAlreadySetException;
import net.xpkgclient.exceptions.XPkgClosedExecutionContextException;
import net.xpkgclient.exceptions.XPkgImmutableVarException;
import net.xpkgclient.exceptions.XPkgInvalidCallException;
import net.xpkgclient.exceptions.XPkgNotSetException;
import net.xpkgclient.vars.XPkgBool;
import net.xpkgclient.vars.XPkgResource;
import net.xpkgclient.vars.XPkgString;
import net.xpkgclient.vars.XPkgVar;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

/**
 * This file stores variables and other environment data for execution of a script or the portion of a script.
 */
public class ExecutionContext {

    // All environment variable names
    private static final String[] envVarNames = {"$IS_MAC_OS", "$IS_WINDOWS", "$IS_LINUX", "$IS_OTHER_OS",
            "$XP_DIR", "$TMP", "$SPACE"};
    // Store all variables
    private final HashMap<String, XPkgVar> vars;
    private final XPkgResource tmp;
    // Head or file meta
    private PackageType packageType;
    private ScriptType scriptType;
    // If this context is active (true if not closed)
    private boolean isActive;
    // The currently executing line
    private int currentLine = 0;

    //TODO setup cache directories for file modification tracking and resource directories for resource storage

    /**
     * Create a default execution context.
     *
     * @throws IOException Thrown if there was an issue creating the temporary directory.
     */
    public ExecutionContext() throws IOException {
        isActive = true;
        vars = new HashMap<>();

        //TODO store UUID
        // Create a unique id for the temporary folder
        String uniqueID = UUID.randomUUID().toString();

        // The X-Plane folder and temporary folders
        XPkgString xp = new XPkgString(Configuration.getXpPath().toString());
        tmp = new XPkgResource(new File(xp.getValue(), "xpkg/tmp/" + uniqueID));

        // Create the temporary file
        Files.createDirectories(Path.of(tmp.getValue().toString()));

        // Set flags for operating system information
        boolean isMacOS = SystemUtils.IS_OS_MAC;
        boolean isWindows = SystemUtils.IS_OS_WINDOWS;
        boolean isLinux = SystemUtils.IS_OS_LINUX;
        boolean isOtherOS = !isMacOS && !isWindows && !isLinux;

        // Set environment variables
        try {
            setInternalVar("$IS_MAC_OS", new XPkgBool(isMacOS));
            setInternalVar("$IS_WINDOWS", new XPkgBool(isWindows));
            setInternalVar("$IS_LINUX", new XPkgBool(isLinux));
            setInternalVar("$IS_OTHER_OS", new XPkgBool(isOtherOS));
            setInternalVar("$XP_DIR", xp);
            setInternalVar("$TMP", tmp);
            setInternalVar("$SPACE", new XPkgString(" "));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Create a blank execution context for testing. Sets both script_type and package_type to {@code OTHER}.
     *
     * @return A new execution context.
     * @throws IOException Thrown if there was an issue creating the temporary directory.
     */
    public static ExecutionContext createBlankContext() throws IOException {
        ExecutionContext context = new ExecutionContext();
        try {
            context.setScriptType(ScriptType.OTHER);
            context.setPackageType(PackageType.OTHER);
        } catch (XPkgInvalidCallException e) {
            throw new IllegalStateException(e);
        }
        return context;
    }

    /**
     * Check if a variable name is an environment/default variable name.
     *
     * @param name The name of the variable to check (including the dollar sign at the beginning).
     * @return True if the variable name is an environment/default variable name.
     */
    private static boolean isEnvVarName(String name) {
        for (String n : envVarNames) {
            if (n.equals(name))
                return true;
        }
        return false;
    }

    /**
     * Increment the line counter by one.
     */
    public void incCounter() throws XPkgInvalidCallException {
        activityCheck();
        ++currentLine;
    }

    /**
     * Get the current line counter.
     *
     * @return The current line of program execution.
     */
    public int getLineCounter() throws XPkgInvalidCallException {
        activityCheck();
        return currentLine;
    }

    /**
     * Close the context and delete all temporary files.
     */
    public void close() {
        isActive = false;
        //noinspection ResultOfMethodCallIgnored
        tmp.getValue().delete();
    }

    /**
     * Get the package_type of the execution context read from the script head.
     *
     * @return The package_type of the execution context.
     * @throws XPkgInvalidCallException Thrown if the execution context is closed.
     */
    public PackageType getPackageType() throws XPkgInvalidCallException {
        activityCheck();
        if (packageType == null)
            throw new XPkgNotSetException("package_type");
        return packageType;
    }

    /**
     * Set the package_type of the script after reading it from the script head.
     *
     * @param type The package_type of the script.
     * @throws XPkgInvalidCallException Thrown if the execution context is closed, or if the package_type has already been set.
     */
    public void setPackageType(PackageType type) throws XPkgInvalidCallException {
        activityCheck();
        if (packageType != null)
            throw new XPkgAlreadySetException("package_type");
        packageType = type;
    }

    /**
     * Get the script_type of the execution context read from the script head.
     *
     * @return The script_type of the execution context.
     * @throws XPkgInvalidCallException Thrown if the execution context is closed or if the script_type has not been set.
     */
    public ScriptType getScriptType() throws XPkgInvalidCallException {
        activityCheck();
        if (scriptType == null)
            throw new XPkgNotSetException("script_type");
        return scriptType;
    }

    /**
     * Set the script_type of the script after reading it from the script head.
     *
     * @param type The script_type of the script.
     * @throws XPkgInvalidCallException Thrown if the execution context is closed, or if the script_type has already been set.
     */
    public void setScriptType(ScriptType type) throws XPkgInvalidCallException {
        activityCheck();
        if (scriptType != null)
            throw new XPkgAlreadySetException("script_type");
        scriptType = type;
    }

    /**
     * Get the directory for all temporary files created in this execution context.
     *
     * @return The directory for all temporary files created in this execution context.
     * @throws XPkgInvalidCallException Thrown if the execution context is closed.
     */
    public File getTmpDir() throws XPkgInvalidCallException {
        activityCheck();
        return tmp.getValue();
    }

    /**
     * Set a variable.
     *
     * @param name  The name of the variable to set.
     * @param value The instance of an {@link XPkgVar} to set the value of the variable to.
     * @throws XPkgInvalidCallException  Thrown if the execution context is closed.
     * @throws XPkgImmutableVarException Thrown if {@code name} is an immutable variable.
     */
    public void setVar(String name, XPkgVar value) throws XPkgInvalidCallException, XPkgImmutableVarException {
        activityCheck();
        if (isEnvVarName(name))
            throw new XPkgImmutableVarException("Error: attempted to set value of immutable environment variable");
        setInternalVar(name, value);
    }

    /**
     * Set a variable without the safety checks like in {@link #setVar(String, XPkgVar)}.
     *
     * @param name  The name of the variable to set.
     * @param value The instance of an {@link XPkgVar} to set the value of the variable to.
     */
    private void setInternalVar(@NotNull String name, XPkgVar value) {
        vars.put(name, value);
    }

    // Get a variable

    /**
     * Get a variable.
     *
     * @param name The name of the variable to get.
     * @return The instance of an {@link XPkgVar} which was set using {@link #setVar(String, XPkgVar)} or {@link #setInternalVar(String, XPkgVar)}. Returns null if the variable is not set.
     * @throws XPkgInvalidCallException Thrown if the execution context is closed.
     */
    public XPkgVar getVar(@NotNull String name) throws XPkgInvalidCallException {
        activityCheck();
        return vars.get(name);
    }

    /**
     * Check if a variable exists.
     *
     * @param name The name of the variable to check for existence.
     * @return True if the variable exists.
     * @throws XPkgInvalidCallException Thrown if the execution context is closed.
     */
    public boolean hasVar(@NotNull String name) throws XPkgInvalidCallException {
        activityCheck();
        return vars.containsKey(name);
    }

    /**
     * Print all execution context data to {@code System.out}.
     */
    public void printContext() {

        if (!isActive) {
            System.out.println("-----------");
            System.out.println("Execution context is not active");
        }

        try {
            System.out.println("--Meta-----");
            System.out.println(" - packageType: " + packageType);
            System.out.println(" - scriptType: " + scriptType);
            System.out.println("--Vars-----");
            for (String k : vars.keySet())
                System.out.println(" - " + k + ": " + getVar(k).toString());
            System.out.println("-----------");
        } catch (Exception e) {
            // You really should never reach this
        }
    }

    /**
     * Throw an exception if this execution context is closed.
     *
     * @throws XPkgClosedExecutionContextException Thrown if the execution context is closed.
     */
    private void activityCheck() throws XPkgClosedExecutionContextException {
        if (!isActive)
            throw new XPkgClosedExecutionContextException();
    }
}
