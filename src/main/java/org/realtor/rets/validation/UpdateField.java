/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/UpdateField.java,v 1.4 2005/03/30 21:41:53 ekovach Exp $  */
package org.realtor.rets.validation;

import org.apache.xpath.XPathAPI;

import org.realtor.rets.validation.terms.InvalidTermException;
import org.realtor.rets.validation.terms.Term;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;


/**
 * UpdateField.java Created Sep 26, 2003
 * <p>
 * <p>
 * Copyright 2003, Avantia inc.
 *
 * @author scohen
 * @version $Revision: 1.4 $
 */
public class UpdateField implements Comparable {
    private final static Logger logger = LoggerFactory.getLogger(UpdateField.class);
    private ValidationError error;
    private int sequence;
    private int[] attributes;
    private String systemName;
    private String validateHelpID;
    private String validateExternalName;
    private ArrayList validationExpressions;
    private String def;
    private String dataType;

    public UpdateField(Node toBuild) {
        init(toBuild);
    }

    /**
     * @param toBuild
     */
    private void init(Node toBuild) {
        validationExpressions = new ArrayList();

        try {
            systemName = getElementValue(toBuild, "SystemName");
            validateHelpID = getElementValue(toBuild, "ValidateHelpID");
            try {
                sequence = Integer.parseInt(getElementValue(toBuild, "Sequence"));
            } catch (NumberFormatException e) {
                sequence = 0;
            }
            attributes = parseAttributes(getElementValue(toBuild, "Attributes"));
            validateExternalName = getElementValue(toBuild, "ValidateExternal");
            initExpressions(toBuild);
            def = getElementValue(toBuild, "Default");
        } catch (Exception e) {
            logger.error("error: ", e);
        }
    }

    /**
     * @param expression
     * @throws TransformerException
     */
    private void initExpressions(Node expression) throws TransformerException {
        String idList = getElementValue(expression, "ValidationExpressionID");
        if (idList != null) {
            String[] expressionNames = idList.split(",");
            System.out.println("For string " + idList +
                    " the split returned an array of length " + expressionNames.length);

            for (int i = 0; i < expressionNames.length; i++) {
                ValidationExpression current = new ValidationExpression(expressionNames[i]);
                validationExpressions.add(current);
                System.out.println(this.systemName + ".expressions[" + i + "]:" +
                        expressionNames[i]);
            }
        }
    }

    /**
     * @param string
     * @return
     */
    private int[] parseAttributes(String string) {
        int[] rv = null;

        if ((string != null) && (string.trim().length() > 0)) {
            String[] atts = string.split(",");
            rv = new int[atts.length];

            for (int i = 0; i < atts.length; i++) {
                try {
                    rv[i] = Integer.parseInt(atts[i]);
                } catch (NumberFormatException e) {
                    logger.error("error: ", e);
                }
            }
        }

        return rv;
    }

    private String getElementValue(Node toQuery, String nodeName)
            throws TransformerException {
        Node selected = XPathAPI.selectSingleNode(toQuery, nodeName +
                "/text()");

        String value = null;

        if (selected != null) {
            value = selected.getNodeValue().trim();
        }

        return value;
    }

    /**
     * @return
     */
    public int[] getAttributes() {
        return attributes;
    }

    /**
     * @return
     */
    public int getSequence() {
        return sequence;
    }

    /**
     * @return
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * @param is
     */
    public void setAttributes(int[] is) {
        attributes = is;
    }

    /**
     * @param i
     */
    public void setSequence(int i) {
        sequence = i;
    }

    /**
     * @param string
     */
    public void setSystemName(String string) {
        systemName = string;
    }

    /**
     * @return
     */
    public String getDefault() {
        return def;
    }

    /**
     * @return
     */
    public String getValidateExternalName() {
        return validateExternalName;
    }

    /**
     * @return
     */
    public String getValidateHelpID() {
        return validateHelpID;
    }

    /**
     * @param string
     */
    public void setDefault(String string) {
        def = string;
    }

    /**
     * @param string
     */
    public void setValidateExternalName(String string) {
        validateExternalName = string;
    }

    /**
     * @param string
     */
    public void setValidateHelpID(String string) {
        validateHelpID = string;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        UpdateField update = (UpdateField) o;

        return sequence - update.getSequence();
    }

    /**
     * @return
     */
    public List getValidationExpressions() {
        return validationExpressions;
    }

    public void addValidationExpression(ValidationExpression expr) {
        validationExpressions.add(expr);
    }

    /**
     * @param expressions
     */
    public void setValidationExpressions(List expressions) {
        validationExpressions.clear();
        validationExpressions.addAll(expressions);
    }

    public void validate(Map symbols) {
        ValidationExpressionEvaluator evaluator = new ValidationExpressionEvaluator();
        evaluator.getTermFactory().setSymbolTable(symbols);
        evaluator.getTermFactory().setEntry(getSystemName());

        boolean accepted = false;
        System.out.println("Validating field " + getSystemName() +
                " there are " + validationExpressions.size() + " expressions");

        for (Iterator iter = validationExpressions.iterator(); iter.hasNext(); ) {
            ValidationExpression expression = (ValidationExpression) iter.next();
            String type = expression.getType();
            String exprText = expression.getExpression();
            String toSet = "";
            Term result;
            Boolean TRUE = new Boolean(true);
            System.out.println("Executing " + exprText);

            if ("SET".equals(type)) {
                int equalsPos = exprText.indexOf("=");

                if (equalsPos < 0) {
                    System.out.println("Couldn't find an equals in expression " +
                            exprText);

                    continue;
                } else {
                    exprText = exprText.substring(equalsPos + 1);
                    toSet = exprText.substring(0, equalsPos);
                }
            } else if (accepted) {
                // if it's a set, we'll execute, otherwise continue to the next one.
                continue;
            }

            try {
                result = evaluator.eval(exprText);

                if (type.equals("SET")) {
                    evaluator.getTermFactory().setSymbol(toSet.trim(), result);
                } else if (TRUE.equals(result.getValue()) &&
                        type.equals("REJECT")) {
                    error = new ValidationError(getSystemName(),
                            " failed validation because " +
                                    expression.getExpression());

                    break;
                } else if (TRUE.equals(result.getValue()) &&
                        type.equals("ACCEPT")) {
                    // only process SET
                    accepted = true;
                }
            } catch (InvalidTermException e) {
                // TODO Auto-generated catch block
                logger.error("error: ", e);
                error = new ValidationError(getSystemName(),
                        " failed because " + e.getMessage());
            }
        }
    }

    /**
     * @return
     */
    public ValidationError getError() {
        return error;
    }
}
