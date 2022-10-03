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

import net.xpkgclient.commands.Command;
import net.xpkgclient.commands.CommandName;
import net.xpkgclient.enums.EnumParser;
import net.xpkgclient.enums.HeadKey;
import net.xpkgclient.exceptions.ILineException;
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.exceptions.XPkgExecutionException;
import net.xpkgclient.exceptions.XPkgFlowControlException;
import net.xpkgclient.exceptions.XPkgInvalidCallException;
import net.xpkgclient.exceptions.XPkgParseException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.exceptions.XPkgUndefinedVarException;
import net.xpkgclient.exceptions.XPkgUnimplementedException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

//This class parses and executes scripts

/**
 * This class handles and executes script setup and command execution delegation.
 */
public class ScriptExecutor {

    // Script execution context
    private final ExecutionContext context;
    private final String code;
    private String head;
    // True if we made the execution context
    private boolean didMakeContext = false;

    // When we last got code for a flow control statement, was it an ENDIF at the
    // end?
    private boolean wasLastEndIf = false;

    /**
     * Load an entire script file and prepare it for execution.
     *
     * @param contents The text that was in the script.
     * @throws IOException   Thrown if there was an issue creating a temporary folder within the ExecutionContext constructor.
     * @throws XPkgException Thrown if there was an issue parsing the script.
     */
    public ScriptExecutor(@NotNull String contents) throws IOException, XPkgException {
        // File contents
        String contents1 = contents.trim();
        context = new ExecutionContext();

        // Separate the head and body
        String[] parts = contents1.split("---");
        if (parts.length != 2)
            throw new XPkgParseException("Could not parse script, file contains more than one head separator");
        head = parts[0];
        code = parts[1];

        readMeta();
        didMakeContext = true;
    }

    /**
     * Load code but do not create a new execution context, instead use the one provided.
     *
     * @param code    The text to execute.
     * @param context The already existing execution context to execute the script in.
     */
    public ScriptExecutor(String code, @NotNull ExecutionContext context) {
        this.code = code;
        this.context = context;
    }

    /**
     * Read the file head.
     *
     * @throws XPkgException Thrown if there was an error parsing the head.
     */
    private void readMeta() throws XPkgException {
        try (Scanner scanner = new Scanner(head)) {

            // Read the head line by line
            while (scanner.hasNext()) {
                String line = scanner.nextLine().trim();
                context.incCounter();

                // Ignore comments and blank lines
                if (line.startsWith("#") || line.isBlank())
                    continue;

                // Try to split the line in two
                String[] lineParts = line.split(":");
                if (lineParts.length != 2)
                    throw new XPkgParseException("Too many ':'s in file head");

                String keyStr = lineParts[0].trim();
                String valStr = lineParts[1].trim();

                // Parse the key
                HeadKey key = EnumParser.getHeadKey(keyStr);

                // Set different meta depending on the head key
                switch (key) {
                    case PACKAGE_TYPE -> context.setPackageType(EnumParser.getPackageType(valStr));
                    case SCRIPT_TYPE -> context.setScriptType(EnumParser.getScriptType(valStr));
                    default -> throw new XPkgUnimplementedException(
                            "There is a key '" + key + "' hasn't been implemented in the parsing of the metadata");
                }
            }

            // Increase the counter to account for the '---'
            context.incCounter();
        } catch (Exception e) {
            if (e instanceof ILineException)
                throw ((ILineException<?>) e).setLine(context.getLineCounter());
            throw e;
        }
    }

    /**
     * Execute a file line by line.
     *
     * @throws Throwable Thrown if there was an issue executing any command, can be parse errors, or execution errors, or general Java errors.
     */
    @SuppressWarnings("rawtypes")
    public void execute() throws Throwable {

        try (Scanner scanner = new Scanner(code)) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine().trim();
                context.incCounter();

                // Ignore all lines with comments in them
                if (line.startsWith("#") || line.isBlank())
                    continue;

                // Get different parts of the command
                String[] lineParts = line.split("\\s+");
                CommandName cmd = EnumParser.getCommand(lineParts[0]);
                String[] args = Arrays.copyOfRange(lineParts, 1, lineParts.length);

                // Handle branching
                if (cmd == CommandName.IF) {

                    // If the top level if statement is true, get the code and execute it,
                    // otherwise jump to the next flow control statement that we can execute
                    if (ParseHelper.isTrue(args, context)) {

                        String executableCode = getFlowControlCode(scanner);
                        ScriptExecutor executor = new ScriptExecutor(executableCode, context);
                        executor.execute();

                        // Find the end of the flow control statement
                        if (!wasLastEndIf)
                            gotoNextFlowControl(scanner, true);

                    } else {
                        boolean hasExecutableBranch = gotoNextFlowControl(scanner);

                        // Execute the next executable branch
                        if (hasExecutableBranch) {
                            String executableCode = getFlowControlCode(scanner);
                            ScriptExecutor executor = new ScriptExecutor(executableCode, context);
                            executor.execute();

                            // Find the end of the flow control statement
                            if (!wasLastEndIf)
                                gotoNextFlowControl(scanner, true);
                        }

                        //If there is no executable branch keep going
                    }
                } else {

                    // Do different things based on different commands
                    Command.call(cmd, args, context);
                }

            }
        } catch (Throwable e) {
            if (e instanceof ILineException)
                throw ((ILineException) e).setLine(context.getLineCounter());
            throw e;
        } finally {
            if (didMakeContext)
                context.close();
        }
    }

    /**
     * Jump the current scanner position to the next executable flow control statements.
     *
     * @param scanner The {@code Scanner} that is reading the file contents line by line, and which will be incremented to the next flow control statement.
     * @return True if it reaches a flow control statement that is executable, or false if it reaches the end of the flow control statement.
     * @throws XPkgUndefinedVarException Thrown if a boolean expression has an undefined variable.
     * @throws XPkgInvalidCallException  Thrown if this instance's execution context has been closed.
     * @throws XPkgTypeMismatchException Thrown if a boolean expression has a type mismatch.
     * @throws XPkgParseException        Thrown if there was an issue parsing the code.
     */
    private boolean gotoNextFlowControl(Scanner scanner)
            throws XPkgUndefinedVarException, XPkgExecutionException, XPkgTypeMismatchException, XPkgParseException {
        return gotoNextFlowControl(scanner, false);
    }

    /**
     * Either jump to the next executable flow control statement, or jump to the end of the flow control statement.
     *
     * @param scanner   The {@code Scanner} that is reading the file contents line by line, and which will be incremented to the next flow control statement.
     * @param jumpToEnd True if we should just jump to the end of the flow control statement.
     * @return True if it reaches a flow control statement that is executable, or false if it reaches the end of the flow control statement.
     * @throws XPkgUndefinedVarException Thrown if a boolean expression has an undefined variable.
     * @throws XPkgInvalidCallException  Thrown if this instance's execution context has been closed.
     * @throws XPkgTypeMismatchException Thrown if a boolean expression has a type mismatch.
     * @throws XPkgParseException        Thrown if there was an issue parsing the code.
     */
    private boolean gotoNextFlowControl(@NotNull Scanner scanner, boolean jumpToEnd)
            throws XPkgParseException, XPkgUndefinedVarException, XPkgExecutionException, XPkgTypeMismatchException {

        // The amount of nested if statements we're in
        int branchDepth = 0;

        while (scanner.hasNext()) {
            String subLine = scanner.nextLine().trim();

            // Ignore all lines with comments in them
            if (subLine.startsWith("#") || subLine.isBlank())
                continue;

            // Get the command and determine if it's a flow control statement
            String[] subLineParts = subLine.split("\\s+");
            CommandName subCmd = EnumParser.getCommand(subLineParts[0]);
            String[] subArgs = Arrays.copyOfRange(subLineParts, 1, subLineParts.length);

            if (subCmd == CommandName.IF) {
                ++branchDepth;
            } else if ((subCmd == CommandName.ELSE || subCmd == CommandName.ELIF) && branchDepth == 0) {

                // Determine if we can execute the IF or ELIF
                if (!jumpToEnd && (subCmd == CommandName.ELSE || ParseHelper.isTrue(subArgs, context)))
                    return true;
            } else if (subCmd == CommandName.ENDIF) {
                if (branchDepth == 0)
                    return false;
                --branchDepth;
            }
        }
        // Might need to throw a XPkgFlowControlException here
        return false;
    }

    /**
     * Get the code for the current flow control branch that {@code scanner} is at.
     *
     * @param scanner The {@code Scanner} that is reading the file contents line by line, and which will be incremented to the next flow control statement.
     * @return The code for the current flow control branch.
     * @throws XPkgParseException Thrown if there was an issue parsing the code.
     */
    private @NotNull String getFlowControlCode(@NotNull Scanner scanner) throws XPkgParseException, XPkgInvalidCallException {
        wasLastEndIf = false;

        // The code to return
        StringBuilder branchCode = new StringBuilder();

        int branchDepth = 0;
        while (scanner.hasNext()) {

            String subLine = scanner.nextLine().trim();
            context.incCounter();

            // Ignore all lines with comments in them
            if (subLine.startsWith("#") || subLine.isBlank())
                continue;

            // Get the command and determine if it's a flow control statement
            String[] subLineParts = subLine.split("\\s+");
            CommandName subCmd = EnumParser.getCommand(subLineParts[0]);

            if (subCmd == CommandName.IF) {
                ++branchDepth;
                branchCode.append(subLine).append("\n");
            } else if ((subCmd == CommandName.ELSE || subCmd == CommandName.ELIF) && branchDepth == 0)

                // We have found the end of the IF branch, so return it
                return branchCode.toString();

            else if (subCmd == CommandName.ENDIF) {
                if (branchDepth == 0) {
                    wasLastEndIf = true;
                    return branchCode.toString();
                }
                --branchDepth;
                branchCode.append(subLine).append("\n");
            } else
                branchCode.append(subLine).append("\n");
        }
        throw new XPkgFlowControlException("If statement does not have ENDIF command");
    }
}
