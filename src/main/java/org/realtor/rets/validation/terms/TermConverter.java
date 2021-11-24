/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/terms/TermConverter.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.terms;

import java.text.SimpleDateFormat;

import java.util.Date;


/**
 *  TermConverter.java Created Aug 25, 2003
 *   This object takes terms of one type and tries to convert them to another type. If the conversion fails,
 *  and InvalidTermException is thrown.
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class TermConverter {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static NumericTerm toNumericTerm(Term t) throws InvalidTermException {
        // short circuit the whole method if it's already an int term.
        if (t.isClass(NumericTerm.class)) {
            return (NumericTerm) t.getTerm();
        }

        Object origValue = null;
        NumericTerm rv = null;

        try {
            origValue = t.getTerm().getValue();

            if (origValue instanceof String) {
                rv = new NumericTerm((String) origValue);
            } else if (origValue instanceof Date) {
                rv = new NumericTerm(((Date) origValue).getTime());
            } else if (t.isClass(DateIntervalTerm.class)) {
                DateIntervalTerm interval = (DateIntervalTerm) t.getTerm();
                rv = new NumericTerm(interval.getTime());
            }
        } catch (Exception ex) {
            throw new InvalidTermException("Could not convert \"" + origValue +
                "\" into an Integer");
        }

        return rv;
    }

    public static StringTerm toStringTerm(Term t) {
        Object val = t.getTerm().getValue();

        return new StringTerm(val.toString());
    }

    public static DateTerm toDateTerm(Term t) throws InvalidTermException {
        if (t.isClass(DateTerm.class)) {
            return (DateTerm) t.getTerm();
        }

        DateTerm rv = null;
        Object origValue = t.getTerm().getValue();

        try {
            rv = new DateTerm(origValue.toString());
        } catch (Exception e) {
            throw new InvalidTermException("Could not convert \"" + origValue +
                "\" to a string in YYYY-MM-DD format");
        }

        return rv;
    }

    /**
     *  Converts a term to a DateInterval term. The term to be converted can be a string that represents a
     *  Date Interval in ISO8601 format.
     * @param t The term to be converted
     * @return a date interval term if the argument can be converted.
     * @throws InvalidTermException
     */
    public static DateIntervalTerm toDateIntervalTerm(Term t)
        throws InvalidTermException {
        if (t.isClass(DateIntervalTerm.class)) {
            return (DateIntervalTerm) t.getTerm();
        }

        DateIntervalTerm rv = null;
        Object origValue = t.getTerm().getValue();

        try {
            rv = new DateIntervalTerm(origValue.toString());
        } catch (Exception e) {
            throw new InvalidTermException("Could not convert \"" + origValue +
                "\" to a string in YYYY-MM-DD format");
        }

        return rv;
    }
}
