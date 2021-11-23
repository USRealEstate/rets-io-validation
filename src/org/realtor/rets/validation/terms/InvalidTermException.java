/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/terms/InvalidTermException.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.terms;


/**
 *  InvalidTermException.java Created Aug 25, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class InvalidTermException extends Exception {
    public InvalidTermException(String message) {
        super(message);
    }
}
