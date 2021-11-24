/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/operators/SubtractionOperator.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.operators;

import org.realtor.rets.validation.terms.*;
import org.realtor.rets.validation.terms.InvalidTermException;
import org.realtor.rets.validation.terms.NumericTerm;

import java.util.Calendar;
import java.util.Date;


/**
 *  SubtractionOperator.java Created Sep 8, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class SubtractionOperator extends Operator {
    public SubtractionOperator() {
        symbol = "-";
        setPrecedence(2);
    }

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.operators.Operator#apply(org.realtor.rets.validation.terms.Term, org.realtor.rets.validation.terms.Term)
     */
    public Term apply(Term t1, Term t2) throws InvalidTermException {
        if (t1.isClass(DateTerm.class)) {
            return apply((DateTerm) t1.getTerm(), t2);
        }

        return apply(TermConverter.toNumericTerm(t1),
            TermConverter.toNumericTerm(t2));
    }

    public Term apply(DateTerm t1, Term t2) throws InvalidTermException {
        Calendar c = Calendar.getInstance();
        c.setTime((Date) t1.getValue());

        if (t2.isClass(DateTerm.class)) {
            DateTerm toSubtract = (DateTerm) t2.getTerm();
            Date d1;
            Date d2;
            d1 = (Date) t1.getValue();
            d2 = (Date) toSubtract.getValue();

            return new DateIntervalTerm(d1.getTime() - d2.getTime());
        } else if (t2.isClass(DateIntervalTerm.class)) {
            DateIntervalTerm interval = (DateIntervalTerm) t2;
            c.add(Calendar.YEAR, interval.getYears() * -1);
            c.add(Calendar.MONTH, interval.getMonths() * -1);
            c.add(Calendar.DATE, interval.getDays() * -1);
            c.add(Calendar.HOUR, interval.getHours() * -1);
            c.add(Calendar.MINUTE, interval.getMinutes() * -1);
            c.add(Calendar.SECOND, interval.getSeconds() * -1);
            c.add(Calendar.DATE, (interval.getWeeks() * 7) * -1);
        } else {
            NumericTerm i = TermConverter.toNumericTerm(t2);
            Double days = (Double) i.getValue();
            c.add(Calendar.DATE, days.intValue() * -1);
        }

        return new DateTerm(c.getTime());
    }

    public Term apply(NumericTerm t1, NumericTerm t2)
        throws InvalidTermException {
        Double l1;
        Double l2;
        l1 = (Double) t1.getValue();
        l2 = (Double) t2.getValue();

        return new NumericTerm(l1.doubleValue() - l2.doubleValue());
    }
}
