package net.xpkgclient.commands;

import net.xpkgclient.ExecutionContext;
import net.xpkgclient.ParseHelper;
import net.xpkgclient.exceptions.XPkgArgLenException;
import net.xpkgclient.exceptions.XPkgImmutableVarException;
import net.xpkgclient.exceptions.XPkgInvalidBoolStatement;
import net.xpkgclient.exceptions.XPkgInvalidCallException;
import net.xpkgclient.exceptions.XPkgInvalidVarNameException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.exceptions.XPkgUndefinedVarException;
import net.xpkgclient.vars.XPkgBool;
import net.xpkgclient.vars.XPkgVar;

import java.util.Arrays;

//This class sets a variable based on multiple different things
public class SetCommand extends Command {

    public static void execute(String[] args, ExecutionContext context) throws XPkgArgLenException,
            XPkgInvalidVarNameException, XPkgUndefinedVarException, XPkgInvalidCallException, XPkgImmutableVarException,
            XPkgInvalidBoolStatement, XPkgTypeMismatchException {

        // Make sure there are enough arguments
        if (args.length < 2)
            throw new XPkgArgLenException(CommandName.SET, 2, args.length);

        // Get the assigned variable
        String assignee = args[0];
        if (!ParseHelper.isValidVarName(assignee))
            throw new XPkgInvalidVarNameException(assignee);
        args = Arrays.copyOfRange(args, 1, args.length);

        // If it's a variable just copy it
        if (args.length == 1 && ParseHelper.isValidVarName(args[0])) {

            // Make sure the variable exists
            if (!context.hasVar(args[0]))
                throw new XPkgUndefinedVarException(args[0]);

            XPkgVar originalVar = context.getVar(args[0]);
            XPkgVar newVar = originalVar.copy();
            context.setVar(assignee, newVar);
            return;
        }

        // Set the variable to true if it evaluates to true
        context.setVar(assignee, new XPkgBool(ParseHelper.isTrue(args, context)));

    }
}