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

package net.xpkgclient.packagemanager;

import net.xpkgclient.Configuration;
import net.xpkgclient.enums.PackageType;
import net.xpkgclient.enums.ScriptType;
import net.xpkgclient.exceptions.XPkgAlreadySetException;
import net.xpkgclient.exceptions.XPkgClosedExecutionContextException;
import net.xpkgclient.exceptions.XPkgImmutableVarException;
import net.xpkgclient.exceptions.XPkgInternalException;
import net.xpkgclient.exceptions.XPkgInvalidCallException;
import net.xpkgclient.exceptions.XPkgNotSetException;
import net.xpkgclient.filesystem.FileTracker;
import net.xpkgclient.vars.XPkgBool;
import net.xpkgclient.vars.XPkgMutableResource;
import net.xpkgclient.vars.XPkgResource;
import net.xpkgclient.vars.XPkgString;
import net.xpkgclient.vars.XPkgVar;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.UUID;

/**
 * This file stores variables and other environment data for execution of a script or the portion of a script.
 */
public class ExecutionContext {

    // All environment variable names
    private static final String[] envVarNames = {"$IS_MAC_OS", "$IS_WINDOWS", "$IS_LINUX", "$IS_OTHER_OS",
            "$XP", "$TMP", "$SPACE"};
    // Track all changes to the file system
    public final FileTracker fileTracker;
    // Store all variables
    private final HashMap<String, XPkgVar> vars;
    // Mutable resources exposed to the user
    private final XPkgMutableResource tmp;
    private final XPkgMutableResource xp;
    // The base file for all caches
    private final File baseFile;
    // Where we store downloaded resources
    private final File resourceDownloadLoc;
    // Where we store unzipped resources
    private final File resourceStorageLoc;
    // Where we cache overwritten files
    private final File overwrittenFilesLoc;

    // Head or file meta
    private PackageType packageType;
    private ScriptType scriptType;

    // True if this context is not closed
    private boolean isActive;

    private int currentLine = 0;

    /**
     * Create a default execution context.
     *
     * @throws IOException Thrown if there was an issue creating the temporary directory.
     */
    public ExecutionContext(File scriptLoc) throws IOException {
        isActive = true;
        vars = new HashMap<>();

        String contextId = UUID.randomUUID().toString();

        baseFile = new File(Configuration.getXpPath(), "xpkg/tmp/" + contextId);
        xp = new XPkgMutableResource(Configuration.getXpPath());
        tmp = new XPkgMutableResource(new File(baseFile, "tmp"));
        resourceDownloadLoc = new File(baseFile, "downloads");
        resourceStorageLoc = new File(baseFile, "resources");
        overwrittenFilesLoc = new File(baseFile, "cache");

        fileTracker = new FileTracker();

        Files.createDirectories(tmp.getValue().toPath());
        Files.createDirectories(resourceDownloadLoc.toPath());
        Files.createDirectories(resourceStorageLoc.toPath());
        Files.createDirectories(overwrittenFilesLoc.toPath());

        boolean isMacOS = SystemUtils.IS_OS_MAC;
        boolean isWindows = SystemUtils.IS_OS_WINDOWS;
        boolean isLinux = SystemUtils.IS_OS_LINUX;
        boolean isOtherOS = !isMacOS && !isWindows && !isLinux;

        try {
            setInternalVar("$IS_MAC_OS", new XPkgBool(isMacOS));
            setInternalVar("$IS_WINDOWS", new XPkgBool(isWindows));
            setInternalVar("$IS_LINUX", new XPkgBool(isLinux));
            setInternalVar("$IS_OTHER_OS", new XPkgBool(isOtherOS));
            setInternalVar("$XP", xp);
            setInternalVar("$TMP", tmp);
            setInternalVar("$SPACE", new XPkgString(" "));

            if (scriptLoc != null)
                setInternalVar("$default", new XPkgResource(scriptLoc));
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
        ExecutionContext context = new ExecutionContext(null);
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

    public File getResourceDownloadLoc() {
        return resourceDownloadLoc;
    }

    public File getResourceStorageLoc() {
        return resourceStorageLoc;
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
        try {
            FileUtils.deleteDirectory(baseFile);
        } catch (IOException e) {
            // Do nothing, it'll be cleared at next start
        }
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
     * Check if a variable exists, and get it if it does.
     *
     * @param varName The name of the variable to check for existence.
     * @return The variable, if it exists.
     * @throws XPkgInternalException    Thrown with the message "invalid" if {@code varName} is invalid, or it's thrown with the message "undef" if {@code varName} is valid, but this execution context does not have a variable with the name. Any time this exception is thrown its data will be of type {@link String} which will hold the name of the variable that was invalid.
     * @throws XPkgInvalidCallException Thrown if this execution context is closed.
     */
    public @NotNull XPkgVar checkExistingVar(String varName) throws XPkgInternalException, XPkgInvalidCallException {
        activityCheck();
        if (!ParseHelper.isValidVarName(varName))
            throw new XPkgInternalException("invalid", varName);
        if (!hasVar(varName))
            throw new XPkgInternalException("undef", varName);
        return getVar(varName);
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
                System.out.println(" - " + k + ": " + vars.get(k).toString());
            System.out.println("-----------");
        } catch (Exception e) {
            throw new IllegalStateException(e);
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
