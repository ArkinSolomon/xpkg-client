package net.xpkgclient.commands;

import net.xpkgclient.ConfigSetupExtension;
import net.xpkgclient.ScriptExecutionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(ConfigSetupExtension.class)
public class GetCommandTests {

    //We haven't really done anything yet, so it just shouldn't throw an error
    @Test
    void testNormal() {
        assertDoesNotThrow(() -> ScriptExecutionHandler.executeText("get $var resource"), "GET command threw exception when it was not supposed to");
    }
}
