package net.xpkgclient;

import net.xpkgclient.commands.Command;

import java.io.File;

public class XPkg {

    public static void main(String[] args) {

        //Configure
        Configuration.setXpPath(new File("/Users/arkinsolomon/Desktop/X-Plane 12"));

        // Register all commands
        try {
            Command.registerCommands();
        } catch (Exception e) {
            e.printStackTrace();
        }
//		try {
//				throw new XPkgTypeMismatchException("join", "second", VarType.STRING, VarType.RESOURCE);
//		} catch (XPkgRuntimeException e) {
//			throw new XPkgRuntimeException(7, e);
//		}

        // Execute a test script
        ScriptExecutionHandler.executeFile("/Users/arkinsolomon/Desktop/test.xpkgs");
    }
}
