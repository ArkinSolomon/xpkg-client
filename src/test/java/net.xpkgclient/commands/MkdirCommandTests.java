package net.xpkgclient.commands;

import net.xpkgclient.ConfigSetupExtension;
import net.xpkgclient.ExecutionContext;
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.vars.XPkgString;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

@ExtendWith(ConfigSetupExtension.class)
public class MkdirCommandTests {

    /**
     * Quickly create an execution context with default paths.
     *
     * @return A new execution context with three pathlike string variables ($path1 to $path3).
     * @throws IOException Thrown if there was an issue creating the temporary files while initializing the execution context.
     */
    private static @NotNull ExecutionContext newCtx() throws IOException {
        try {
            ExecutionContext context = ExecutionContext.createBlankContext();
            context.setVar("$path1", new XPkgString("/things/go/here"));
            context.setVar("$path2", new XPkgString("/oh/look bananas/"));
            context.setVar("$path3", new XPkgString("/file.txt"));
            return context;
        } catch (XPkgException e) {
            throw new IllegalStateException("Exception while setting up a test execution context", e);
        }
    }

    /**
     * Run code within a certain execution context, which is after {@code runner} runs.
     *
     * @param runner A lambda with an {@link ExecutionContext} as it's only parameter.
     * @throws IOException   Thrown when there is an issue creating temporary files in the execution context.
     * @throws XPkgException Thrown when there's an issue with the tested code.
     */
    private static void runInDefaultContext(@NotNull ContextRunner runner) throws XPkgException, IOException {
        ExecutionContext context = newCtx();
        runner.run(context);
        context.close();
    }

    //TODO write mkdir and mkdirs tests
}
