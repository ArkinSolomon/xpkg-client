package net.arkinsolomon.xpkg;

//This class provides static methods to help parse
public class ParseHelper {

	// Check if a variable is a valid variable name
	public static boolean isValidVarName(String variable) {
		if (!variable.startsWith("$"))
			return false;
		String name = variable.substring(1);
		return !name.contains("!@#$%^&*()-+={}[]\\|'\":;<>,./?") && name.length() > 0;
	}
}
