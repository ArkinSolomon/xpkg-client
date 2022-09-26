package net.xpkgclient.commands;

import net.xpkgclient.ConfigSetupExtension;
import net.xpkgclient.ScriptExecutionHandler;
import net.xpkgclient.exceptions.XPkgArgLenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(ConfigSetupExtension.class)
class ContextCommandTests {

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
