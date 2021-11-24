/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/ValidationExpression.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation;


/**
 *  ValidationExpression.java Created Oct 3, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class ValidationExpression {
    private String expression;
    private String type;
    private String id;

    public ValidationExpression(String id) {
        this.id = id;
    }

    public ValidationExpression(String type, String id) {
        this.type = type;
        this.id = id;
    }

    /**
     * @return
     */
    public String getExpression() {
        return expression;
    }

    /**
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * @param string
     */
    public void setExpression(String string) {
        expression = string;
    }

    /**
     * @param string
     */
    public void setType(String string) {
        type = string;
    }

    /**
     * @return
     */
    public String getID() {
        return id;
    }

    /**
     * @param string
     */
    public void setID(String string) {
        id = string;
    }
}
