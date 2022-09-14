package net.arkinsolomon.xpkg;

import net.arkinsolomon.xpkg.Exceptions.InvalidScriptException;
import net.arkinsolomon.xpkg.Exceptions.ScriptExecutionException;
import net.arkinsolomon.xpkg.Vars.VarType;
import net.arkinsolomon.xpkg.Vars.XPkgBool;
import net.arkinsolomon.xpkg.Vars.XPkgVar;

//This class provides static methods to help parse
public class ParseHelper {

	// Check if a variable is a valid variable name
	public static boolean isValidVarName(String variable) {
		if (!variable.startsWith("$"))
			return false;
		String name = variable.substring(1);
		return !name.contains("!@#$%^&*()-+={}[]\\|'\":;<>,./?") && name.length() > 0;
	}

	// Determine if a set of arguments evaluates to true
	public static boolean isTrue(String[] args, ExecutionContext context)
			throws ScriptExecutionException, InvalidScriptException {

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
					throw new ScriptExecutionException("Invalid variable in script: '"+ part + "' does not exist");
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
}
