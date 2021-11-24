/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/tests/TokenizerTest.java,v 1.1.1.1 2003/11/21 16:20:05 rsegelman Exp $  */
package org.realtor.rets.validation.tests;


import org.junit.jupiter.api.Test;
import org.realtor.rets.validation.RetsTokenParser;
import org.realtor.rets.validation.operators.*;
import org.realtor.rets.validation.terms.*;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * TokenizerTest.java Created Sep 10, 2003
 * <p>
 * <p>
 * Copyright 2003, Avantia inc.
 *
 * @author scohen
 * @version $Revision: 1.1.1.1 $
 */
public class TokenizerTest {
    private RetsTokenParser tokenizer;

    public TokenizerTest() {
    }

    @Test
    public void testNullExpression() {
        tokenizer.setExpression("");
        assertNull(tokenizer.nextToken());

        tokenizer.setExpression(" ");
        assertNull(tokenizer.nextToken());
    }

    public void testSingleNumericToken() {
        tokenizer.setExpression("3");

        Term t = (Term) tokenizer.nextToken();
        assertIntTermValue(t, 3);
        tokenizer.setExpression("150");
        t = (Term) tokenizer.nextToken();
        assertIntTermValue(t, 150);
        tokenizer.setExpression("    1500    ");
        t = (Term) tokenizer.nextToken();
        assertIntTermValue(t, 1500);
    }

    public void testSingleOperator() {
        tokenizer.setExpression("+");

        Operator oper = (Operator) tokenizer.nextToken();
        assertEquals(oper, new AdditionOperator());

        tokenizer.setExpression("-");
        oper = (Operator) tokenizer.nextToken();
        assertEquals(oper, new SubtractionOperator());

        tokenizer.setExpression("*");
        oper = (Operator) tokenizer.nextToken();
        assertEquals(oper, new MultiplicationOperator());

        tokenizer.setExpression(">");
        oper = (Operator) tokenizer.nextToken();
        assertEquals(oper, new GreaterThanOperator());

        tokenizer.setExpression("<");
        oper = (Operator) tokenizer.nextToken();
        assertEquals(oper, new LessThanOperator());

        tokenizer.setExpression("=");
        oper = (Operator) tokenizer.nextToken();
        assertEquals(oper, new EqualsOperator());
    }

    public void testCompoundOperators() {
        Operator oper;

        tokenizer.setExpression(">=");
        oper = (Operator) tokenizer.nextToken();
        assertEquals(new GreaterThanOrEqualToOperator(), oper);
        tokenizer.setExpression("!=");
        assertCorrectOperator(tokenizer.nextToken(), new NotEqualsOperator());
    }

    public void testSingleDottedOperators() {
        tokenizer.setExpression(".CONTAINS.");
        assertCorrectOperator(tokenizer.nextToken(), new ContainsOperator());
        tokenizer.setExpression(".OR.");
        assertCorrectOperator(tokenizer.nextToken(), new OrOperator());
        tokenizer.setExpression(".AND.");
        assertCorrectOperator(tokenizer.nextToken(), new AndOperator());
    }

    public void testReadSingleQuotedString() {
        tokenizer.setExpression("\"QuotedString\"");

        StringTerm term = (StringTerm) tokenizer.nextToken();
        assertEquals("QuotedString", term.getValue());

        tokenizer.setExpression("\"Quoted String\"");
        term = (StringTerm) tokenizer.nextToken();
        assertEquals("Quoted String", term.getValue());

        tokenizer.setExpression("\"     \"");
        term = (StringTerm) tokenizer.nextToken();
        assertEquals("     ", term.getValue());
    }

    public void testSimpleExpression() {
        tokenizer.setExpression("1 + 3");

        Term t = (Term) tokenizer.nextToken();
        assertIntTermValue(t, 1);

        Operator o = (Operator) tokenizer.nextToken();
        assertEquals(o, new AdditionOperator());
        assertIntTermValue((Term) tokenizer.nextToken(), 3);
    }

    public void testQuotedStrings() {
        String expr = "\"First String\" = \"SecondString\"";

        // should have three tokens;
        tokenizer.setExpression(expr);

        StringTerm firstTerm = (StringTerm) tokenizer.nextToken();
        assertCorrectOperator(tokenizer.nextToken(), new EqualsOperator());

        StringTerm secondTerm = (StringTerm) tokenizer.nextToken();
        assertEquals("First String", firstTerm.getValue());
        assertEquals("SecondString", secondTerm.getValue());
    }

    public void testGreaterThan() {
        tokenizer.setExpression("10>2");
        assertIntTermValue(tokenizer.nextToken(), 10);
        assertCorrectOperator(tokenizer.nextToken(), new GreaterThanOperator());
        assertIntTermValue(tokenizer.nextToken(), 2);
    }

    public void testParentheticalOperators() {
        tokenizer.setExpression("()");
        assertCorrectOperator(tokenizer.nextToken(), new OpenParen());
        assertCorrectOperator(tokenizer.nextToken(), new CloseParen());
    }

    public void testParenthesizedEquation() {
        tokenizer.setExpression("(1+3)");

        Operator o = (Operator) tokenizer.nextToken();
        assertIntTermValue((Term) tokenizer.nextToken(), 1);
        assertCorrectOperator(tokenizer.nextToken(), new AdditionOperator());
        assertIntTermValue(tokenizer.nextToken(), 3);
        assertCorrectOperator(tokenizer.nextToken(), new CloseParen());
    }

    public void testParentheticalGreaterThan() {
        tokenizer.setExpression("(10>2)");
        assertCorrectOperator(tokenizer.nextToken(), new OpenParen());
        assertIntTermValue(tokenizer.nextToken(), 10);
        assertCorrectOperator(tokenizer.nextToken(), new GreaterThanOperator());
        assertIntTermValue(tokenizer.nextToken(), 2);
        assertCorrectOperator(tokenizer.nextToken(), new CloseParen());
    }

    public void testNotEqualsExpression() {
        String expr = "3 != 4";
        tokenizer.setExpression(expr);
        assertIntTermValue(tokenizer.nextToken(), 3);
        assertCorrectOperator(tokenizer.nextToken(), new NotEqualsOperator());
        assertIntTermValue(tokenizer.nextToken(), 4);
    }

    public void testDateExpression() {
        String expr = "2002-09-10";
        tokenizer.setExpression(expr);
        assertEquals(DateTerm.class, tokenizer.nextToken().getClass());

        expr = "2009-09-10 = 2002-09-10";
        tokenizer.setExpression(expr);
        assertEquals(DateTerm.class, tokenizer.nextToken().getClass());
        assertCorrectOperator(tokenizer.nextToken(), new EqualsOperator());
        assertEquals(DateTerm.class, tokenizer.nextToken().getClass());
    }

    public void testDateAddition() {
        String expr = "2002-09-10 + 5";
        tokenizer.setExpression(expr);
        assertEquals(DateTerm.class, tokenizer.nextToken().getClass());
        assertCorrectOperator(tokenizer.nextToken(), new AdditionOperator());
        assertIntTermValue(tokenizer.nextToken(), 5);
    }

    public void testDateInterval() {
        String expr = "P1M";
        tokenizer.setExpression(expr);
//        assertEquals(expr, DateIntervalTerm.class,
//                tokenizer.nextToken().getClass());
    }

    public void testVariables() {
        String expr = "ListPrice";
        tokenizer.setExpression(expr);
//        assertEquals(expr, VariableTerm.class, tokenizer.nextToken().getClass());
    }

    public void testModOperator() {
        String expr = ".MOD.";
        tokenizer.setExpression(expr);
        assertCorrectOperator(tokenizer.nextToken(), new ModOperator());
        expr = "7 .MOD. 3 ";
        tokenizer.setExpression(expr);
        assertIntTermValue(tokenizer.nextToken(), 7);
        assertCorrectOperator(tokenizer.nextToken(), new ModOperator());
        assertIntTermValue(tokenizer.nextToken(), 3);
    }

    public void testNotOperator() {
        String expr = ".NOT.";
        tokenizer.setExpression(expr);
        assertCorrectOperator(tokenizer.nextToken(), new NotOperator());
        expr = (".NOT. ( 3 = 5)");
        tokenizer.setExpression(expr);
        assertCorrectOperator(tokenizer.nextToken(), new NotOperator());
        assertCorrectOperator(tokenizer.nextToken(), new OpenParen());
        assertIntTermValue(tokenizer.nextToken(), 3);
        assertCorrectOperator(tokenizer.nextToken(), new EqualsOperator());
        assertIntTermValue(tokenizer.nextToken(), 5);
        assertCorrectOperator(tokenizer.nextToken(), new CloseParen());
    }

    public void testDateRange() throws ParseException {
        String expr = "(2003-09-18 - P1D )> 2003-09-17";
        tokenizer.setExpression(expr);
        assertCorrectOperator(tokenizer.nextToken(), new OpenParen());
        assertEquals(((Term) tokenizer.nextToken()).getValue(),
                new DateTerm("2003-09-18").getValue());
        assertCorrectOperator(tokenizer.nextToken(), new SubtractionOperator());
        assertEquals(((Term) tokenizer.nextToken()).getValue(),
                new DateIntervalTerm("P1D").getValue());
        assertCorrectOperator(tokenizer.nextToken(), new CloseParen());
        assertCorrectOperator(tokenizer.nextToken(), new GreaterThanOperator());
        assertEquals(new DateTerm("2003-09-17").getValue(),
                ((DateTerm) tokenizer.nextToken()).getValue());
    }

    public void assertCorrectOperator(Object toTest, Operator expected) {
        assertEquals(expected, toTest);
    }

    private void assertIntTermValue(Object t, long val) {
//        assertTrue(t + " is not an int term", t instanceof NumericTerm);
        assertEquals(new Double(val), ((NumericTerm) t).getValue());
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        tokenizer = new RetsTokenParser();
        tokenizer.setTermFactory(new TermFactory());
    }
}
