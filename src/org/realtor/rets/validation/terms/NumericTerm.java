/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/terms/NumericTerm.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.terms;


/**
 *  IntTerm.java Created Aug 25, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class NumericTerm extends AbstractTerm {
    public NumericTerm(String s) {
        value = new Double(s);
    }

    public NumericTerm(char c) {
    }

    public NumericTerm(int i) {
        value = new Double(i);
    }

    public NumericTerm(double d) {
        value = new Double(d);
    }

    public NumericTerm(long i) {
        value = new Double(i);
    }

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.operators.Term#setValue(java.lang.Object)
     */
    public void setValue(Object val) {
        if (val instanceof Double) {
            value = val;
        }
    }
}
