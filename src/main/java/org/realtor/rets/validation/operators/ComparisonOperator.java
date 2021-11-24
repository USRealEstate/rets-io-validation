/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/operators/ComparisonOperator.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.operators;

import org.realtor.rets.validation.terms.*;


/**
 *  ComparisonOperator.java Created Aug 25, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public abstract class ComparisonOperator extends Operator {
    public ComparisonOperator() {
        this.setPrecedence(3);
    }

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.operators.Operator#apply(org.realtor.rets.validation.operators.Term, org.realtor.rets.validation.operators.Term)
     */
    public Term apply(Term t1, Term t2) throws InvalidTermException {
        return new BooleanTerm(doComparison(t1, t2));
    }

    protected abstract boolean doComparison(Term t1, Term t2)
        throws InvalidTermException;
}
