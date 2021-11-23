/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/operators/LessThanOrEqualToOperator.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.operators;

import org.realtor.rets.validation.terms.InvalidTermException;
import org.realtor.rets.validation.terms.NumericTerm;
import org.realtor.rets.validation.terms.StringTerm;
import org.realtor.rets.validation.terms.Term;
import org.realtor.rets.validation.terms.TermConverter;


/**
 *  LessThanOrEqualToOperator.java Created Sep 11, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class LessThanOrEqualToOperator extends ComparisonOperator {
    public LessThanOrEqualToOperator() {
        symbol = "<=";
        setPrecedence(3);
    }

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.operators.ComparisonOperator#doComparison(org.realtor.rets.validation.terms.Term, org.realtor.rets.validation.terms.Term)
     */
    protected boolean doComparison(Term t1, Term t2)
        throws InvalidTermException {
        double i1;
        double i2;

        if (t1.isClass(StringTerm.class)) {
            StringTerm st1;
            StringTerm st2;
            st1 = (StringTerm) t1;
            st2 = TermConverter.toStringTerm(t2);

            String s1;
            String s2;
            s1 = (String) st1.getValue();
            s2 = (String) st2.getValue();

            return s1.compareTo(s2) <= 0;
        } else {
            NumericTerm it1;
            NumericTerm it2;
            it1 = TermConverter.toNumericTerm(t1.getTerm());
            it2 = TermConverter.toNumericTerm(t2.getTerm());
            i1 = ((Double) it1.getValue()).doubleValue();
            i2 = ((Double) it2.getValue()).doubleValue();
        }

        return i1 <= i2;
    }
}
