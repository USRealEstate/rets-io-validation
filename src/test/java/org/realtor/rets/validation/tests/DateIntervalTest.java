/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/tests/DateIntervalTest.java,v 1.1.1.1 2003/11/21 16:20:05 rsegelman Exp $  */
package org.realtor.rets.validation.tests;

import org.junit.jupiter.api.Test;
import org.realtor.rets.validation.terms.DateIntervalTerm;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 *  DateIntervalTest.java Created Sep 18, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.1.1.1 $
 *  @author scohen
 */
public class DateIntervalTest {
    private DateIntervalTerm term;

    @Test
    public void testDays() throws ParseException {
        term.setInterval("P15D");
        assertEquals(new Integer(15), new Integer(term.getDays()));
    }

    public void testYears() throws ParseException {
        term.setInterval("P10Y");
        assertEquals(new Integer(10), new Integer(term.getYears()));
    }

    public void testWeeks() throws ParseException {
        term.setInterval("P8W");
        assertEquals(new Integer(8), new Integer(term.getWeeks()));
    }

    public void testMonths() throws ParseException {
        term.setInterval("P6M");
        assertEquals(new Integer(6), new Integer(term.getMonths()));
    }

    public void testHours() throws ParseException {
        term.setInterval("PT10H");
        assertEquals(new Integer(10), new Integer(term.getHours()));
    }

    public void testMinutes() throws ParseException {
        term.setInterval("PT72M");
        assertEquals(new Integer(72), new Integer(term.getMinutes()));
    }

    public void testSeconds() throws ParseException {
        term.setInterval("PT45S");
        assertEquals(new Integer(45), new Integer(term.getSeconds()));
    }

    public void testCombination() throws ParseException {
        term.setInterval("P1M10DT1H2M30S");
        assertEquals(new Integer(1), new Integer(term.getMonths()));
        assertEquals(new Integer(10), new Integer(term.getDays()));
        assertEquals(new Integer(1), new Integer(term.getHours()));
        assertEquals(new Integer(2), new Integer(term.getMinutes()));
        assertEquals(new Integer(30), new Integer(term.getSeconds()));

        term.setInterval("P15Y6DT10H");

        assertEquals(new Integer(15), new Integer(term.getYears()));
        assertEquals(new Integer(6), new Integer(term.getDays()));
        assertEquals(new Integer(10), new Integer(term.getHours()));
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        term = new DateIntervalTerm("24d");
    }
}
