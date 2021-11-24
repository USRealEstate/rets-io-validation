/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/operators/MultiplicationOperator.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.operators;

import org.realtor.rets.validation.terms.InvalidTermException;
import org.realtor.rets.validation.terms.NumericTerm;
import org.realtor.rets.validation.terms.Term;
import org.realtor.rets.validation.terms.TermConverter;


/**
 *  MultiplicationOperator.java Created Sep 3, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class MultiplicationOperator extends Operator {
    public MultiplicationOperator() {
        super();
        setPrecedence(1);
        symbol = "*";
    }

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.operators.Operator#apply(org.realtor.rets.validation.terms.Term, org.realtor.rets.validation.terms.Term)
     */
    public Term apply(Term t1, Term t2) throws InvalidTermException {
        return apply(TermConverter.toNumericTerm(t1),
            TermConverter.toNumericTerm(t2));
    }

    public Term apply(NumericTerm t1, NumericTerm t2) {
        Double i1;
        Double i2;
        i1 = (Double) t1.getValue();
        i2 = (Double) t2.getValue();

        return new NumericTerm(i1.doubleValue() * i2.doubleValue());
    }
}
