package net.xpkgclient.commands;

import net.xpkgclient.ExecutionContext;
import net.xpkgclient.ParseHelper;
import net.xpkgclient.exceptions.QuickHandles;
import net.xpkgclient.exceptions.XPkgArgLenException;
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.exceptions.XPkgInternalException;
import net.xpkgclient.exceptions.XPkgInvalidVarNameException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.vars.VarType;
import net.xpkgclient.vars.XPkgString;
import net.xpkgclient.vars.XPkgVar;

import java.util.Arrays;

//This class concats two strings together and stores it in the first variable
public class JoinCommand extends Command {

    public static void execute(String[] args, ExecutionContext context) throws XPkgException {

        // Check arguments
        if (args.length < 2)
            throw new XPkgArgLenException(CommandName.JOIN, 2, args.length);

        // Check if the variable is valid
        if (!ParseHelper.isValidVarName(args[0]))
            throw new XPkgInvalidVarNameException(args[0]);

        String assignee = args[0];
        if (!context.hasVar(args[0]))
            context.setVar(assignee, new XPkgString(""));
        else {
            XPkgVar assigneeVar = context.getVar(assignee);
            VarType assigneeType = assigneeVar.getVarType();
            if (assigneeType != VarType.STRING)
                throw new XPkgTypeMismatchException(CommandName.JOIN, "first", VarType.STRING, assigneeType);
        }

        // Check if the second argument is a variable
        args = Arrays.copyOfRange(args, 1, args.length);

        // The string to add to the variable assignee
        String catString;
        try {
            catString = ParseHelper.getStr(args, context);
        } catch (XPkgInternalException e) {
            throw QuickHandles.handleGetStr(CommandName.JOIN, e);
        }

        // Update the variable in memory
        XPkgString assigneeVar = (XPkgString) context.getVar(assignee);
        String assigneeCurrVal = assigneeVar.getValue();
        assigneeVar.setValue(assigneeCurrVal + catString);
    }
}
