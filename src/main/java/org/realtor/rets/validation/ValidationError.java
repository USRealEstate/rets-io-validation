/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/ValidationError.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation;


/**
 *  ValidationError.java Created Oct 3, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class ValidationError {
    private String fieldName;
    private String errorMessage;

    public ValidationError() {
    }

    public ValidationError(String fieldName, String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }

    /**
     * @return
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @return
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @param string
     */
    public void setErrorMessage(String string) {
        errorMessage = string;
    }

    /**
     * @param string
     */
    public void setFieldName(String string) {
        fieldName = string;
    }

    public String toString() {
        return fieldName + " " + errorMessage;
    }
}
