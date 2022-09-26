package net.xpkgclient.commands;

import net.xpkgclient.Configuration;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;

public class GetCommandTests {

    // Set up configuration and commands
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        Configuration.setInlinePrint(true);
        Configuration.setXpPath(new File("/Users/arkinsolomon/Desktop/X-Plane 12"));
        Command.registerCommands();
    }


}
