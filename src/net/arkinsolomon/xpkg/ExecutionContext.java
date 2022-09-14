package net.arkinsolomon.xpkg;

import java.util.HashMap;
import org.apache.commons.lang3.SystemUtils;

import net.arkinsolomon.xpkg.Enums.PackageType;
import net.arkinsolomon.xpkg.Enums.ScriptType;
import net.arkinsolomon.xpkg.Exceptions.ProgrammerError;
import net.arkinsolomon.xpkg.Vars.XPkgVar;

//This file stores variables and other data for execution
public class ExecutionContext {

	// Store all variables
	private HashMap<String, XPkgVar> vars;

	// Head or file meta
	PackageType packageType;
	ScriptType scriptType;

	// Machine information
	boolean isMacOS = false;
	boolean isWindows = false;
	boolean isLinux = false;
	boolean isOtherOS = false;

	public ExecutionContext() {
		vars = new HashMap<String, XPkgVar>();

		//Set flags for machine
		isMacOS = SystemUtils.IS_OS_MAC;
		isWindows = SystemUtils.IS_OS_WINDOWS;
		isLinux = SystemUtils.IS_OS_LINUX;
		isOtherOS = !isMacOS && !isWindows && !isLinux;
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
	public void setVar(String name, XPkgVar value) {
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
}
