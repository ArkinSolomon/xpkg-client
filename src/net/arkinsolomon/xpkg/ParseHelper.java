package net.arkinsolomon.xpkg;

import java.util.Arrays;

import net.arkinsolomon.xpkg.Exceptions.XPkgInternalException;
import net.arkinsolomon.xpkg.Exceptions.XPkgInvalidBoolStatement;
import net.arkinsolomon.xpkg.Exceptions.XPkgInvalidCallException;
import net.arkinsolomon.xpkg.Exceptions.XPkgTypeMismatchException;
import net.arkinsolomon.xpkg.Exceptions.XPkgUndefinedVarException;
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

		// If a path is empty it's valid
		if (path.isEmpty())
			return true;

		if (!path.startsWith("/") || path.contains("\\") || stringContains(path, "~%")
				|| path.matches(".+/\\.\\./?.+")) {
			return false;
		}
		return true;
	}

	// Determine if a set of arguments evaluates to true
	public static boolean isTrue(String[] args, ExecutionContext context) throws XPkgInvalidBoolStatement,
			XPkgUndefinedVarException, XPkgInvalidCallException, XPkgTypeMismatchException{

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
					throw new XPkgInvalidBoolStatement(part);

				// Check if the variable exists
				if (!context.hasVar(part))
					throw new XPkgUndefinedVarException(part);

				// Check for type
				XPkgVar unknownTypeVar = context.getVar(part);
				VarType uTVType = unknownTypeVar.getVarType();
				if (uTVType != VarType.BOOL)
					throw new XPkgTypeMismatchException(part, VarType.BOOL, uTVType);

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
			throws XPkgUndefinedVarException, XPkgInternalException, XPkgInvalidCallException {
		String retStr;

		// Make sure the variable name is valid and exist
		if (isValidVarName(args[0])) {
			if (!context.hasVar(args[0]))
				throw new XPkgUndefinedVarException(args[0]);

			// Get the variable and check the type
			XPkgVar var = context.getVar(args[0]);
			VarType vType = var.getVarType();
			
			if (vType == VarType.STRING) {
				retStr = ((XPkgString) var).getValue();

				// Make sure there's no other argument
				if (args.length > 1)
					throw new XPkgInternalException("arg");
			} else
				throw new XPkgInternalException("mismatch", vType);
		} else

			// If there is no variable just join all the arguments and return it
			retStr = String.join(" ", args);
		return retStr;
	}

	// Determine if a string contains any of an item
	private static boolean stringContains(String testStr, String matches) {
		String[] m = matches.split("");
		return Arrays.stream(m).anyMatch(testStr::contains);
	}
}
