/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/operators/AdditionOperator.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.operators;

import org.realtor.rets.validation.terms.*;

import java.util.Calendar;
import java.util.Date;


/**
 *  AdditionOperator.java Created Aug 25, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class AdditionOperator extends Operator {
    public AdditionOperator() {
        symbol = "+";
        setPrecedence(2);
    }

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.operators.Operator#apply(org.realtor.rets.validation.operators.Term, org.realtor.rets.validation.operators.Term)
     */
    public Term apply(DateTerm t1, Term t2) throws InvalidTermException {
        Calendar c = Calendar.getInstance();
        c.setTime((Date) t1.getValue());

        if (t2.isClass(DateTerm.class)) {
            throw new InvalidTermException("Cannot add two dates together");
        } else if (t2.isClass(DateIntervalTerm.class)) {
            DateIntervalTerm interval = (DateIntervalTerm) t2;
            c.add(Calendar.YEAR, interval.getYears());
            c.add(Calendar.MONTH, interval.getMonths());
            c.add(Calendar.DATE, interval.getDays());
            c.add(Calendar.HOUR, interval.getHours());
            c.add(Calendar.MINUTE, interval.getMinutes());
            c.add(Calendar.SECOND, interval.getSeconds());
        } else {
            NumericTerm i = TermConverter.toNumericTerm(t2);
            Double days = (Double) i.getValue();
            c.add(Calendar.DATE, days.intValue());
        }

        return new DateTerm(c.getTime());
    }

    public Term apply(Term t1, Term t2) throws InvalidTermException {
        if (t1.isClass(DateTerm.class)) {
            return apply((DateTerm) t1.getTerm(), t2);
        } else if (t2.isClass(DateTerm.class)) {
            return apply((DateTerm) t2.getTerm(), t1);
        } else {
            NumericTerm it1;
            NumericTerm it2;
            it1 = TermConverter.toNumericTerm(t1);
            it2 = TermConverter.toNumericTerm(t2);

            double i1;
            double i2;
            i1 = ((Double) it1.getValue()).doubleValue();
            i2 = ((Double) it2.getValue()).doubleValue();

            return new NumericTerm(i1 + i2);
        }
    }
}
