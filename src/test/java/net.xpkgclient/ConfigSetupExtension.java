package net.xpkgclient;

import net.xpkgclient.commands.Command;
import net.xpkgclient.exceptions.XPkgExecutionException;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.File;

//See https://stackoverflow.com/questions/43282798/in-junit-5-how-to-run-code-before-all-tests
public class ConfigSetupExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static boolean started = false;

    @Override
    public void beforeAll(ExtensionContext context) throws XPkgExecutionException {
        if (!started) {
            started = true;
            Configuration.setInlinePrint(true);
            Configuration.setXpPath(new File("/Users/arkinsolomon/Desktop/X-Plane 12"));
            Command.registerCommands();
        }
    }

    @Override
    public void close() {

    }
}