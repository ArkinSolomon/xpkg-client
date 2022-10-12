/*
 * Copyright (c) 2022. XPkg-Client Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied limitations under the License.
 */

package net.xpkgclient.commands;

import net.xpkgclient.ConfigSetupExtension;
import net.xpkgclient.Configuration;
import net.xpkgclient.ExecutionContext;
import net.xpkgclient.exceptions.XPkgArgLenException;
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.exceptions.XPkgImmutableVarException;
import net.xpkgclient.exceptions.XPkgInvalidCallException;
import net.xpkgclient.exceptions.XPkgUndefinedVarException;
import net.xpkgclient.vars.XPkgBool;
import net.xpkgclient.vars.XPkgString;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//Test the print command
@ExtendWith(ConfigSetupExtension.class)
class PrintCommandTests {

    // Basic context that is shared among many tests
    private static ExecutionContext context;

    // Setup and cleanup execution contexts
    @BeforeAll
    public static void setupBeforeClass() throws XPkgInvalidCallException, IOException, XPkgImmutableVarException {
        context = ExecutionContext.createBlankContext();
        context.setVar("$testStr", new XPkgString("Oh look at that a string"));
        context.setVar("$test2", new XPkgBool(true));
        context.setVar("$test3", new XPkgBool(false));
    }

    @AfterAll
    public static void cleanupAfterClass() {
        context.close();
    }

    // Get the string from executing a print test
    private static String printText(String[] args, ExecutionContext context) throws Throwable {

        // Create a temporary print stream to save the output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;
        System.setOut(ps);

        PrintCommand.execute(args, context);

        // Reset system.out
        System.out.flush();
        System.setOut(old);
        return baos.toString();
    }

    private static String printText(String[] args) throws Throwable {
        return printText(args, null);
    }

    // Print just regular strings
    @Test
    void testPrintOneWord() throws Throwable {
        assertEquals("Hello", printText(new String[]{"Hello"}));
    }

    @Test
    void testPrintMultiWord() throws Throwable {
        assertEquals("Hello world!", printText(new String[]{"Hello", "world!"}));
    }

    @Test
    void testPrintApostrophe() throws Throwable {
        assertEquals("Howdy there ma'am", printText(new String[]{"Howdy", "there", "ma'am"}));
    }

    @Test
    void testPrintSpaceVariable() throws Throwable {
        assertEquals(" ", printText(new String[]{"$SPACE"}, context));
    }

    @Test
    void testPrintCustomVar() throws Throwable {
        assertEquals("Oh look at that a string", printText(new String[]{"$testStr"}, context));
    }

    @Test
    void testPrintBooleanTrue() throws Throwable {
        assertEquals("TRUE", printText(new String[]{"$test2"}, context));
    }

    @Test
    void testPrintBooleanFalse() throws Throwable {
        assertEquals("FALSE", printText(new String[]{"$test3"}, context));
    }

    @Test
    void testPrintResource() throws Throwable {
        assertEquals(Configuration.getXpPath().toString(), printText(new String[]{"$XP"}, context));
    }

    @Test
    void testPrintNoArgs() {
        assertThrows(XPkgArgLenException.class, () -> PrintCommand.execute(new String[]{}, context));
    }

    @Test
    void testPrintVariableAndText() {
        assertThrows(XPkgArgLenException.class, () -> PrintCommand.execute(new String[]{"$testStr", "hello"}, context));
    }

    @Test
    void testPrintUndefinedVar() {
        assertThrows(XPkgUndefinedVarException.class, () -> PrintCommand.execute(new String[]{"$nonExistentVar"}, context));
    }

    @Test
    void testClosedContext() throws XPkgException, IOException {
        ExecutionContext closingContext = ExecutionContext.createBlankContext();
        closingContext.setVar("$closeTestStr", new XPkgString("This should throw an exception"));
        closingContext.close();
        assertThrows(XPkgInvalidCallException.class, () -> PrintCommand.execute(new String[]{"$closeTestStr"}, closingContext));
    }
}
