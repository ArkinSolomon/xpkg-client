package net.xpkgclient.commands;

import net.xpkgclient.ConfigSetupExtension;
import net.xpkgclient.ExecutionContext;
import net.xpkgclient.ScriptExecutionHandler;
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
    private static String testBasic(String path, boolean expected) throws Throwable {
        ExecutionContext context = ExecutionContext.createBlankContext();
        ScriptExecutionHandler.executeText("ispl $testVar " + path, context);
        if (!context.hasVar("$testVar"))
            return "Context does not have variable";
        XPkgVar var = context.getVar("$testVar");
        if (var.getVarType() != VarType.BOOL)
            return "Context has variable, but it is not a boolean";
        if (((XPkgBool) var).getValue() != expected)
            return "Variable's value should be " + expected;

        return "";
    }

    //Test normal execution
    @Test
    void testNormal() throws Throwable {

        // Test normal path like checking
        String output = testBasic("/is/this/path/like", true);
        if (!output.isBlank())
            fail(output);
    }

    @Test
    void testFail() throws Throwable {

        // Test a failed path
        String output = testBasic("this/should/not/be/pathlike", false);
        if (!output.isBlank())
            fail(output);
    }

    @Test
    void testDoubleDot() throws Throwable {

        //Test a path with a double dot in it
        String output = testBasic("this/should/not/be/pathlike/../i/hope/", false);
        if (!output.isBlank())
            fail(output);
    }

    @Test
    void testSpace() throws Throwable {

        //Test a path with a space in it
        String output = testBasic("/this/should/be a/pathlike", true);
        if (!output.isBlank())
            fail(output);
    }
}
