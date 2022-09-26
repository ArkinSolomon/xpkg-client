package net.arkinsolomon.xpkg;

import java.io.File;
import java.util.Scanner;


//This file provides an interface for executing scripts
public class ScriptExecutionHandler {

	// Read and execute the whole file
	public static void executeFile(String path) {
		Scanner reader = null;
		try {
			File f = new File(path);
			reader = new Scanner(f);

			// Read the entire file into s
			String s = "";
			while (reader.hasNext())
				s += reader.nextLine() + "\n";

			// Execute the file
			ScriptExecutor file = new ScriptExecutor(s);
			file.execute();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	// Execute some code (for testing)
	public static ExecutionContext executeText(String text) throws Throwable {
		return executeText(text, ExecutionContext.createBlankContext());
	}

	// Execute a some code (for testing) with a provided execution context
	public static ExecutionContext executeText(String text, ExecutionContext context) throws Throwable {

		// Create a context if there is none
		if (context == null)
			return executeText(text);

		ScriptExecutor script = new ScriptExecutor(text, context);
		script.execute();

		// Return context (without closing) no matter what
		return context;
	}
}
