package net.arkinsolomon.xpkg;

//This class contains static methods for parsing different enums
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
}
