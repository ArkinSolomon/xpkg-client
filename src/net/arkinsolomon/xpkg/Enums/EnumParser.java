package net.arkinsolomon.xpkg.Enums;

import net.arkinsolomon.xpkg.Commands.CommandName;
import net.arkinsolomon.xpkg.Exceptions.InvalidScriptException;

//This class contains static methods for parsing different enumerations
public class EnumParser {
	
	// Get a HeadKey enumeration from a string
	public static HeadKey getHeadKey(String headKey) throws InvalidScriptException {
		switch (headKey.toUpperCase()) {
		case "SCRIPT_TYPE":
			return HeadKey.SCRIPT_TYPE;
		case "PACKAGE_TYPE":
			return HeadKey.PACKAGE_TYPE;
		default:
			throw new InvalidScriptException("Invalid key in head: '" + headKey + "'");
		}
	}

	// Get a ScriptType enumeration from a string
	public static ScriptType getScriptType(String scriptType) throws InvalidScriptException {
		switch (scriptType.toUpperCase()) {
		case "OTHER":
			return ScriptType.OTHER;
		case "INSTALL":
			return ScriptType.INSTALL;
		case "UNINSTALL":
			return ScriptType.UNINSTALL;
		case "UPGRADE":
			return ScriptType.UPGRADE;
		default:
			throw new InvalidScriptException("Invalid script_type: '" + scriptType + "' is not a valid script type");
		}
	}

	// Get a PackageType enumeration from a string
	public static PackageType getPackageType(String packageType) throws InvalidScriptException {
		switch (packageType.toUpperCase()) {
		case "OTHER":
			return PackageType.OTHER;
		case "SCENERY":
			return PackageType.SCENERY;
		case "AIRCRAFT":
			return PackageType.AIRCRAFT;
		case "PLUGIN":
			return PackageType.PLUGIN;
		case "LIVERY":
			return PackageType.LIVERY;
		default:
			throw new InvalidScriptException("Invalid package_type: '" + packageType + "' is not a valid script type");
		}
	}
	
	// Get a Command enumeration from a string
	public static CommandName getCommand(String command) throws InvalidScriptException {
		switch (command.toUpperCase()) {
		case "QUICK":
			return CommandName.QUICK;
		case "GET":
			return CommandName.GET;
		case "PRINT":
			return CommandName.PRINT;
		case "IF":
			return CommandName.IF;
		case "ELIF":
			return CommandName.ELIF;
		case "ELSE":
			return CommandName.ELSE;
		case "ENDIF":
			return CommandName.ENDIF;
		case "SET":
			return CommandName.SET;
		case "SETSTR":
			return CommandName.SETSTR;
		case "MKDIR":
			return CommandName.MKDIR;
		case "MKDIRS":
			return CommandName.MKDIRS;
		case "ISPL":
			return CommandName.ISPL;
		default:
			throw new InvalidScriptException("Invalid command: '" + command + "' is not a valid command");
		}
	}
}
