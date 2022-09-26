package net.xpkgclient.commands;

import net.xpkgclient.ExecutionContext;
import net.xpkgclient.ParseHelper;
import net.xpkgclient.exceptions.QuickHandles;
import net.xpkgclient.exceptions.XPkgArgLenException;
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.exceptions.XPkgInternalException;
import net.xpkgclient.exceptions.XPkgInvalidVarNameException;
import net.xpkgclient.exceptions.XPkgNotPathLikeException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.vars.VarType;
import net.xpkgclient.vars.XPkgString;
import net.xpkgclient.vars.XPkgVar;

import java.nio.file.Path;
import java.nio.file.Paths;

//This command joins two paths
public class JoinpCommand extends Command {
    public static void execute(String[] args, ExecutionContext context) throws XPkgException {

        // Check arguments
        if (args.length < 2)
            throw new XPkgArgLenException(CommandName.JOINP, 2, args.length);

        // Check if the variable is valid
        if (!ParseHelper.isValidVarName(args[0]))
            throw new XPkgInvalidVarNameException(args[0]);
        String assignee = args[0];
        if (!context.hasVar(args[0]))
            context.setVar(assignee, new XPkgString(""));
        else {

            // Make sure the variable is a string and valid
            XPkgVar assigneeVar = context.getVar(assignee);
            VarType assigneeType = assigneeVar.getVarType();
            if (assigneeType != VarType.STRING)
                throw new XPkgTypeMismatchException(CommandName.JOINP, "first", VarType.STRING, assigneeType);

            String assigneePath = ((XPkgString) assigneeVar).getValue();
            if (!ParseHelper.isValidPath(assigneePath))
                throw new XPkgNotPathLikeException(CommandName.JOINP, assignee, assigneePath);
        }

        // Get the second part of the path and combine them
        String secondHalf;
        try {
            secondHalf = ParseHelper.getStr(args, context);
        } catch (XPkgInternalException e) {
            throw QuickHandles.handleGetStr(CommandName.JOINP, e);
        }

        XPkgString assigneeVar = (XPkgString) context.getVar(assignee);
        Path newPath = Paths.get(assigneeVar.getValue(), secondHalf);
        assigneeVar.setValue(newPath.toString());
    }
}
