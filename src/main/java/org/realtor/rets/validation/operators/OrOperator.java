/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/operators/OrOperator.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.operators;

import org.realtor.rets.validation.terms.*;


/**
 *  OrOperator.java Created Aug 25, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class OrOperator extends Operator implements LogicOperator {
    public OrOperator() {
        super();
        setPrecedence(6);
        symbol = ".OR.";
    }

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.operators.Operator#apply(org.realtor.rets.validation.operators.Term, org.realtor.rets.validation.operators.Term)
     */
    public Term apply(Term t1, Term t2) {
        if (t1.isClass(BooleanTerm.class) && t2.isClass(BooleanTerm.class)) {
            BooleanTerm b1;
            BooleanTerm b2;
            b1 = (BooleanTerm) t1;
            b2 = (BooleanTerm) t2;

            boolean bool1;
            boolean bool2;
            bool1 = ((Boolean) b1.getValue()).booleanValue();
            bool2 = ((Boolean) b2.getValue()).booleanValue();

            return new BooleanTerm(bool1 || bool2);
        } else {
            System.out.println(
                "One of the terms in OR is not a boolean term t1:" +
                t1.getClass().getName() + " t2: " + t2.getClass().getName());

            return null;
        }
    }
}
