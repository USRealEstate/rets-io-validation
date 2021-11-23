/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/operators/LessThanOperator.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.operators;

import org.realtor.rets.validation.terms.*;


/**
 *  LessThanOperator.java Created Aug 25, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class LessThanOperator extends ComparisonOperator
    implements MathematicOperator {
    public LessThanOperator() {
        symbol = "<";
        comparable = true;
        setPrecedence(3);
    }

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.operators.ComparisonOperator#doComparison(org.realtor.rets.validation.operators.Term, org.realtor.rets.validation.operators.Term)
     */
    protected boolean doComparison(Term t1, Term t2)
        throws InvalidTermException {
        if (t1.isClass(StringTerm.class)) {
            StringTerm st1;
            StringTerm st2;
            st1 = (StringTerm) t1;
            st2 = TermConverter.toStringTerm(t2);

            String s1;
            String s2;
            s1 = (String) st1.getValue();
            s2 = (String) st2.getValue();

            return s1.compareTo(s2) > 0;
        } else {
            // most other terms can be converted to int.
            NumericTerm it1;

            // most other terms can be converted to int.
            NumericTerm it2;
            double i1;
            double i2;

            it1 = TermConverter.toNumericTerm(t1.getTerm());
            it2 = TermConverter.toNumericTerm(t2.getTerm());
            i1 = ((Double) it1.getValue()).doubleValue();
            i2 = ((Double) it2.getValue()).doubleValue();

            return i1 < i2;
        }
    }
}
