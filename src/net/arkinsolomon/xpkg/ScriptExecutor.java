package net.arkinsolomon.xpkg;

import java.util.Scanner;

//This class parses and executes scripts
public class ScriptExecutor {

	// Script metadata
	ScriptType scriptType;
	PackageType packageType;

	// File contents
	String contents;
	String head;
	String code;

	// Parse the entire file
	public ScriptExecutor(String contents) throws ScriptParseException, InvalidScriptException {
		this.contents = contents.trim();

		// Separate the head and body
		String[] parts = this.contents.split("---");
		if (parts.length != 2)
			throw new ScriptParseException("Could not parse script, file contains more than one head seperator");
		head = parts[0];
		code = parts[1];

		readMeta();
	}

	// Read file metadata
	private void readMeta() throws ScriptParseException, InvalidScriptException {
		Scanner scanner = null;
		try {
			
			//Read the head line by line
			scanner = new Scanner(head);
			while (scanner.hasNext()) {
				String line = scanner.nextLine();

				// Ignore comments
				if (line.startsWith("#"))
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
					packageType = EnumParser.getPackageType(valStr);
					break;
				case SCRIPT_TYPE:
					scriptType = EnumParser.getScriptType(valStr);
					break;
				default:
					throw new InvalidScriptException("As a user, you shouldn't see this error, "
							+ "there was a key that hasn't been implemented in the parsing of the metadata");
				}

			}
		} catch (InvalidScriptException e) {
			throw e;
		} finally {
			if (scanner != null)
				scanner.close();
		}
	}

	// Execute the file line by line, throws if execution fails
	public void execute() {
		Scanner scanner = null;

		try {

			// Remove all lines with comments in them
			String newCode = "";
			scanner = new Scanner(code);
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (!line.startsWith("#"))
					newCode += line;
			}
			scanner.close();

			// Go through the code line by line
			scanner = new Scanner(newCode);

		} finally {
			if (scanner != null)
				scanner.close();
		}
	}

}
