/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/terms/AbstractTerm.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.terms;


/**
 *  AbstractTerm.java Created Sep 29, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public abstract class AbstractTerm implements Term {
    protected Class type = this.getClass();
    protected Object value;

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.terms.Term#getValue()
     */
    public Object getValue() {
        return value;
    }

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.terms.Term#setValue(java.lang.Object)
     */
    public void setValue(Object val) {
        value = val;
    }

    public boolean isClass(Class toTest) {
        return type.equals(toTest);
    }

    public String toString() {
        return type.getName() + "-->" + value;
    }

    public Class getType() {
        return type;
    }

    public Term getTerm() {
        return this;
    }
}
