/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/operators/NotEqualsOperator.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.operators;

import org.realtor.rets.validation.terms.InvalidTermException;
import org.realtor.rets.validation.terms.Term;


/**
 *  NotEqualsOperator.java Created Sep 15, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class NotEqualsOperator extends ComparisonOperator {
    public NotEqualsOperator() {
        symbol = "!=";
        setPrecedence(4);
    }

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.operators.ComparisonOperator#doComparison(org.realtor.rets.validation.terms.Term, org.realtor.rets.validation.terms.Term)
     */
    protected boolean doComparison(Term t1, Term t2)
        throws InvalidTermException {
        System.out.println("T1: " + t1 + " t2: " + t2);

        return !t1.getValue().equals(t2.getValue());
    }
}
