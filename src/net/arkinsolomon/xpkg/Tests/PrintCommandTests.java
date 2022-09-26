package net.arkinsolomon.xpkg.Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.arkinsolomon.xpkg.Configuration;
import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.ScriptExecutionHandler;
import net.arkinsolomon.xpkg.Commands.Command;
import net.arkinsolomon.xpkg.Exceptions.XPkgArgLenException;

//Test the print command
class PrintCommandTests {

	// Set up configuration and commands
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Configuration.setInlinePrint(true);
		Configuration.setXpPath(new File("/Users/arkinsolomon/Desktop/X-Plane 12"));
		Command.registerCommands();
	}

	// Print just regular strings
	@Test
	void basic() throws Throwable {
		assertEquals("Hello", printText("Hello", null).trim());
		assertEquals("Hello world!", printText("Hello world!", null).trim());
		assertEquals("Howdy there ma'am", printText("Howdy there ma'am", null).trim());
	}

	// Print some variables
	@Test
	void variable() throws Throwable {

		// Setup test context
		ExecutionContext context = ExecutionContext.createBlankContext();
		ScriptExecutionHandler.executeText("setstr $testStr Oh look at that a string", context);
		ScriptExecutionHandler.executeText("set $test2 TRUE", context);
		ScriptExecutionHandler.executeText("set $test3 FALSE", context);

		assertEquals(" ", printText("$SPACE", context));
		assertEquals("Oh look at that a string", printText("$testStr", context));
		assertEquals("TRUE", printText("$test2", context));
		assertEquals("FALSE", printText("$test3", context));
		assertEquals(Configuration.getXpPath().toString(), printText("$XP_DIR", context));
	}

	// Test out argument exceptions
	@Test
	void argExceptions() throws Throwable {

//		//Setup test context
//		ExecutionContext context = ExecutionContext.createBlankContext();
//		
		// Test without any arguments
		Exception e1 = assertThrows(XPkgArgLenException.class, () -> ScriptExecutionHandler.executeText("print"));
	
	}

	// Get the string from executing a print test
	private static String printText(String testArg, ExecutionContext context) throws Throwable {

		// Create a temporary print stream to save the output
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);

		PrintStream old = System.out;
		System.setOut(ps);

		ScriptExecutionHandler.executeText("print " + testArg, context);

		// Reset system.out
		System.out.flush();
		System.setOut(old);
		return baos.toString();
	}
}
