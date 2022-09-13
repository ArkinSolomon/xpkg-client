package net.arkinsolomon.xpkg;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import net.arkinsolomon.xpkg.Exceptions.InvalidScriptException;
import net.arkinsolomon.xpkg.Exceptions.ProgrammerError;
import net.arkinsolomon.xpkg.Exceptions.ScriptExecutionException;
import net.arkinsolomon.xpkg.Exceptions.ScriptParseException;

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
		} catch (FileNotFoundException | ScriptParseException | InvalidScriptException | ScriptExecutionException
				| ProgrammerError e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				reader.close();
		}
	}
}
