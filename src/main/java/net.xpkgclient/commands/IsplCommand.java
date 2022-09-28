package net.xpkgclient.commands;

import net.xpkgclient.ExecutionContext;
import net.xpkgclient.ParseHelper;
import net.xpkgclient.exceptions.QuickHandles;
import net.xpkgclient.exceptions.XPkgArgLenException;
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.exceptions.XPkgInternalException;
import net.xpkgclient.exceptions.XPkgInvalidVarNameException;
import net.xpkgclient.vars.XPkgBool;

import java.util.Arrays;

//This command determines if a path or variable is pathlike, and stores the boolean result in a variable
public class IsplCommand extends Command {
    public static void execute(String[] args, ExecutionContext context)
            throws
            XPkgException {

        // Argument checking
        if (args.length < 2)
            throw new XPkgArgLenException(CommandName.ISPL, 2, args.length);

        // Variable to assign the result to
        String assignee = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        // Make sure the assignee is valid
        if (!ParseHelper.isValidVarName(assignee))
            throw new XPkgInvalidVarNameException(assignee);

        // The string that might be a path
        String testStr;
        try {
            testStr = ParseHelper.getStr(args, context);
        } catch (XPkgInternalException e) {
            throw QuickHandles.handleGetStr(CommandName.ISPL, e);
        }

        // Check the path name and set the value
        boolean isValid = ParseHelper.isValidPath(testStr);
        context.setVar(assignee, new XPkgBool(isValid));
    }
}
