package net.xpkgclient;

import net.xpkgclient.enums.PackageType;
import net.xpkgclient.enums.ScriptType;
import net.xpkgclient.exceptions.XPkgAlreadySetException;
import net.xpkgclient.exceptions.XPkgClosedExecutionContextException;
import net.xpkgclient.exceptions.XPkgImmutableVarException;
import net.xpkgclient.exceptions.XPkgInvalidCallException;
import net.xpkgclient.exceptions.XPkgNotYetSetException;
import net.xpkgclient.vars.XPkgBool;
import net.xpkgclient.vars.XPkgResource;
import net.xpkgclient.vars.XPkgString;
import net.xpkgclient.vars.XPkgVar;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

//This file stores variables and other data for execution
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
    // If this context is active (note closed)
    private boolean isActive;
    // The currently executing line
    private int currentLine = 0;

    public ExecutionContext() throws IOException {
        isActive = true;
        vars = new HashMap<>();

        // Create a unique id for the temporary folder
        String uniqueID = UUID.randomUUID().toString();

        // The X-Plane folder and temporary folders
        XPkgString xp = new XPkgString(Configuration.getXpPath().toString());
        tmp = new XPkgResource(new File(xp.getValue(), "xpkg/temp/" + uniqueID));

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
            // Should be no reason this is reached
        }
    }

    // Create a blank execution context for testing
    public static ExecutionContext createBlankContext() throws XPkgInvalidCallException, IOException {
        ExecutionContext context = new ExecutionContext();
        context.setScriptType(ScriptType.OTHER);
        context.setPackageType(PackageType.OTHER);
        return context;
    }

    // Check if a variable name is an environment variable name (returns true if it
    // is)
    private static boolean isEnvVarName(String name) {
        for (String n : envVarNames) {
            if (n.equals(name))
                return true;
        }
        return false;
    }

    // Increment the line counter
    public void incCounter() {
        ++currentLine;
    }

    // Get the line counter
    public int getLineCounter() {
        return currentLine;
    }

    // Close and destroy the context
    public void close() {
        isActive = false;
        tmp.getValue().delete();
    }

    public PackageType getPackageType() throws XPkgInvalidCallException {
        if (!isActive)
            throw new XPkgClosedExecutionContextException();
        if (packageType == null)
            throw new XPkgNotYetSetException("package_type");
        return packageType;
    }

    // PackageType getter and setter
    public void setPackageType(PackageType type) throws XPkgInvalidCallException {
        if (!isActive)
            throw new XPkgClosedExecutionContextException();
        if (packageType != null)
            throw new XPkgInvalidCallException("Execution context already has set packageType");
        packageType = type;
    }

    public ScriptType getScriptType() throws XPkgInvalidCallException {
        if (!isActive)
            throw new XPkgClosedExecutionContextException();
        if (scriptType == null)
            throw new XPkgNotYetSetException("script_type");
        return scriptType;
    }

    // ScriptType getter and setter
    public void setScriptType(ScriptType type) throws XPkgInvalidCallException {
        if (!isActive)
            throw new XPkgClosedExecutionContextException();
        if (scriptType != null)
            throw new XPkgAlreadySetException("script_type");
        scriptType = type;
    }

    public File getTmpDir() {
        return tmp.getValue();
    }

    // Set a variable
    public void setVar(String name, XPkgVar value) throws XPkgInvalidCallException, XPkgImmutableVarException {
        if (!isActive)
            throw new XPkgClosedExecutionContextException();
        if (isEnvVarName(name))
            throw new XPkgImmutableVarException("Error: attempted to set value of immutable environment variable");
        setInternalVar(name, value);
    }

    // Set a variable without safety checks
    private void setInternalVar(String name, XPkgVar value) {
        vars.put(name, value);
    }

    // Get a variable
    public XPkgVar getVar(String name) throws XPkgInvalidCallException {
        if (!isActive)
            throw new XPkgClosedExecutionContextException();
        return vars.get(name);
    }

    // Check if a variable exists
    public boolean hasVar(String name) throws XPkgInvalidCallException {
        if (!isActive)
            throw new XPkgClosedExecutionContextException();
        return vars.containsKey(name);
    }

    // Print data about execution context
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
}
