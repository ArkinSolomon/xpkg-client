package net.xpkgclient.commands;

import net.xpkgclient.Configuration;
import net.xpkgclient.ScriptExecutionHandler;
import net.xpkgclient.exceptions.XPkgArgLenException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContextCommandTests {

    // Set up configuration and commands
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        Configuration.setInlinePrint(true);
        Configuration.setXpPath(new File("/Users/arkinsolomon/Desktop/X-Plane 12"));
        Command.registerCommands();
    }

    //Test basic execution (make sure it doesn't throw an error)
    @Test
    void testNormal() {
        assertDoesNotThrow(() -> ScriptExecutionHandler.executeText("context"), "Context command threw exception that was not expected");
    }

    //Test that it throws an exception when there's argument
    @Test
    void testArgLen() {
        assertThrows(XPkgArgLenException.class, () -> ScriptExecutionHandler.executeText("context $an_argument"));
    }
}
