/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/operators/Operator.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.operators;

import org.realtor.rets.validation.terms.*;


/**
 *  Operator.java Created Aug 22, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public abstract class Operator {
    public static int MIN_PRECEDENCE = Integer.MIN_VALUE;
    protected Object leftSide;
    protected Object rightSide;
    protected boolean comparable = false;
    protected String symbol;
    protected int precedence;

    public abstract Term apply(Term t1, Term t2) throws InvalidTermException;

    public boolean isComparable() {
        return comparable;
    }

    public String getSymbol() {
        return symbol;
    }

    public String toString() {
        String className = getClass().getName();

        return className.substring(className.lastIndexOf(".") + 1);
    }

    /**
     * @return
     */
    public int getPrecedence() {
        return precedence;
    }

    /**
     * @param i
     */
    public void setPrecedence(int i) {
        precedence = 0 - i;
    }

    public boolean equals(Object another) {
        if (another != null) {
            return another.getClass().equals(this.getClass());
        } else {
            return false;
        }
    }
}
