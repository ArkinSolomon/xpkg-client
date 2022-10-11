package net.xpkgclient.commands;

import net.xpkgclient.ConfigSetupExtension;
import net.xpkgclient.ExecutionContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(ConfigSetupExtension.class)
public class GetCommandTests {

    //We haven't really done anything yet, so it just shouldn't throw an error
    @Test
    void testNormal() throws IOException {
        ExecutionContext context = ExecutionContext.createBlankContext();
        assertDoesNotThrow(() -> GetCommand.execute(new String[]{"$var", "12345678901234567890123456789012"}, context), "GET command threw exception when it was not supposed to");
        context.close();
    }
}
