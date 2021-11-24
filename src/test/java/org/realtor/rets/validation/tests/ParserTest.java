/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/tests/ParserTest.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.tests;

import org.junit.jupiter.api.Test;

import org.realtor.rets.validation.ValidationExpressionEvaluator;
import org.realtor.rets.validation.terms.*;

import java.text.ParseException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * ParserTest.java Created Sep 5, 2003
 * <p>
 * <p>
 * Copyright 2003, Avantia inc.
 *
 * @author scohen
 * @version $Revision: 1.2 $
 */
public class ParserTest {
    private ValidationExpressionEvaluator evaluator;

    @Test
    public void testSingleTerm() {
        NumericTerm rv = (NumericTerm) evaluateExpression("5");

        assertEquals(rv.getValue(), new Double(5));
    }

    public void testStringAddition() {
        Term result = evaluateExpression("\"3\" + \"1\"");
        assertEquals(new Double(4), result.getValue());
    }

    public void testSimpleAddition() {
        NumericTerm result = (NumericTerm) evaluateExpression("5 + 1");
        assertEquals(new Double(6), result.getValue());
    }

    public void testSimpleSubtraction() {
        NumericTerm result = (NumericTerm) evaluateExpression("10 - 5");
        assertEquals(new Double(5), result.getValue());
    }

    public void testMultiplePositiveSubtraction() {
        NumericTerm result = (NumericTerm) evaluateExpression("20 - 3 - 10");
        assertEquals(new Double(7), result.getValue());
    }

    public void testNegativeSubtraction() {
        NumericTerm term = (NumericTerm) evaluateExpression("3 - 5");
        assertEquals(new Double(-2), term.getValue());
    }

    public void testDivision() {
        NumericTerm term = (NumericTerm) evaluateExpression("10/5");
        assertEquals(new Double(2), term.getValue());
    }

    public void testMultipleNegative() {
        NumericTerm term = (NumericTerm) evaluateExpression("3 - 5 - 8");
        assertEquals(new Double(-10), term.getValue());
    }

    public void testMultipleAdds() {
        NumericTerm term = (NumericTerm) evaluateExpression("5 + 6 + 3");
        assertEquals(new Double(14), term.getValue());
    }

    public void testMultipleAssortedOperations() {
        NumericTerm term = (NumericTerm) evaluateExpression("5 * 3 + 4");
        assertEquals(new Double(19), term.getValue());
    }

    public void testFourOperations() {
        NumericTerm term = (NumericTerm) evaluateExpression("3 * 4 * 6 * 10");
        assertEquals(new Double(720), term.getValue());
    }

    public void testSimpleOrderOfOperations() {
        NumericTerm term = (NumericTerm) evaluateExpression("5 + 6 * 8");
        assertEquals(new Double(53), term.getValue());
    }

    public void testComplexOrderOfOperations() {
        NumericTerm term = (NumericTerm) evaluateExpression("5 - 6 * 5 - 2");
        assertEquals(new Double(-27), term.getValue());
    }

    public void testMoreComplexOrderOfOperations() {
        NumericTerm term = (NumericTerm) evaluateExpression("5 - 6 * 5 * 3 - 2");
        assertEquals(new Double(-87), term.getValue());
    }

    public void testSimpleParenthetical() {
        NumericTerm term = (NumericTerm) evaluateExpression("(3 * 5)");
        assertEquals(new Double(15), term.getValue());
    }

    public void testUselessParenthetical() {
        NumericTerm term = (NumericTerm) evaluateExpression("(((32)))");
        assertEquals(new Double(32), term.getValue());
    }

    public void testPrefixParenthetical() {
        NumericTerm term = (NumericTerm) evaluateExpression("(3 + 4) * 2");
        assertEquals(new Double(14), term.getValue());
    }

    public void testPostfixParenthetical() {
        NumericTerm term = (NumericTerm) evaluateExpression("2 * ( 3 + 4)");
        assertEquals(new Double(14), term.getValue());
    }

    public void testNestedParenthetical() {
        NumericTerm term = (NumericTerm) evaluateExpression("((1 + 9) + 7) * 3");
        assertEquals(new Double(51), term.getValue());
    }

    public void testComplexParenthetica() {
        NumericTerm term = (NumericTerm) evaluateExpression("((2+4)*(7-2)*3)+5");
        assertEquals(new Double(95), term.getValue());
    }

    public void testMaddeninglyComplexParenthetical() {
        NumericTerm term = (NumericTerm) evaluateExpression(
                "(((2+4)* 6) * 2) - (6 * 5) + 4 - 2");
        assertEquals(new Double(44), term.getValue());
    }

    public void testSimpleTripleParenthetical() {
        NumericTerm term = (NumericTerm) evaluateExpression("( 3 + 4 + 5 + 6)");
        assertEquals(new Double(18), term.getValue());
    }

    public void testTripleParenthetical() {
        NumericTerm term = (NumericTerm) evaluateExpression(
                "( 3 + (4 + 5) + 7)");
        assertEquals(new Double(19), term.getValue());
    }

    public void testSimpleBoolean() {
        BooleanTerm term = (BooleanTerm) evaluateExpression("(3 = 3)");
        assertEquals(new Boolean(true), term.getValue());
    }

    public void testSimpleGreaterThan() {
        Term term = evaluateExpression("(10 > 3)");
        assertEquals(new Boolean(true), term.getValue());
    }

    public void testArithmeticComparison() {
        Term term = evaluateExpression(" (10 + 4) = 14");
        assertEquals(new Boolean(true), term.getValue());
    }

    public void greaterThanTest() {
        Term term = evaluateExpression("10 > 3");
        assertEquals(new Boolean(true), term.getValue());
    }

    public void testGreaterThanOrEqualTo() {
        Term term = evaluateExpression("10 >= 9");
        assertEquals(new Boolean(true), term.getValue());
        term = evaluateExpression("10 >= 10");
        assertEquals(new Boolean(true), term.getValue());
        term = evaluateExpression("10 >= 11");
        assertEquals(new Boolean(false), term.getValue());
    }

    public void testLessThanOrEqualTo() {
        Term term = evaluateExpression("9 <= 10");
        assertEquals(new Boolean(true), term.getValue());
        term = evaluateExpression("10 <= 10");
        assertEquals(new Boolean(true), term.getValue());
        term = evaluateExpression("11 <= 10");
        assertEquals(new Boolean(false), term.getValue());
    }

    @Test
    public void testComplexBooleanExpression() {
        String expr = "(3 + 4) > (3 * 4)";
        Term term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(false), term.getValue());

        expr = "(3+4) > 3 * 4";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(false), term.getValue());

        expr = "(3+4) < (3 * 4)";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());

        expr = "(3 + 4 ) < 3 * 4";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());
    }

    @Test
    public void testStringComparison() {
        String expr = "\"One\" = \"One\"";
        Term term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());
        expr = "\"One\" = \"one\"";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(false), term.getValue());
        expr = "\"One\" != \"one\"";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());
    }

    public void testContainsOperator() {
        String expr = "\"Everyone\" .CONTAINS. \"very\"";
        Term term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());

        expr = "\"Someone\" .CONTAINS. \"everyone\"";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(false), term.getValue());
    }

    public void testOrOperator() {
        String expr = "(\"Someone\" .CONTAINS. \"me\") .OR. ( \"Test\" = \"false\")";
        Term term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());
        expr = "(\"Someone\" .CONTAINS. \"time\") .OR. ( \"Test\" = \"false\")";
//        assertEquals(expr, new Boolean(true), term.getValue());
    }

    /*
            public void testQuotedDateAddition() {
                    String expr = " (\"2003-09-15\" + \"P3D\") = 2003-09-18 ";
                    Term term = evaluateExpression(expr);
                    assertEquals(expr,new Boolean(true),term.getValue());
            }
            */
    public void testDateComparison() {
        String expr = "2000-06-30 = 2000-06-30";
        Term term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());
    }

    public void testDateAddition() {
        String expr = "2002-09-10 + 5";
        Term term = evaluateExpression(expr);

//        try {
//            assertEquals(expr, new DateTerm("2002-09-15").getValue(),
//                    term.getValue());
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

    public void testDivisionOperator() {
        String expr = " 10 / 5";
        Term term = evaluateExpression(expr);
//        assertEquals(expr, new Double(2), term.getValue());
        expr = "7 .MOD. 3";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Double(1), term.getValue());
    }

    public void testUnaryNot() {
        String expr = ".NOT. ( 3 = 5)";
        Term term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());
    }

    public void testComplexBooleanExpressionWithNot() {
        String expr = (" (.NOT. 2003-09-15 = 2003-09-15) .OR. (  ( 3 = 5))");
        Term term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(false), term.getValue());
        expr = "(.NOT. 3 = 98 .AND. 25 = 5)";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());
        expr = "(100 + 50 - 75) = ( 70 + 5)";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());
    }

    public void testDateIntervalExpression() throws ParseException {
        String expr = "(2003-09-15 + P1D)";
        Term term = evaluateExpression(expr);
//        assertEquals(expr, new DateTerm("2003-09-16").getValue(),
//                term.getValue());
        expr = "(2003-09-15 - P1D)";
        term = evaluateExpression(expr);
//        assertEquals(expr, new DateTerm("2003-09-14").getValue(),
//                term.getValue());
    }

    public void testDateSubtraction() {
        String expr = "(2003-09-11 - 2003-09-10) = P1D";
        Term term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());
        expr = "(2003-09-11 - 2003-09-04) = P1W";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());
        expr = "(2003-09-11 - 2002-09-11) = P1Y";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());
    }

    public void testVariableExpression() throws ParseException {
        HashMap values = new HashMap();
        values.put("ListPrice", new NumericTerm("100000"));
        values.put("AgentName", new StringTerm("John Smith"));
        values.put("ListDate", new DateTerm("2003-09-14"));
        setSymbolTable(values);

        String expr = "( ListPrice  = 100000)";
        Term term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());

        expr = "(ListPrice - 1000 ) = 99000";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());

        expr = "(AgentName = \"John Smith\")";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());
        expr = "ListDate = 2003-09-14";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());
        expr = "(2003-09-19 -  P1D)";
        term = evaluateExpression(expr);
//        assertEquals(expr, new DateTerm("2003-09-18").getValue(),
//                term.getValue());
    }

    public void testComplexDateMath() throws Exception {
        Term term;
        String expr;

        HashMap values = new HashMap();
        values.put("Sept11", new DateTerm("2001-09-11"));
        values.put(".TODAY.", new DateTerm());
        setSymbolTable(values);

        expr = "(2003-09-18 - P1D  ) < 2003-09-18";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());

        expr = "(Sept11 <=  ( .TODAY. - P3M))";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());

        expr = "(( .TODAY. - P1M) < .TODAY.)";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());

        expr = "(Sept11 < ( .TODAY. - P1Y)) .AND.   ( .TODAY. > Sept11 - P3M))";
        term = evaluateExpression(expr);
//        assertEquals(expr, new Boolean(true), term.getValue());
    }

    public void testEverything() throws Exception {
        HashMap values = new HashMap();
        values.put("Sept11", new DateTerm("2001-09-11"));
        values.put(".TODAY.", new DateTerm());
        setSymbolTable(values);

        String expr = " (.TODAY. - ( 18 .MOD. 4) )  > ( .TODAY. - ( 4 * 3 )) .AND. ( Sept11 - P1Y < .TODAY.)";

        Term term = evaluateExpression(expr);
        assertEquals(new Boolean(true), term.getValue());
    }

    /**
     * @param values
     */
    private void setSymbolTable(HashMap values) {
        evaluator.setSymbolTable(values);
    }

    private Term evaluateExpression(String exp) {
        try {
            Term rv = evaluator.eval(exp);

            return rv;
        } catch (InvalidTermException e) {
            e.printStackTrace();
        }

        return null;
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        evaluator = new ValidationExpressionEvaluator();
    }
}
