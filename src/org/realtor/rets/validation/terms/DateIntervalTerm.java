/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/terms/DateIntervalTerm.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.terms;

import java.text.ParseException;

import java.util.regex.*;


/**
 *  DateIntervalTerm.java Created Sep 17, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class DateIntervalTerm extends AbstractTerm {
    private static long MILLIS_PER_HOUR = 3600 * 1000;
    private static long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;
    private static long MILLIS_PER_YEAR = 365 * MILLIS_PER_DAY;
    private static Pattern iso8601;
    private int days = 0;
    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;
    private int months = 0;
    private int weeks = 0;
    private int years = 0;

    DateIntervalTerm() {
        super();

        if (iso8601 == null) {
            iso8601 = Pattern.compile(
                    "P(?:(-?\\d+)Y)?(?:(-?\\d+)M)?(?:(-?\\d+)D)?(?:T(?:(-?\\d+)H)?(?:(-?\\d+)M)?(?:(-?\\d+)S)?)?");
        }
    }

    /**
     * @param l
     */
    public DateIntervalTerm(long interval) {
        this();
        setTime(interval);
    }

    /**
     *
     */
    public DateIntervalTerm(String interval) throws ParseException {
        this();
        setValue(interval);
    }

    public void setInterval(String interval) throws ParseException {
        value = interval.toUpperCase();
        parseInterval();
    }

    public long getTime() {
        long rv = 0;
        rv += (years * 365);
        rv += (months * 30);
        rv += (weeks * 7);
        rv += days;

        // now get it into seconds
        rv *= (24 * 3600);
        rv += (minutes * 60);
        rv += seconds;
        rv *= 1000;

        return rv;
    }

    public void setTime(long interval) {
        years = (int) (interval / MILLIS_PER_YEAR);
        interval -= (years * MILLIS_PER_YEAR);
        weeks = (int) (interval / (7 * MILLIS_PER_DAY));
        interval -= (weeks * (7 * MILLIS_PER_DAY));
        days = (int) (interval / MILLIS_PER_DAY);
        interval -= (days * MILLIS_PER_DAY);
        hours = (int) interval / (3600 * 1000);
        interval -= (hours * (3600 * 1000));
        minutes = (int) interval / (60 * 1000);
        interval -= (minutes * (60 * 1000));
        seconds = (int) interval / 1000;
        interval -= (seconds * 1000);
        generateDateString();
    }

    /**
     *
     */
    private void generateDateString() {
        StringBuffer date = new StringBuffer("P");

        appendTimeUnit(date, years, "Y", false);
        appendTimeUnit(date, months, "M", false);
        appendTimeUnit(date, weeks, "W", false);
        appendTimeUnit(date, days, "D", false);
        appendTimeUnit(date, hours, "H", true);
        appendTimeUnit(date, minutes, "M", true);
        appendTimeUnit(date, seconds, "S", true);

        value = date.toString();
    }

    private void appendTimeUnit(StringBuffer toAppend, int time, String label,
        boolean isTimeUnit) {
        if (time > 0) {
            if (isTimeUnit && (toAppend.indexOf("T") < 0)) {
                toAppend.append("T");
            }

            toAppend.append(time);
            toAppend.append(label);
        }
    }

    /**
     * @param interval
     * @return
     */
    private void parseInterval() throws ParseException {
        String sVal = (String) getValue();

        if (sVal.matches("P-?\\d+W")) {
            System.out.println("Matches! " + sVal);
            weeks = toInt(sVal.substring(1, sVal.indexOf("W")));
        } else {
            Matcher matcher = iso8601.matcher(sVal);

            if (matcher.find()) {
                years = toInt(matcher.group(1));
                months = toInt(matcher.group(2));
                days = toInt(matcher.group(3));
                hours = toInt(matcher.group(4));
                minutes = toInt(matcher.group(5));
                seconds = toInt(matcher.group(6));
                weeks = 0;
            } else {
                throw new ParseException(sVal +
                    " is not a valid Date interval", 0);
            }
        }
    }

    /**
     * @param string
     * @return
     */
    private int toInt(String string) {
        int rv = 0;

        if (string != null) {
            try {
                rv = Integer.parseInt(string);
            } catch (NumberFormatException e) {
            }
        }

        return rv;
    }

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.terms.Term#setValue(java.lang.Object)
     */
    public void setValue(Object val) {
        super.setValue(val);

        String sVal = val.toString();

        try {
            setInterval(sVal);
            parseInterval();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    public int getDays() {
        return days;
    }

    /**
     * @return
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * @return
     */
    public int getMonths() {
        return months;
    }

    /**
     * @return
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * @return
     */
    public int getWeeks() {
        return weeks;
    }

    /**
     * @return
     */
    public int getYears() {
        return years;
    }

    /**
     * @param i
     */
    public void setDays(int i) {
        days = i;
    }

    /**
     * @param i
     */
    public void setMinutes(int i) {
        minutes = i;
    }

    /**
     * @param i
     */
    public void setMonths(int i) {
        months = i;
    }

    /**
     * @param i
     */
    public void setSeconds(int i) {
        seconds = i;
    }

    /**
     * @param i
     */
    public void setWeeks(int i) {
        weeks = i;
    }

    /**
     * @param i
     */
    public void setYears(int i) {
        years = i;
    }

    /**
     * @return
     */
    public int getHours() {
        return hours;
    }

    /**
     * @param i
     */
    public void setHours(int i) {
        hours = i;
    }
}
