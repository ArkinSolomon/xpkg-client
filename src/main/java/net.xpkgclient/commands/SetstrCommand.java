package net.xpkgclient.commands;

import net.xpkgclient.ExecutionContext;
import net.xpkgclient.ParseHelper;
import net.xpkgclient.exceptions.XPkgArgLenException;
import net.xpkgclient.exceptions.XPkgImmutableVarException;
import net.xpkgclient.exceptions.XPkgInvalidCallException;
import net.xpkgclient.exceptions.XPkgInvalidVarNameException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.vars.XPkgString;

import java.util.Arrays;

//This command just sets the value of a variable to a string
public class SetstrCommand extends Command {
    public static void execute(String[] args, ExecutionContext context)
            throws XPkgArgLenException, XPkgInvalidVarNameException, XPkgInvalidCallException,
            XPkgImmutableVarException, XPkgTypeMismatchException {

        // Make sure there are arguments
        if (args.length < 2)
            throw new XPkgArgLenException(CommandName.SETSTR, 2, args.length);

        // Get the assigned variable
        String assignee = args[0];
        if (!ParseHelper.isValidVarName(assignee))
            throw new XPkgInvalidVarNameException(assignee);
        args = Arrays.copyOfRange(args, 1, args.length);

        if (args[0].startsWith("$"))
            throw new XPkgTypeMismatchException(CommandName.SETSTR, "second");

        // Make a new string
        XPkgString newStr = new XPkgString(String.join(" ", args));
        context.setVar(assignee, newStr);
    }
}