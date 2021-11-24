/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/operators/ContainsOperator.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.operators;

import org.realtor.rets.validation.terms.BooleanTerm;
import org.realtor.rets.validation.terms.InvalidTermException;
import org.realtor.rets.validation.terms.StringTerm;
import org.realtor.rets.validation.terms.Term;
import org.realtor.rets.validation.terms.TermConverter;


/**
 *  ContainsOperator.java Created Sep 15, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class ContainsOperator extends Operator {
    public ContainsOperator() {
        super();
        symbol = ".CONTAINS.";
        setPrecedence(2);
    }

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.operators.Operator#apply(org.realtor.rets.validation.terms.Term, org.realtor.rets.validation.terms.Term)
     */
    public Term apply(Term t1, Term t2) throws InvalidTermException {
        StringTerm st1;
        StringTerm st2;
        st1 = TermConverter.toStringTerm(t1);
        st2 = TermConverter.toStringTerm(t2);

        String s1;
        String s2;
        s1 = (String) st1.getValue();
        s2 = (String) st2.getValue();

        return new BooleanTerm(s1.indexOf(s2) >= 0);
    }
}
