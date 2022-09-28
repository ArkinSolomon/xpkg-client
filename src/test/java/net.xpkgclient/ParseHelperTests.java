package net.xpkgclient;

import net.xpkgclient.exceptions.XPkgImmutableVarException;
import net.xpkgclient.exceptions.XPkgInvalidBoolStatement;
import net.xpkgclient.exceptions.XPkgInvalidCallException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.exceptions.XPkgUndefinedVarException;
import net.xpkgclient.vars.XPkgBool;
import net.xpkgclient.vars.XPkgString;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ConfigSetupExtension.class)
public class ParseHelperTests {

    //Basic contexts that are shared among many tests
    private static ExecutionContext context;

    //Setup execution contexts
    @BeforeAll
    public static void setupBeforeClass() throws XPkgInvalidCallException, IOException, XPkgImmutableVarException {
        context = ExecutionContext.createBlankContext();
        context.setVar("$str", new XPkgString("String that should screw things up"));
        context.setVar("$bool_t", new XPkgBool(true));
        context.setVar("$bool_f", new XPkgBool(false));
        context.setVar("$str1", new XPkgString("String 1"));
        context.setVar("$str2", new XPkgString("String 2"));
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
    void testTruthConstantTrue() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE"}, context));
    }

    @Test
    void testTruthConstantFalse() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertFalse(ParseHelper.isTrue(new String[]{"FALSE"}, context));
    }

    @Test
    void testTruthConstantTrueCaseInsensitive() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"tRuE"}, context));
    }

    @Test
    void testTruthConstantFalseCaseInsensitive() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertFalse(ParseHelper.isTrue(new String[]{"faLSe"}, context));
    }

    @Test
    void testTruthConstantInvalid() {
        assertThrows(XPkgInvalidBoolStatement.class, () -> ParseHelper.isTrue(new String[]{"MAYBE"}, context));
    }

    @Test
    void testTruthOrDoubleTrue() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "|", "TRUE"}, context));
    }

    @Test
    void testTruthTrueOrFalse() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "|", "FALSE"}, context));
    }

    @Test
    void testTruthFalseOrTrue() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"FALSE", "|", "TRUE"}, context));
    }

    @Test
    void testTruthOrTripleTrue() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "|", "TRUE", "|", "TRUE"}, context));
    }

    @Test
    void testTruthOrTwoTrueOneFalse() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "|", "TRUE", "|", "FALSE"}, context));
    }

    @Test
    void testTruthOrTwoFalseOneTrue() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"FALSE", "|", "TRUE", "|", "FALSE"}, context));
    }

    @Test
    void testTruthOrTripleFalse() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertFalse(ParseHelper.isTrue(new String[]{"FALSE", "|", "FALSE", "|", "FALSE"}, context));
    }

    @Test
    void testTruthTrueVar() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"$bool_t"}, context));
    }

    @Test
    void testTruthFalseVar() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
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
    void testTruthOrDoubleTruthyVar() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
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
    void testTruthAndDoubleTrue() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "&", "TRUE"}, context));
    }

    @Test
    void testTruthTrueAndFalse() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertFalse(ParseHelper.isTrue(new String[]{"TRUE", "&", "FALSE"}, context));
    }

    @Test
    void testTruthFalseAndTrue() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertFalse(ParseHelper.isTrue(new String[]{"FALSE", "&", "TRUE"}, context));
    }

    @Test
    void testTruthAndTripleTrue() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "&", "TRUE", "&", "TRUE"}, context));
    }

    @Test
    void testTruthAndTwoTrueOneFalse() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertFalse(ParseHelper.isTrue(new String[]{"TRUE", "&", "TRUE", "&", "FALSE"}, context));
    }

    @Test
    void testTruthAndTwoFalseOneTrue() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertFalse(ParseHelper.isTrue(new String[]{"FALSE", "&", "TRUE", "&", "FALSE"}, context));
    }

    @Test
    void testTruthAndTripleFalse() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
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
    void testTruthAndDoubleTruthyVar() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
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
    void testTruthTrueAndFalseOrFalse() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertFalse(ParseHelper.isTrue(new String[]{"TRUE", "&", "FALSE", "|", "FALSE"}, context));
    }

    @Test
    void testTruthTrueAndFalseOrTrue() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "&", "FALSE", "|", "TRUE"}, context));
    }

    @Test
    void testTruthTrueAndTrueOrTrue() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "&", "TRUE", "|", "FALSE"}, context));
    }

    @Test
    void testTruthFalseAndFalseOrTrue() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"FALSE", "&", "FALSE", "|", "TRUE"}, context));
    }

    @Test
    void testTruthFalseAndTrueOrTrue() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"FALSE", "&", "TRUE", "|", "TRUE"}, context));
    }

    @Test
    void testTruthTrueOrFalseAndFalse() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "|", "FALSE", "&", "FALSE"}, context));
    }

    @Test
    void testTruthTrueOrFalseAndTrue() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "|", "FALSE", "&", "TRUE"}, context));
    }

    @Test
    void testTruthTrueOrTrueAndTrue() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"TRUE", "|", "TRUE", "&", "FALSE"}, context));
    }

    @Test
    void testTruthFalseOrFalseAndTrue() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertFalse(ParseHelper.isTrue(new String[]{"FALSE", "|", "FALSE", "&", "TRUE"}, context));
    }

    @Test
    void testTruthFalseOrTrueAndTrue() throws XPkgInvalidCallException, XPkgUndefinedVarException, XPkgTypeMismatchException, XPkgInvalidBoolStatement {
        assertTrue(ParseHelper.isTrue(new String[]{"FALSE", "|", "TRUE", "&", "TRUE"}, context));
    }

    @Test
    void testGetStrNormal() {

    }
}
