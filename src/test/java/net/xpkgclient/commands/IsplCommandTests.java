package net.xpkgclient.commands;

import net.xpkgclient.ConfigSetupExtension;
import net.xpkgclient.ExecutionContext;
import net.xpkgclient.vars.VarType;
import net.xpkgclient.vars.XPkgBool;
import net.xpkgclient.vars.XPkgVar;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.fail;

// Test the ISPL command, since the majority of this command's fails are reliant on ParseHelper.isValidPath(), most of the tests will be handled there
@ExtendWith(ConfigSetupExtension.class)
public class IsplCommandTests {

    //Basic ISPL command checks, returns empty string if passed
    private static String testBasic(String[] args, boolean expected) throws Throwable {
        ExecutionContext context = ExecutionContext.createBlankContext();
        IsplCommand.execute(args, context);
        if (!context.hasVar("$testVar"))
            return "Context does not have variable";
        XPkgVar var = context.getVar("$testVar");
        context.close();
        if (var.getVarType() != VarType.BOOL)
            return "Context has variable, but it is not a boolean";
        if (((XPkgBool) var).getValue() != expected)
            return "Variable's value should be " + expected;

        return "";
    }

    @Test
    void testNormal() throws Throwable {
        String output = testBasic(new String[]{"$testVar", "/is/this/path/like"}, true);
        if (!output.isBlank())
            fail(output);
    }

    @Test
    void testFile() throws Throwable {
        String output = testBasic(new String[]{"$testVar", "/this/points/to/a/file.txt"}, true);
        if (!output.isBlank())
            fail(output);
    }

    @Test
    void testFail() throws Throwable {
        String output = testBasic(new String[]{"$testVar", "this/should/not/be/pathlike"}, false);
        if (!output.isBlank())
            fail(output);
    }

    @Test
    void testDoubleDot() throws Throwable {
        String output = testBasic(new String[]{"$testVar", "this/should/not/be/pathlike/../i/hope/"}, false);
        if (!output.isBlank())
            fail(output);
    }

    @Test
    void testSpace() throws Throwable {

        String output = testBasic(new String[]{"$testVar", "/this/should/be", "a/pathlike"}, true);
        if (!output.isBlank())
            fail(output);
    }
}
