/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/terms/BooleanTerm.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.terms;


/**
 *  BooleanTerm.java Created Aug 25, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class BooleanTerm extends AbstractTerm {
    public BooleanTerm(boolean b) {
        value = new Boolean(b);
    }

    public boolean equals(Object another) {
        if (another instanceof BooleanTerm) {
            BooleanTerm otherBool = (BooleanTerm) another;
            Boolean otherValue = (Boolean) otherBool.getValue();

            return (value.equals(otherValue));
        } else {
            return false;
        }
    }
}
