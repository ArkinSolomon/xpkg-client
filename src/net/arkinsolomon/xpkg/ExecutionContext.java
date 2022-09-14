package net.arkinsolomon.xpkg;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import org.apache.commons.lang3.SystemUtils;

import net.arkinsolomon.xpkg.Enums.PackageType;
import net.arkinsolomon.xpkg.Enums.ScriptType;
import net.arkinsolomon.xpkg.Exceptions.ProgrammerError;
import net.arkinsolomon.xpkg.Exceptions.ScriptExecutionException;
import net.arkinsolomon.xpkg.Vars.XPkgBool;
import net.arkinsolomon.xpkg.Vars.XPkgResource;
import net.arkinsolomon.xpkg.Vars.XPkgVar;

//This file stores variables and other data for execution
public class ExecutionContext {

	// Store all variables
	private HashMap<String, XPkgVar> vars;

	// Head or file meta
	private PackageType packageType;
	private ScriptType scriptType;

	// Machine information
	private boolean isMacOS = false;
	private boolean isWindows = false;
	private boolean isLinux = false;
	private boolean isOtherOS = false;

	// The X-Plane folder and temporary folders
	private XPkgResource xp = new XPkgResource(
			Path.of("Users", "arkinsolomon", "Desktop", "X-Plane 12").toAbsolutePath());
	private XPkgResource tmp = new XPkgResource(
			Path.of("Users", "arkinsolomon", "Desktop", "X-Plane 12", "TMP").toAbsolutePath());

	// All environment variable names
	private static String[] envVarNames = new String[] { "$IS_MAC_OS", "$IS_WINDOWS", "$IS_LINUX", "$IS_OTHER_OS", "$XP",
			"$TMP" };

	public ExecutionContext() {
		vars = new HashMap<String, XPkgVar>();

		// Set flags for machine
		isMacOS = SystemUtils.IS_OS_MAC;
		isWindows = SystemUtils.IS_OS_WINDOWS;
		isLinux = SystemUtils.IS_OS_LINUX;
		isOtherOS = !isMacOS && !isWindows && !isLinux;

		// Set environment variables
		setInternalVar("$IS_MAC_OS", new XPkgBool(isMacOS));
		setInternalVar("$IS_WINDOWS", new XPkgBool(isWindows));
		setInternalVar("$IS_LINUX", new XPkgBool(isLinux));
		setInternalVar("$IS_OTHER_OS", new XPkgBool(isOtherOS));
		setInternalVar("$XP", xp);
		setInternalVar("$TMP", tmp);
	}

	// PackageType getter and setter
	public void setPackageType(PackageType type) throws ProgrammerError {
		if (packageType != null)
			throw new ProgrammerError(
					"Whatever programmer made this accidentally set packageType in the execution context more than once");
		packageType = type;
	}

	public PackageType getPackageType() throws ProgrammerError {
		if (packageType == null)
			throw new ProgrammerError("packageType has not yet been set");
		return packageType;
	}

	// ScriptType getter and setter
	public void setScriptType(ScriptType type) throws ProgrammerError {
		if (scriptType != null)
			throw new ProgrammerError(
					"Whatever programmer made this accidentally set scriptType in the execution context more than once");
		scriptType = type;
	}

	public ScriptType getScriptType() throws ProgrammerError {
		if (scriptType == null)
			throw new ProgrammerError("scriptType has not yet been set");
		return scriptType;
	}

	// Set a variable
	public void setVar(String name, XPkgVar value) throws ScriptExecutionException {
		if (isEnvVarName(name))
			throw new ScriptExecutionException("Error: attempted to set value of immutable environment variable");
		setInternalVar(name, value);
	}

	// Set a variable without safety checks
	private void setInternalVar(String name, XPkgVar value) {
		vars.put(name, value);
	}

	// Get a variable
	public XPkgVar getVar(String name) {
		return vars.get(name);
	}

	// Check if a variable exists
	public boolean hasVar(String name) {
		return vars.containsKey(name);
	}

	// Print the execution context
	public void printContext() {
		System.out.println("--Meta-----");
		System.out.println(" - packageType: " + packageType);
		System.out.println(" - scriptType: " + scriptType);
		System.out.println("--Vars-----");
		for (String k : vars.keySet())
			System.out.println(" - " + k + ": " + getVar(k).toString());
		System.out.println("-----------");
	}

	// Check if a variable name is an environment variable name (returns true if it is)
	private static boolean isEnvVarName(String name) {
		for (String n : envVarNames) {
			if (n.equals(name))
				return true;
		}
		return false;
	}
}
