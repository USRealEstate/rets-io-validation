/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/operators/NotOperator.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.operators;

import org.realtor.rets.validation.terms.*;
import org.realtor.rets.validation.terms.InvalidTermException;


/**
 *  NotOperator.java Created Sep 17, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class NotOperator extends Operator {
    public NotOperator() {
        setPrecedence(7);
        symbol = ".NOT.";
    }

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.operators.Operator#apply(org.realtor.rets.validation.terms.Term, org.realtor.rets.validation.terms.Term)
     */
    public Term apply(Term t1, Term t2) throws InvalidTermException {
        throw new InvalidTermException(
            "This is a unary operator, it is not applicable for " + t1 + "," +
            t2);
    }

    public Term apply(Term t1) throws InvalidTermException {
        if (!(t1 instanceof BooleanTerm)) {
            String className = t1.getClass().getName();
            className = className.substring(className.lastIndexOf(".") + 1);
            throw new InvalidTermException(
                "Expected a boolean expression but found a " + className +
                " while processing .NOT.");
        }

        Boolean orig = (Boolean) t1.getValue();

        return new BooleanTerm(!orig.booleanValue());
    }
}
