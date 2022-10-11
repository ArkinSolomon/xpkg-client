package net.xpkgclient.commands;

import net.xpkgclient.ConfigSetupExtension;
import net.xpkgclient.ExecutionContext;
import net.xpkgclient.exceptions.XPkgArgLenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(ConfigSetupExtension.class)
class ContextCommandTests {

    @Test
    void testNormal() throws IOException {
        ExecutionContext context = ExecutionContext.createBlankContext();
        assertDoesNotThrow(() -> ContextCommand.execute(new String[]{}, context), "Context command threw exception that was not expected while printing information about open execution context.");
        context.close();
    }

    @Test
    void testClosedContextPrint() throws IOException {
        ExecutionContext context = ExecutionContext.createBlankContext();
        context.close();
        assertDoesNotThrow(() -> ContextCommand.execute(new String[]{}, context), "Context command threw exception that was not expected while printing information about closed execution context");
    }

    @Test
    void testArgLen() throws IOException{
        ExecutionContext context = ExecutionContext.createBlankContext();
        assertThrows(XPkgArgLenException.class, () -> ContextCommand.execute(new String[]{"an_argument"}, context));
        context.close();
    }
}
