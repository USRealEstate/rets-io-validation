/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/operators/EqualsOperator.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.operators;

import org.realtor.rets.validation.terms.*;


/**
 *  EqualsOperator.java Created Aug 25, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class EqualsOperator extends ComparisonOperator {
    public EqualsOperator() {
        super();
        symbol = "=";
        setPrecedence(4);
    }

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.operators.ComparisonOperator#doComparison(org.realtor.rets.validation.terms.Term, org.realtor.rets.validation.terms.Term)
     */
    protected boolean doComparison(Term t1, Term t2)
        throws InvalidTermException {
        boolean ret = false;
        ret = t1.getValue().equals(t2.getValue());

        return ret;
    }
}
