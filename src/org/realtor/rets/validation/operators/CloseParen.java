/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/operators/CloseParen.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.operators;

import org.realtor.rets.validation.terms.InvalidTermException;
import org.realtor.rets.validation.terms.Term;


/**
 *  CloseParen.java Created Sep 3, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class CloseParen extends Operator implements Parenthetical {
    public CloseParen() {
        super();
        symbol = ")";
        setPrecedence(0);
    }

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.operators.Operator#apply(org.realtor.rets.validation.terms.Term, org.realtor.rets.validation.terms.Term)
     */
    public Term apply(Term t1, Term t2) throws InvalidTermException {
        // TODO Auto-generated method stub
        return null;
    }
}
