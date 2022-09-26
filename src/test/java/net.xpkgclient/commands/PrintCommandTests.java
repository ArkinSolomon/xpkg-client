package net.xpkgclient.commands;

import net.xpkgclient.ConfigSetupExtension;
import net.xpkgclient.Configuration;
import net.xpkgclient.ExecutionContext;
import net.xpkgclient.ScriptExecutionHandler;
import net.xpkgclient.exceptions.XPkgArgLenException;
import net.xpkgclient.exceptions.XPkgImmutableVarException;
import net.xpkgclient.exceptions.XPkgInvalidCallException;
import net.xpkgclient.exceptions.XPkgUndefinedVarException;
import net.xpkgclient.vars.XPkgString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//TODO Split the tests up into individual classes
//Test the print command
@ExtendWith(ConfigSetupExtension.class)
class PrintCommandTests {

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

    // Print just regular strings
    @Test
    void testBasicPrints() throws Throwable {
        assertEquals("Hello", printText("Hello", null).trim());
        assertEquals("Hello world!", printText("Hello world!", null).trim());
        assertEquals("Howdy there ma'am", printText("Howdy there ma'am", null).trim());
    }

    // Print some variables
    @Test
    void testVariablePrints() throws Throwable {

        // Setup test context
        //TODO setup context using ExecutionContext methods instead of through scripts to isolate testing to the print command
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

    // Test out exceptions
    @Test
    void exceptions() throws XPkgInvalidCallException, XPkgImmutableVarException, IOException {

        //Setup test context
        ExecutionContext context = ExecutionContext.createBlankContext();
        context.setVar("$testStr", new XPkgString("Test string"));

        // Test without any arguments
        assertThrows(XPkgArgLenException.class, () -> ScriptExecutionHandler.executeText("print"));
        assertThrows(XPkgArgLenException.class, () -> ScriptExecutionHandler.executeText("print   "));

        // Test with variable then text
        assertThrows(XPkgArgLenException.class, () -> ScriptExecutionHandler.executeText("print $testStr hello"));

        // Test with non-existent variable
        assertThrows(XPkgUndefinedVarException.class, () -> ScriptExecutionHandler.executeText("print $nonExistentVar"));
    }
}
