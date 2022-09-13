package net.arkinsolomon.xpkg;

import java.util.Arrays;
import java.util.Scanner;

import net.arkinsolomon.xpkg.Commands.Command;
import net.arkinsolomon.xpkg.Commands.CommandName;
import net.arkinsolomon.xpkg.Enums.EnumParser;
import net.arkinsolomon.xpkg.Enums.HeadKey;
import net.arkinsolomon.xpkg.Enums.PackageType;
import net.arkinsolomon.xpkg.Enums.ScriptType;
import net.arkinsolomon.xpkg.Exceptions.InvalidScriptException;
import net.arkinsolomon.xpkg.Exceptions.ProgrammerError;
import net.arkinsolomon.xpkg.Exceptions.ScriptExecutionException;
import net.arkinsolomon.xpkg.Exceptions.ScriptParseException;

//This class parses and executes scripts
public class ScriptExecutor {

	// Script metadata
	ScriptType scriptType;
	PackageType packageType;

	// Script execution context
	ExecutionContext context;

	// File contents
	String contents;
	String head;
	String code;

	// Parse the entire file
	public ScriptExecutor(String contents) throws ScriptParseException, InvalidScriptException, ProgrammerError {
		this.contents = contents.trim();
		context = new ExecutionContext();

		// Separate the head and body
		String[] parts = this.contents.split("---");
		if (parts.length != 2)
			throw new ScriptParseException("Could not parse script, file contains more than one head seperator");
		head = parts[0];
		code = parts[1];

		readMeta();
	}

	// Read file metadata
	private void readMeta() throws ScriptParseException, InvalidScriptException, ProgrammerError {
		Scanner scanner = null;
		try {

			// Read the head line by line
			scanner = new Scanner(head);
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();

				// Ignore comments and blank lines
				if (line.startsWith("#") || line == "")
					continue;

				// Try to split the line in two
				String[] lineParts = line.split(":");
				if (lineParts.length != 2) {
					throw new InvalidScriptException(
							"Error reading script head: line has no or too many ':' in file head");
				}
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
					throw new InvalidScriptException("As a user, you shouldn't see this error... "
							+ "there was a key that hasn't been implemented in the parsing of the metadata");
				}
			}
		} finally {
			if (scanner != null)
				scanner.close();
		}
	}

	// Execute the file line by line, throws if execution fails
	public void execute() throws InvalidScriptException, ScriptExecutionException, ProgrammerError {
		Scanner scanner = null;
		try {
			scanner = new Scanner(code);
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();

				// Ignore all lines with comments in them
				if (line.startsWith("#") || line == "")
					continue;

				// Get different parts of the command
				String[] lineParts = line.split("\\s+");
				CommandName cmd = EnumParser.getCommand(lineParts[0]);
				String[] args = Arrays.copyOfRange(lineParts, 1, lineParts.length);

				// Do different things based on different commands
				Command.call(cmd, args, context);
			}
		} finally {
			context.printContext();
			if (scanner != null)
				scanner.close();
		}
	}

}
