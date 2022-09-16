package net.arkinsolomon.xpkg;

import java.util.Arrays;

import net.arkinsolomon.xpkg.Exceptions.InvalidScriptException;
import net.arkinsolomon.xpkg.Exceptions.ProgrammerError;
import net.arkinsolomon.xpkg.Exceptions.ScriptExecutionException;
import net.arkinsolomon.xpkg.Vars.VarType;
import net.arkinsolomon.xpkg.Vars.XPkgBool;
import net.arkinsolomon.xpkg.Vars.XPkgString;
import net.arkinsolomon.xpkg.Vars.XPkgVar;

//This class provides static methods to help parse
public class ParseHelper {

	// Check if a variable is a valid variable name
	public static boolean isValidVarName(String variable) {
		if (!variable.startsWith("$"))
			return false;
		String name = variable.substring(1);
		return !stringContains(name, "!@#$%^&*()-+={}[]\\|'\":;<>,./?") && name.length() > 0;
	}

	// Determine if a string is a valid path
	public static boolean isValidPath(String path) {

		//If a path is empty it's valid
		if (path.isEmpty())
			return true;
		
		if (!path.startsWith("/") || path.contains("\\") || stringContains(path, "~%")
				|| path.matches(".+/\\.\\./?.+")) {
			return false;
		}
		return true;
	}

	// Determine if a set of arguments evaluates to true
	public static boolean isTrue(String[] args, ExecutionContext context)
			throws ScriptExecutionException, InvalidScriptException, ProgrammerError {

		// First separate all OR's from AND's
		String[] ands = String.join(" ", args).split("\\|");

		// Loop through every and
		for (String andStatement : ands) {

			boolean isThisStatementTrue = true;

			// Evaluate each part of the and statement separately
			andStatement = andStatement.trim();
			String[] parts = andStatement.split("&");

			for (String part : parts) {
				part = part.trim();

				// Check for inversion
				boolean isInverted = part.startsWith("!");
				if (isInverted)
					part = part.substring(1);

				// Check for true and false
				if ((part.equalsIgnoreCase("FALSE") && !isInverted) || (part.equalsIgnoreCase("TRUE") && isInverted)) {
					isThisStatementTrue = false;
					break;
				} else if (part.equalsIgnoreCase("TRUE"))
					continue;

				// Check if it's a variable
				if (!isValidVarName(part))
					throw new InvalidScriptException("Invalid item in boolean statement");

				// Check if the variable exists
				if (!context.hasVar(part)) {
					throw new ScriptExecutionException("Invalid variable in script: '" + part + "' does not exist");
				}

				// Check for type
				XPkgVar unknownTypeVar = context.getVar(part);
				if (unknownTypeVar.getVarType() != VarType.BOOL)
					throw new ScriptExecutionException(
							"Type mismatch: '" + part + "' is a " + unknownTypeVar.getVarType() + " expected BOOL");

				XPkgBool var = (XPkgBool) unknownTypeVar;
				isThisStatementTrue = var.getValue();
			}

			if (isThisStatementTrue)
				return true;
		}
		return false;
	}

	// Get a string or the value of a string variable
	public static String getStr(String[] args, ExecutionContext context)
			throws InvalidScriptException, ProgrammerError {
		String retStr;

		// Make sure the variable name is valid and exist
		if (isValidVarName(args[0])) {
			if (!context.hasVar(args[0]))
				throw new InvalidScriptException("Tried to pass in non-existent variable: '" + args[0] + "'");

			// Get the variable and check the type
			XPkgVar var = context.getVar(args[0]);

			if (var.getVarType() == VarType.STRING) {
				retStr = ((XPkgString) var).getValue();

				// Make sure there's no other argument
				if (args.length > 1)
					throw new InvalidScriptException("Passed in variable but also more than one argument");
			} else
				throw new InvalidScriptException(
						"Tried to pass in non-string variable instead of a string: '" + args[0] + "'");
		} else

			// If there is no variable just join all the argumentss and return it
			retStr = String.join(" ", args);
		return retStr;
	}

	// Determine if a string contains any of an item
	private static boolean stringContains(String testStr, String matches) {
		String[] m = matches.split("");
		return Arrays.stream(m).anyMatch(testStr::contains);
	}
}
