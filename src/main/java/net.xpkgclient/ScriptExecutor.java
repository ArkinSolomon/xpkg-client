package net.xpkgclient;

import net.xpkgclient.commands.Command;
import net.xpkgclient.commands.CommandName;
import net.xpkgclient.enums.EnumParser;
import net.xpkgclient.enums.HeadKey;
import net.xpkgclient.exceptions.ILineException;
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.exceptions.XPkgFlowControlException;
import net.xpkgclient.exceptions.XPkgInvalidCallException;
import net.xpkgclient.exceptions.XPkgParseException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.exceptions.XPkgUndefinedVarException;
import net.xpkgclient.exceptions.XPkgUnimplementedException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

//This class parses and executes scripts
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

    // Parse the entire file
    public ScriptExecutor(String contents) throws IOException, XPkgException {
        // File contents
        String contents1 = contents.trim();
        context = new ExecutionContext();

        // Separate the head and body
        String[] parts = contents1.split("---");
        if (parts.length != 2)
            throw new XPkgParseException("Could not parse script, file contains more than one head seperator");
        head = parts[0];
        code = parts[1];

        readMeta();
        didMakeContext = true;
    }

    // Execute a subscript by passing in context and code
    public ScriptExecutor(String code, ExecutionContext context) {
        this.code = code;
        this.context = context;
    }

    // Read file metadata
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

    // Execute the file line by line, throws if execution fails
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

    // Jump to the next flow control statement that's executable, returns true if it
    // reaches an executable statement, and false if it reaches the end of the flow
    // control
    private boolean gotoNextFlowControl(Scanner scanner)
            throws XPkgUndefinedVarException, XPkgInvalidCallException, XPkgTypeMismatchException, XPkgParseException {
        return gotoNextFlowControl(scanner, false);
    }

    private boolean gotoNextFlowControl(Scanner scanner, boolean jumpToEnd)
            throws XPkgParseException, XPkgUndefinedVarException, XPkgInvalidCallException, XPkgTypeMismatchException {

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
        return false;
    }

    // Get the code for the current flow control branch
    private String getFlowControlCode(Scanner scanner) throws XPkgParseException {
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
            } else if ((subCmd == CommandName.ELSE || subCmd == CommandName.ELIF) && branchDepth == 0) {

                // We have found the end of the IF branch, so return it
                return branchCode.toString();

            } else if (subCmd == CommandName.ENDIF) {
                if (branchDepth == 0) {
                    wasLastEndIf = true;
                    return branchCode.toString();
                }
                --branchDepth;
                branchCode.append(subLine).append("\n");
            } else {
                branchCode.append(subLine).append("\n");
            }
        }
        throw new XPkgFlowControlException("If statement does not have ENDIF command");
    }
}
