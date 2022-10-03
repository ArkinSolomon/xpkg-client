package net.xpkgclient.commands;

import net.xpkgclient.ConfigSetupExtension;
import net.xpkgclient.ExecutionContext;
import net.xpkgclient.exceptions.XPkgArgLenException;
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.exceptions.XPkgImmutableVarException;
import net.xpkgclient.exceptions.XPkgUndefinedVarException;
import net.xpkgclient.vars.XPkgString;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(ConfigSetupExtension.class)
public class JoinCommandTests {

    /**
     * Quickly create an execution context with default strings.
     *
     * @return A new execution context with three string variables ($str1 to $str3).
     * @throws IOException Thrown if there was an issue creating the temporary files while initializing the execution context.
     */
    private static @NotNull ExecutionContext newCtx() throws IOException {
        try {
            ExecutionContext context = ExecutionContext.createBlankContext();
            context.setVar("$str1", new XPkgString("String 1"));
            context.setVar("$str2", new XPkgString("String 2"));
            context.setVar("$str3", new XPkgString("String 3"));
            return context;
        } catch (XPkgException e) {
            throw new IllegalStateException("Exception while setting up a test execution context", e);
        }
    }

    @Test
    void joinTwoStrings() throws XPkgException, IOException {
        ExecutionContext context = newCtx();
        JoinCommand.execute(new String[]{"$str1", "$str2"}, context);
        assertEquals("String 1String 2", context.getVar("$str1").toString());
        context.close();
    }

    @Test
    void joinStringAndSpace() throws XPkgException, IOException {
        ExecutionContext context = newCtx();
        JoinCommand.execute(new String[]{"$str1", "$SPACE"}, context);
        assertEquals("String 1 ", context.getVar("$str1").toString());
        context.close();
    }

    @Test
    void joinStringToSpace() throws IOException {
        ExecutionContext context = newCtx();
        assertThrows(XPkgImmutableVarException.class, () -> JoinCommand.execute(new String[]{"$SPACE", "$str2"}, context));
        context.close();
    }

    @Test
    void joinStringLiteralToString() throws XPkgException, IOException {
        ExecutionContext context = newCtx();
        JoinCommand.execute(new String[]{"$str3", "wow", "look", "a", "string"}, context);
        assertEquals("String 3wow look a string", context.getVar("$str3").toString());
        context.close();
    }

    @Test
    void joinSameStringVariable() throws XPkgException, IOException {
        ExecutionContext context = newCtx();
        JoinCommand.execute(new String[]{"$str1", "$str1"}, context);
        assertEquals("String 1String 1", context.getVar("$str1").toString());
        context.close();
    }

    @Test
    void joinStringLiteralTwoVariables() throws IOException {
        ExecutionContext context = newCtx();
        assertThrows(XPkgArgLenException.class, () -> JoinCommand.execute(new String[]{"$str1", "$str2", "$str3"}, context));
        context.close();
    }

    @Test
    void joinFirstVarUndefined() throws IOException, XPkgException {
        ExecutionContext context = newCtx();
        JoinCommand.execute(new String[]{"$undef_var", "$str1"}, context);
        assertEquals("String 1", context.getVar("$undef_var").toString());
        context.close();
    }

    @Test
    void joinSecondVarUndefined() throws IOException {
        ExecutionContext context = newCtx();
        assertThrows(XPkgUndefinedVarException.class, () -> JoinCommand.execute(new String[]{"$str1", "$undef_var"}, context));
        context.close();
    }

    @Test
    void joinNoArgs() throws IOException {
        ExecutionContext context = newCtx();
        assertThrows(XPkgArgLenException.class, () -> JoinCommand.execute(new String[]{}, context));
        context.close();
    }
}
