package net.arkinsolomon.xpkg;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import net.arkinsolomon.xpkg.Commands.Command;
import net.arkinsolomon.xpkg.Commands.CommandName;
import net.arkinsolomon.xpkg.Enums.EnumParser;
import net.arkinsolomon.xpkg.Enums.HeadKey;
import net.arkinsolomon.xpkg.Exceptions.ILineException;
import net.arkinsolomon.xpkg.Exceptions.XPkgException;
import net.arkinsolomon.xpkg.Exceptions.XPkgFlowControlException;
import net.arkinsolomon.xpkg.Exceptions.XPkgInvalidCallException;
import net.arkinsolomon.xpkg.Exceptions.XPkgParseException;
import net.arkinsolomon.xpkg.Exceptions.XPkgTypeMismatchException;
import net.arkinsolomon.xpkg.Exceptions.XPkgUndefinedVarException;
import net.arkinsolomon.xpkg.Exceptions.XPkgUnimplementedException;

//This class parses and executes scripts
public class ScriptExecutor {

	// Script execution context
	private ExecutionContext context;

	// File contents
	private String contents;
	private String head;
	private String code;

	// True if we made the execution context
	private boolean didMakeContext = false;

	// When we last got code for a flow control statement, was it an ENDIF at the
	// end?
	private boolean wasLastEndIf = false;

	// Parse the entire file
	public ScriptExecutor(String contents) throws IOException, XPkgException {
		this.contents = contents.trim();
		context = new ExecutionContext();

		// Separate the head and body
		String[] parts = this.contents.split("---");
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
	@SuppressWarnings("rawtypes")
	private void readMeta() throws XPkgException {
		Scanner scanner = null;
		try {

			// Read the head line by line
			scanner = new Scanner(head);
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				context.incCounter();

				// Ignore comments and blank lines
				if (line.startsWith("#") || line == "")
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
				case PACKAGE_TYPE:
					context.setPackageType(EnumParser.getPackageType(valStr));
					break;
				case SCRIPT_TYPE:
					context.setScriptType(EnumParser.getScriptType(valStr));
					break;
				default:
					throw new XPkgUnimplementedException(
							"There is a key '" + key + "' hasn't been implemented in the parsing of the metadata");
				}
			}

			// Increase the counter to account for the '---'
			context.incCounter();
		} catch (Exception e) {
			if (e instanceof ILineException)
				throw ((ILineException) e).setLine(context.getLineCounter());
			throw e;
		} finally {
			if (scanner != null)
				scanner.close();
		}
	}

	// Execute the file line by line, throws if execution fails
	@SuppressWarnings("rawtypes")
	public void execute() throws Throwable {

		Scanner scanner = null;
		try {
			scanner = new Scanner(code);
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				context.incCounter();

				// Ignore all lines with comments in them
				if (line.startsWith("#") || line == "")
					continue;

				// Get different parts of the command
				String[] lineParts = line.split("\\s+");
				CommandName cmd = EnumParser.getCommand(lineParts[0]);
				String[] args = Arrays.copyOfRange(lineParts, 1, lineParts.length);

				// Handle branching
				if (cmd == CommandName.IF) {

					// If the top level if statement is true, get the code and execute it it,
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
						} else

							// If there is no executable branch keep going
							continue;
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
			if (scanner != null)
				scanner.close();
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
			if (subLine.startsWith("#") || subLine == "")
				continue;

			// Get the command and determine if it's a flow control statement
			String[] subLineParts = subLine.split("\\s+");
			CommandName subCmd = EnumParser.getCommand(subLineParts[0]);
			String[] subArgs = Arrays.copyOfRange(subLineParts, 1, subLineParts.length);

			if (subCmd == CommandName.IF) {
				++branchDepth;
			} else if ((subCmd == CommandName.ELSE || subCmd == CommandName.ELIF) && branchDepth == 0) {

				// Determine if we can execute the IF or ELIF
				if (!jumpToEnd && (subCmd == CommandName.ELSE
						|| (subCmd == CommandName.ELIF && ParseHelper.isTrue(subArgs, context))))
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

		// The line we started at
		int startLine = context.getLineCounter();

		// The code to return
		String branchCode = "";

		int branchDepth = 0;
		while (scanner.hasNext()) {

			String subLine = scanner.nextLine().trim();
			context.incCounter();

			// Ignore all lines with comments in them
			if (subLine.startsWith("#") || subLine == "")
				continue;

			// Get the command and determine if it's a flow control statement
			String[] subLineParts = subLine.split("\\s+");
			CommandName subCmd = EnumParser.getCommand(subLineParts[0]);

			if (subCmd == CommandName.IF) {
				++branchDepth;
				branchCode += subLine + "\n";
			} else if ((subCmd == CommandName.ELSE || subCmd == CommandName.ELIF) && branchDepth == 0) {

				// Reset the line counter
				context.setCounter(startLine);

				// We have found the end of the IF branch, so return it it
				return branchCode;

			} else if (subCmd == CommandName.ENDIF) {
				if (branchDepth == 0) {

					// Reset the line counter
					context.setCounter(startLine);

					wasLastEndIf = true;
					return branchCode;
				}
				--branchDepth;
				branchCode += subLine + "\n";
			} else {
				branchCode += subLine + "\n";
			}
		}
		throw new XPkgFlowControlException("If statment does not have ENDIF command");
	}
}
