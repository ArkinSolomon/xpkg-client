package net.xpkgclient.commands;

import net.xpkgclient.ConfigSetupExtension;
import net.xpkgclient.ExecutionContext;
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.vars.XPkgString;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

@ExtendWith(ConfigSetupExtension.class)
public class MkdirCommandTests {

    private ExecutionContext context;

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

    @BeforeEach
    void setupContext() throws IOException {
        context = newCtx();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }
}

