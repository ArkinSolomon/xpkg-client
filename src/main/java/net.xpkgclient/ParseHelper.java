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

package net.xpkgclient;

import net.xpkgclient.exceptions.XPkgExecutionException;
import net.xpkgclient.exceptions.XPkgInternalException;
import net.xpkgclient.exceptions.XPkgInvalidBoolStatement;
import net.xpkgclient.exceptions.XPkgInvalidCallException;
import net.xpkgclient.exceptions.XPkgParseException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.exceptions.XPkgUndefinedVarException;
import net.xpkgclient.vars.VarType;
import net.xpkgclient.vars.XPkgBool;
import net.xpkgclient.vars.XPkgString;
import net.xpkgclient.vars.XPkgVar;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

//This class provides static methods to help parse

/**
 * This class provide methods to easily help commands parse repeating scenarios (such as booleans and getting ...STRING's).
 */
public class ParseHelper {

    /**
     * Check if a variable name is valid.
     *
     * @param variable The variable name, including the dollar sign at the beginning, if the dollar sign is not present, it will be considered invalid.
     * @return True if the variable name is valid. Does not guarantee existence.
     */
    public static boolean isValidVarName(String variable) {
        if (variable == null || !variable.startsWith("$"))
            return false;
        String name = variable.substring(1);
        return name.length() > 0 && name.matches("^\\w+$") && !name.substring(0, 1).matches("\\d");
    }

    /**
     * Check if a string can be considered a path. Also check for bad characters, like the tilde, percent sign, or double dots.
     *
     * @param path The potential path that is being checked. Requires a leading slash. If a leading slash is not present the path will be considered invalid.
     * @return True if the path is valid.
     */
    public static boolean isValidPath(@NotNull String path) {

        // If a path is empty it's valid
        if (path.isEmpty() || path.equalsIgnoreCase("/"))
            return true;

        if (!(path.startsWith("/") && !path.contains("\\") && !stringContains(path, "~%$")
                && !path.matches(".+/\\.(\\.)?/?.+")))
            return false;

        String[] pathParts = path.split("/");
        for (String part : pathParts) {
            if (part.matches("^\s+") || path.matches("\s+$"))
                return false;
        }
        return true;
    }

    /**
     * Check if a resource id is valid.
     *
     * @return True if the resource id is valid.
     */
    public static boolean isValidResourceId(String resourceId) {
        return resourceId.matches("\\w{32}");
    }

    /**
     * Evaluate a list of arguments and determine, based on the current execution context and constants if the expression evaluates to True.
     *
     * @param args    The arguments to check.
     * @param context The execution context that this evaluation is running in.
     * @return True if the expression evaluates to true with regard to the provided execution context.
     * @throws XPkgInvalidBoolStatement  Exception thrown if there is an unexpected token in the expression.
     * @throws XPkgUndefinedVarException Exception thrown if a variable is undefined. Only thrown if the expression hasn't already been evaluated to true.
     * @throws XPkgInvalidCallException  Exception thrown if the execution context provided is closed.
     * @throws XPkgTypeMismatchException Exception thrown if the type of a provided variable is not a boolean. Thrown even if the expression has already been evaluated to true.
     */
    public static boolean isTrue(String[] args, ExecutionContext context) throws XPkgParseException,
            XPkgUndefinedVarException, XPkgExecutionException, XPkgTypeMismatchException {

        String joined = String.join(" ", args);

        // Make sure there's no two AND's or OR's together
        if (joined.contains("\\|\\s*\\|"))
            throw new XPkgInvalidBoolStatement('|');
        else if (joined.matches("&\\s*&"))
            throw new XPkgInvalidBoolStatement('&');

        // First separate all OR's from AND's
        String[] ands = joined.split("\\|");

        boolean statementCurrentlyTrue = false;

        // Loop through every and
        for (String andStatement : ands) {

            boolean isThisStatementTrue = true;

            // Evaluate each part of the and statement separately
            andStatement = andStatement.trim();
            String[] parts = andStatement.split("&");
            if (andStatement.startsWith("&") || andStatement.endsWith("&"))
                throw new XPkgInvalidBoolStatement();

            //            if (parts.length == 0)
            //                throw new XPkgInvalidBoolStatement();

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
                if (!isValidVarName(part)) {
                    if (part.isBlank())
                        throw new XPkgInvalidBoolStatement();
                    throw new XPkgInvalidBoolStatement(part);
                }

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

            //Don't return immediately, check for type mismatches
            if (isThisStatementTrue)
                statementCurrentlyTrue = true;
        }
        return statementCurrentlyTrue;
    }

    /**
     * Get remaining arguments as a string by determining if the first variable is a variable and returning that, or combining all values in args.
     *
     * @param args    The arguments to get a string from.
     * @param context The execution context that this evaluation is running in.
     * @return The string evaluation of the args.
     * @throws XPkgUndefinedVarException Thrown if there is only one argument which is a valid variable name, but if the variable does not exist.
     * @throws XPkgInternalException     Thrown with the message 'arglen' if there are too many arguments (only happens if the first argument is a variable and there is more than one argument). Can also be thrown with the message 'mismatch' if the first argument exists and is a valid variable, but is not of type STRING. If thrown with 'mismatch' the type of the variable received will be thrown as a {@code VarType} enumeration as the exception's data.
     * @throws XPkgInvalidCallException  Thrown if the execution context provided is closed.
     */
    public static String getStr(String @NotNull [] args, ExecutionContext context)
            throws XPkgUndefinedVarException, XPkgInternalException, XPkgInvalidCallException {
        String retStr;

        // Make sure the variable name is valid and exist
        if (isValidVarName(args[0])) {

            // Make sure there's no other argument
            if (args.length > 1)
                throw new XPkgInternalException("arglen");

            // Ensure the variable exists
            if (!context.hasVar(args[0]))
                throw new XPkgUndefinedVarException(args[0]);

            // Get the variable and check the type
            XPkgVar var = context.getVar(args[0]);
            VarType vType = var.getVarType();

            if (vType == VarType.STRING)
                retStr = ((XPkgString) var).getValue();
            else
                throw new XPkgInternalException("mismatch", vType);
        } else

            // If there is no variable just join all the arguments and return it
            retStr = String.join(" ", args);
        return retStr;
    }

    /**
     * Determine if a string matches any characters.
     *
     * @param testStr The string to test against.
     * @param matches The characters to check for in {@code testStr}.
     * @return True if any characters in {@code matches} are inside {@code testStr}.
     */
    public static boolean stringContains(@NotNull String testStr, @NotNull String matches) {
        String[] m = matches.split("");
        return Arrays.stream(m).anyMatch(testStr::contains);
    }
}
