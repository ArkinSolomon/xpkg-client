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

package net.xpkgclient;

import net.xpkgclient.commands.CommandName;
import net.xpkgclient.exceptions.QuickHandles;
import net.xpkgclient.exceptions.XPkgArgLenException;
import net.xpkgclient.exceptions.XPkgExecutionException;
import net.xpkgclient.exceptions.XPkgImmutableVarException;
import net.xpkgclient.exceptions.XPkgInternalException;
import net.xpkgclient.exceptions.XPkgInvalidBoolStatement;
import net.xpkgclient.exceptions.XPkgInvalidCallException;
import net.xpkgclient.exceptions.XPkgParseException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.exceptions.XPkgUndefinedVarException;
import net.xpkgclient.packagemanager.ExecutionContext;
import net.xpkgclient.packagemanager.ParseHelper;
import net.xpkgclient.vars.XPkgBool;
import net.xpkgclient.vars.XPkgString;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ConfigSetupExtension.class)
public class ParseHelperTests {

    //Basic contexts that are shared among many tests
    private static ExecutionContext context;

    //Setup and cleanup execution context
    @BeforeAll
    public static void setupBeforeClass() throws XPkgInvalidCallException, IOException, XPkgImmutableVarException {
        context = ExecutionContext.createBlankContext();
        context.setVar("$str", new XPkgString("String that should screw things up"));
        context.setVar("$bool_t", new XPkgBool(true));
        context.setVar("$bool_f", new XPkgBool(false));
        context.setVar("$str1", new XPkgString("String 1"));
        context.setVar("$str2", new XPkgString("String 2"));
    }

    @AfterAll
    public static void cleanupAfterClass() {
        context.close();
    }

    @Test
    void testValidVarNameNormal() {
        assertTrue(ParseHelper.isValidVarName("$aVar"));
    }

    @Test
    void testValidVarNameWithSpace() {
        assertTrue(ParseHelper.isValidVarName("$a_variable"));
    }

    @Test
    void testValidVarNameWithNumbers() {
        assertTrue(ParseHelper.isValidVarName("$hello_123_there"));
    }

    @Test
    void testValidVarNameWithNull() {
        assertFalse(ParseHelper.isValidVarName(null));
    }

    @Test
    void testValidVarNameWithNoName() {
        assertFalse(ParseHelper.isValidVarName("$"));
    }

    @Test
    void testValidVarNameHyphen() {
        assertFalse(ParseHelper.isValidVarName("$variable-with-hyphen"));
    }

    @Test
    void testValidVarNameSpace() {
        assertFalse(ParseHelper.isValidVarName("$spaced var"));
    }

    @Test
    void testValidVarNameDollarSignAsName() {
        assertFalse(ParseHelper.isValidVarName("$$"));
    }

    @Test
    void testValidVarNameDollarSignInName() {
        assertFalse(ParseHelper.isValidVarName("$thi$_has_a_dollar_in_it"));
    }

    @Test
    void testValidVarNameWithOpenBracket() {
        assertFalse(ParseHelper.isValidVarName("$bracket_open_[_var"));
    }

    @Test
    void testValidVarNameWithCloseBracket() {
        assertFalse(ParseHelper.isValidVarName("$bracket_close_]_var"));
    }

    @Test
    void testValidVarNameWithOpenBrace() {
        assertFalse(ParseHelper.isValidVarName("$brace_open_{_var"));
    }

    @Test
    void testValidVarNameWithCloseBrace() {
        assertFalse(ParseHelper.isValidVarName("$brace_close_}_var"));
    }

    @Test
    void testValidVarNameWithOpenParentheses() {
        assertFalse(ParseHelper.isValidVarName("$parentheses_open_)_var"));
    }

    @Test
    void testValidVarNameWithCloseParentheses() {
        assertFalse(ParseHelper.isValidVarName("$parentheses_close_)_var"));
    }

    @Test
    void testValidVarNameWithOpenAngle() {
        assertFalse(ParseHelper.isValidVarName("$parentheses_open_<_var"));
    }

    @Test
    void testValidVarNameWithCloseAngle() {
        assertFalse(ParseHelper.isValidVarName("$parentheses_close_>_var"));
    }

    @Test
    void testValidVarNameWithTilde() {
        assertFalse(ParseHelper.isValidVarName("$tilde~variable"));
    }

    @Test
    void testValidVarNameWithQuote() {
        assertFalse(ParseHelper.isValidVarName("$quote_\"_var"));
    }

    @Test
    void testValidVarNameWithSingleQuote() {
        assertFalse(ParseHelper.isValidVarName("$single_quote_'_var"));
    }

    @Test
    void testValidVarNameWithLineBreak() {
        assertFalse(ParseHelper.isValidVarName("$this_var_has_a_\nlinebreak"));
    }

    @Test
    void testValidVarNameWithPlus() {
        assertFalse(ParseHelper.isValidVarName("$variable_with_+_in_it"));
    }

    @Test
    void testValidVarNameWithEquals() {
        assertFalse(ParseHelper.isValidVarName("$variable_with_=_in_it"));
    }

    @Test
    void testValidVarNameStartsWithNumber() {
        assertFalse(ParseHelper.isValidVarName("$234_var"));
    }

    @Test
    void testValidVarNameOnlyNumber() {
        assertFalse(ParseHelper.isValidVarName("$234"));
    }

    @Test
    void testValidPathNormal() {
        assertTrue(ParseHelper.isValidPath("/a/path/to/something"));
    }

    @Test
    void testValidPathEmpty() {
        assertTrue(ParseHelper.isValidPath(""));
    }

    @Test
    void testValidPathSpace() {
        assertFalse(ParseHelper.isValidPath(" "));
    }

    @Test
    void testValidPathMultiSpace() {
        assertFalse(ParseHelper.isValidPath("   "));
    }

    @Test
    void testValidPathSingleSlash() {
        assertTrue(ParseHelper.isValidPath("/"));
    }

    @Test
    void testValidPathOnePiece() {
        assertTrue(ParseHelper.isValidPath("/path_one_piece"));
    }

    @Test
    void testValidPathTrailingSlash() {
        assertTrue(ParseHelper.isValidPath("/a/path/to/something/with/a/slash"));
    }

    @Test
    void testValidPathDot() {
        assertFalse(ParseHelper.isValidPath("/a/path/to/something/with/./in/it"));
    }

    @Test
    void testValidPathDotDot() {
        assertFalse(ParseHelper.isValidPath("/a/path/to/../something/bad"));
    }

    @Test
    void testValidPathTilde() {
        assertFalse(ParseHelper.isValidPath("/a/path/to/~/something/bad/"));
    }

    @Test
    void testValidPathPercent() {
        assertFalse(ParseHelper.isValidPath("/%APPDATA%/evil/path"));
    }

    @Test
    void testValidPathBackslashes() {
        assertFalse(ParseHelper.isValidPath("\\safe\\path\\but\\backslashes\\"));
    }

    @Test
    void testValidPathOneBackslash() {
        assertFalse(ParseHelper.isValidPath("/a/person/thinks/that\\ spaces/need backslashes"));
    }

    @Test
    void testValidPathNoStartSlash() {
        assertFalse(ParseHelper.isValidPath("a/invalid/path"));
    }

    @Test
    void testTruthConstantTrue() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE"}, context));
    }

    @Test
    void testTruthConstantFalse() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertFalse(ParseHelper.isTrue(new String[]{"FALSE"}, context));
    }

    @Test
    void testTruthConstantTrueCaseInsensitive() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"tRuE"}, context));
    }

    @Test
    void testTruthConstantFalseCaseInsensitive() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertFalse(ParseHelper.isTrue(new String[]{"faLSe"}, context));
    }

    @Test
    void testTruthConstantInvalid() {
        assertThrows(XPkgInvalidBoolStatement.class, () -> ParseHelper.isTrue(new String[]{"MAYBE"}, context));
    }

    @Test
    void testTruthOrDoubleTrue() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "|", "TRUE"}, context));
    }

    @Test
    void testTruthTrueOrFalse() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "|", "FALSE"}, context));
    }

    @Test
    void testTruthFalseOrTrue() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"FALSE", "|", "TRUE"}, context));
    }

    @Test
    void testTruthOrTripleTrue() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "|", "TRUE", "|", "TRUE"}, context));
    }

    @Test
    void testTruthOrTwoTrueOneFalse() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "|", "TRUE", "|", "FALSE"}, context));
    }

    @Test
    void testTruthOrTwoFalseOneTrue() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"FALSE", "|", "TRUE", "|", "FALSE"}, context));
    }

    @Test
    void testTruthOrTripleFalse() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertFalse(ParseHelper.isTrue(new String[]{"FALSE", "|", "FALSE", "|", "FALSE"}, context));
    }

    @Test
    void testTruthTrueVar() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"$bool_t"}, context));
    }

    @Test
    void testTruthFalseVar() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertFalse(ParseHelper.isTrue(new String[]{"$bool_f"}, context));
    }

    @Test
    void testTruthSingleVarTypeMismatch() {
        assertThrows(XPkgTypeMismatchException.class, () -> ParseHelper.isTrue(new String[]{"$str"}, context));
    }

    @Test
    void testTruthSingleVarNonExistent() {
        assertThrows(XPkgUndefinedVarException.class, () -> ParseHelper.isTrue(new String[]{"$undef_var"}, context));
    }

    @Test
    void testTruthUndefinedVarOrTypeMismatchVarUndefFirst() {
        assertThrows(XPkgUndefinedVarException.class, () -> ParseHelper.isTrue(new String[]{"$undef_var", "|", "$str"}, context));
    }

    @Test
    void testTruthUndefinedVarOrTypeMismatchVarTypeMismatchFirst() {
        assertThrows(XPkgTypeMismatchException.class, () -> ParseHelper.isTrue(new String[]{"$str", "|", "$undef_var"}, context));
    }

    @Test
    void testTruthOrDoubleUndefinedVar() {
        assertThrows(XPkgUndefinedVarException.class, () -> ParseHelper.isTrue(new String[]{"$undef_var", "|", "$undef_var"}, context));
    }

    @Test
    void testTruthOrDoubleTypeMismatchVar() {
        assertThrows(XPkgTypeMismatchException.class, () -> ParseHelper.isTrue(new String[]{"$str", "|", "$str"}, context));
    }

    @Test
    void testTruthOrDoubleTruthyVar() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"$bool_t", "|", "$bool_t"}, context));
    }

    @Test
    void testTruthTruthyVarOrTypeMismatchVarTruthyFirst() {
        assertThrows(XPkgTypeMismatchException.class, () -> ParseHelper.isTrue(new String[]{"$bool_t", "|", "$str"}, context));
    }

    @Test
    void testTruthTruthyVarOrTypeMismatchVarTypeMismatchFirst() {
        assertThrows(XPkgTypeMismatchException.class, () -> ParseHelper.isTrue(new String[]{"$str", "|", "$bool_t"}, context));
    }


    @Test
    void testTruthTruthyVarOrUndefinedVarTruthyFirst() {
        assertThrows(XPkgUndefinedVarException.class, () -> ParseHelper.isTrue(new String[]{"$bool_t", "|", "$undef_var"}, context));
    }

    @Test
    void testTruthTruthyVarOrUndefinedVarUndefinedFirst() {
        assertThrows(XPkgUndefinedVarException.class, () -> ParseHelper.isTrue(new String[]{"$undef_var", "|", "$bool_t"}, context));
    }

    @Test
    void testTruthAndDoubleTrue() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "&", "TRUE"}, context));
    }

    @Test
    void testTruthTrueAndFalse() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertFalse(ParseHelper.isTrue(new String[]{"TRUE", "&", "FALSE"}, context));
    }

    @Test
    void testTruthFalseAndTrue() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertFalse(ParseHelper.isTrue(new String[]{"FALSE", "&", "TRUE"}, context));
    }

    @Test
    void testTruthAndTripleTrue() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "&", "TRUE", "&", "TRUE"}, context));
    }

    @Test
    void testTruthAndTwoTrueOneFalse() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertFalse(ParseHelper.isTrue(new String[]{"TRUE", "&", "TRUE", "&", "FALSE"}, context));
    }

    @Test
    void testTruthAndTwoFalseOneTrue() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertFalse(ParseHelper.isTrue(new String[]{"FALSE", "&", "TRUE", "&", "FALSE"}, context));
    }

    @Test
    void testTruthAndTripleFalse() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertFalse(ParseHelper.isTrue(new String[]{"FALSE", "&", "FALSE", "&", "FALSE"}, context));
    }

    @Test
    void testTruthUndefinedVarAndTypeMismatchVarUndefFirst() {
        assertThrows(XPkgUndefinedVarException.class, () -> ParseHelper.isTrue(new String[]{"$undef_var", "&", "$str"}, context));
    }

    @Test
    void testTruthUndefinedVarAndTypeMismatchVarTypeMismatchFirst() {
        assertThrows(XPkgTypeMismatchException.class, () -> ParseHelper.isTrue(new String[]{"$str", "&", "$undef_var"}, context));
    }

    @Test
    void testTruthAndDoubleUndefinedVar() {
        assertThrows(XPkgUndefinedVarException.class, () -> ParseHelper.isTrue(new String[]{"$undef_var", "&", "$undef_var"}, context));
    }

    @Test
    void testTruthAndDoubleTypeMismatchVar() {
        assertThrows(XPkgTypeMismatchException.class, () -> ParseHelper.isTrue(new String[]{"$str", "&", "$str"}, context));
    }

    @Test
    void testTruthAndDoubleTruthyVar() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"$bool_t", "&", "$bool_t"}, context));
    }

    @Test
    void testTruthTruthyVarAndTypeMismatchVarTruthyFirst() {
        assertThrows(XPkgTypeMismatchException.class, () -> ParseHelper.isTrue(new String[]{"$bool_t", "&", "$str"}, context));
    }

    @Test
    void testTruthTruthyVarAndTypeMismatchVarTypeMismatchFirst() {
        assertThrows(XPkgTypeMismatchException.class, () -> ParseHelper.isTrue(new String[]{"$str", "&", "$bool_t"}, context));
    }


    @Test
    void testTruthTruthyVarAndUndefinedVarTruthyFirst() {
        assertThrows(XPkgUndefinedVarException.class, () -> ParseHelper.isTrue(new String[]{"$bool_t", "&", "$undef_var"}, context));
    }

    @Test
    void testTruthTruthyVarAndUndefinedVarUndefinedFirst() {
        assertThrows(XPkgUndefinedVarException.class, () -> ParseHelper.isTrue(new String[]{"$undef_var", "&", "$bool_t"}, context));
    }

    @Test
    void testTruthTrueAndFalseOrFalse() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertFalse(ParseHelper.isTrue(new String[]{"TRUE", "&", "FALSE", "|", "FALSE"}, context));
    }

    @Test
    void testTruthTrueAndFalseOrTrue() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "&", "FALSE", "|", "TRUE"}, context));
    }

    @Test
    void testTruthTrueAndTrueOrTrue() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "&", "TRUE", "|", "FALSE"}, context));
    }

    @Test
    void testTruthFalseAndFalseOrTrue() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"FALSE", "&", "FALSE", "|", "TRUE"}, context));
    }

    @Test
    void testTruthFalseAndTrueOrTrue() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"FALSE", "&", "TRUE", "|", "TRUE"}, context));
    }

    @Test
    void testTruthTrueOrFalseAndFalse() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "|", "FALSE", "&", "FALSE"}, context));
    }

    @Test
    void testTruthTrueOrFalseAndTrue() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "|", "FALSE", "&", "TRUE"}, context));
    }

    @Test
    void testTruthTrueOrTrueAndTrue() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "|", "TRUE", "&", "FALSE"}, context));
    }

    @Test
    void testTruthFalseOrFalseAndTrue() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertFalse(ParseHelper.isTrue(new String[]{"FALSE", "|", "FALSE", "&", "TRUE"}, context));
    }

    @Test
    void testTruthFalseOrTrueAndTrue() throws XPkgExecutionException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgParseException {
        assertTrue(ParseHelper.isTrue(new String[]{"FALSE", "|", "TRUE", "&", "TRUE"}, context));
    }

    @Test
    void testTruthTwoAndsTogetherSameArg() {
        assertThrows(XPkgInvalidBoolStatement.class, () -> ParseHelper.isTrue(new String[]{"TRUE", "&&", "TRUE"}, context));
    }

    @Test
    void testTruthTwoAndsTogetherDifferentArgs() {
        assertThrows(XPkgInvalidBoolStatement.class, () -> ParseHelper.isTrue(new String[]{"TRUE", "&", "&", "TRUE"}, context));
    }

    @Test
    void testTruthTwoOrsTogetherSameArg() {
        assertThrows(XPkgInvalidBoolStatement.class, () -> ParseHelper.isTrue(new String[]{"TRUE", "||", "TRUE"}, context));
    }

    @Test
    void testTruthTwoOrsTogetherDifferentArgs() {
        assertThrows(XPkgInvalidBoolStatement.class, () -> ParseHelper.isTrue(new String[]{"TRUE", "|", "|", "TRUE"}, context));
    }

    @Test
    void testTruthAndOrTogetherSameArg() {
        assertThrows(XPkgInvalidBoolStatement.class, () -> ParseHelper.isTrue(new String[]{"TRUE", "&|", "TRUE"}, context));
    }

    @Test
    void testTruthAndOrTogetherDifferentArgs() {
        assertThrows(XPkgInvalidBoolStatement.class, () -> ParseHelper.isTrue(new String[]{"TRUE", "&", "|", "TRUE"}, context));
    }

    @Test
    void testTruthOrAndTogetherSameArg() {
        assertThrows(XPkgInvalidBoolStatement.class, () -> ParseHelper.isTrue(new String[]{"TRUE", "|&", "TRUE"}, context));
    }

    @Test
    void testTruthOrAndTogetherDifferentArgs() {
        assertThrows(XPkgInvalidBoolStatement.class, () -> ParseHelper.isTrue(new String[]{"TRUE", "|", "&", "TRUE"}, context));
    }

    @Test
    void testValidResourceIdNormal() {
        assertTrue(ParseHelper.isValidResourceId("DuxK5knpABld0Xo1KbtBu4LdmjSNPwjD"));
    }

    @Test
    void testValidResourceIdSpace() {
        assertFalse(ParseHelper.isValidResourceId("DuxK5knpABld0Xo1Kbt u4LdmjSNPwjD"));
    }

    @Test
    void testValidResourceIdShort() {
        assertFalse(ParseHelper.isValidResourceId("DuxK5knpABld0Xo1Kbt"));
    }

    @Test
    void testValidResourceIdInvalidChar() {
        assertFalse(ParseHelper.isValidResourceId("DuxK5knpA*#d0Xo1KbtBu4LdmjSNPwjD"));
    }

    @Test
    void testGetStrSingleWord() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgInternalException {
        assertEquals("foobar", ParseHelper.getStr(new String[]{"foobar"}, context));
    }

    @Test
    void testGetStrMultiWord() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgInternalException {
        assertEquals("foo bar", ParseHelper.getStr(new String[]{"foo", "bar"}, context));
    }

    @Test
    void testGetStrSingleVar() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgInternalException {
        assertEquals("String 1", ParseHelper.getStr(new String[]{"$str1"}, context));
    }

    @Test
    void testGetStrSingleUndef() {
        assertThrows(XPkgUndefinedVarException.class, () -> ParseHelper.getStr(new String[]{"$undefined_var"}, context));
    }

    @Test
    void testGetStrMultiVar() {
        assertThrows(XPkgArgLenException.class, () -> {
            try {
                ParseHelper.getStr(new String[]{"$str1", "$str2"}, context);
            } catch (XPkgInternalException e) {
                throw QuickHandles.handleGetStr(CommandName.__INTERNAL_TEST_COMMAND, e);
            }
        });
    }

    @Test
    void testGetStrMultiVarSecondUndef() {
        assertThrows(XPkgArgLenException.class, () -> {
            try {
                ParseHelper.getStr(new String[]{"$str1", "$undefined_var"}, context);
            } catch (XPkgInternalException e) {
                throw QuickHandles.handleGetStr(CommandName.__INTERNAL_TEST_COMMAND, e);
            }
        });
    }

    @Test
    void testGetStrMismatch() {
        assertThrows(XPkgTypeMismatchException.class, () -> {
            try {
                ParseHelper.getStr(new String[]{"$bool_t"}, context);
            } catch (XPkgInternalException e) {
                throw QuickHandles.handleGetStr(CommandName.__INTERNAL_TEST_COMMAND, e);
            }
        });
    }
}
