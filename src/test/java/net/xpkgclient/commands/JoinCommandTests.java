package net.xpkgclient.commands;

import net.xpkgclient.ConfigSetupExtension;
import net.xpkgclient.ExecutionContext;
import net.xpkgclient.exceptions.XPkgArgLenException;
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.exceptions.XPkgImmutableVarException;
import net.xpkgclient.exceptions.XPkgUndefinedVarException;
import net.xpkgclient.vars.XPkgString;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(ConfigSetupExtension.class)
public class JoinCommandTests {

    private ExecutionContext context;

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

    @BeforeEach
    void setupContext() throws IOException {
        context = newCtx();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @Test
    void testJoinTwoStrings() throws XPkgException {
        JoinCommand.execute(new String[]{"$str1", "$str2"}, context);
        assertEquals("String 1String 2", context.getVar("$str1").toString());
    }

    @Test
    void testJoinStringAndSpace() throws XPkgException {
        JoinCommand.execute(new String[]{"$str1", "$SPACE"}, context);
        assertEquals("String 1 ", context.getVar("$str1").toString());
    }

    @Test
    void testJoinStringToSpace() {
        assertThrows(XPkgImmutableVarException.class, () -> JoinCommand.execute(new String[]{"$SPACE", "$str2"}, context));
    }

    @Test
    void testJoinStringLiteralToString() throws XPkgException {
        JoinCommand.execute(new String[]{"$str3", "wow", "look", "a", "string"}, context);
        assertEquals("String 3wow look a string", context.getVar("$str3").toString());
    }

    @Test
    void testJoinSameStringVariable() throws XPkgException {
        JoinCommand.execute(new String[]{"$str1", "$str1"}, context);
        assertEquals("String 1String 1", context.getVar("$str1").toString());
    }

    @Test
    void testJoinStringLiteralTwoVariables() {
        assertThrows(XPkgArgLenException.class, () -> JoinCommand.execute(new String[]{"$str1", "$str2", "$str3"}, context));
    }

    @Test
    void testJoinFirstVarUndefined() throws XPkgException {
        JoinCommand.execute(new String[]{"$undef_var", "$str1"}, context);
        assertEquals("String 1", context.getVar("$undef_var").toString());
    }

    @Test
    void testJoinSecondVarUndefined() {
        assertThrows(XPkgUndefinedVarException.class, () -> JoinCommand.execute(new String[]{"$str1", "$undef_var"}, context));
    }

    @Test
    void testJoinNoArgs() {
        assertThrows(XPkgArgLenException.class, () -> JoinCommand.execute(new String[]{}, context));
    }
}
