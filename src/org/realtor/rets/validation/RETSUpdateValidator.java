/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/RETSUpdateValidator.java,v 1.4 2005/03/30 21:41:53 ekovach Exp $  */
package org.realtor.rets.validation;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Category;
import org.apache.xpath.XPathAPI;
import org.realtor.rets.util.XMLUtils;
import org.realtor.rets.validation.terms.AbstractTerm;
import org.realtor.rets.validation.terms.DateTerm;
import org.realtor.rets.validation.terms.NumericTerm;
import org.realtor.rets.validation.terms.StringTerm;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 *  RETSUpdateValidator.java Created Sep 26, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.4 $
 *  @author scohen
 */
public class RETSUpdateValidator {
    // log4j category
    static Category cat = Category.getInstance(RETSUpdateValidator.class);
    protected static final String AGENT_CODE_KEY = "AGENT-CODE";
    protected static final String USER_CLASS_KEY = "USER-CLASS";
    protected static final String USER_LEVEL_KEY = "USER-LEVEL";
    protected static final String USER_ID_KEY = "USER-ID";
    private Document metaData;
    private Hashtable validationExpressions;
    private String updateResource;
    private String updateClass;
    private String updateType;
    private Map oldValues;
    private Map newValues;
    private List fieldList = null;
    private List errors;
    private Map userInfo;
    private Map symbolTable;

    /**
        A HashMap of field types to Term class types.
     */
    private static HashMap termTypeMap = new HashMap();
    
    static {
        // initialize the termTypeMap
        termTypeMap.put("Date", DateTerm.class);
        termTypeMap.put("Int", NumericTerm.class);
        termTypeMap.put("Decimal", NumericTerm.class);
        termTypeMap.put("Small", NumericTerm.class);
        termTypeMap.put("Long", NumericTerm.class);
        termTypeMap.put("Tiny", NumericTerm.class);
    }
    
    public RETSUpdateValidator() {
        validationExpressions = new Hashtable();
        userInfo = new Hashtable();
    }

    public RETSUpdateValidator(String resource,
        String className, String type) {
        this();
        updateResource = resource;
        updateClass = className;
        updateType = type;
    }

    public List getUpdateFieldList() {
        return fieldList;
    }

    public boolean hasErrors() {
        return (errors != null) && (errors.size() > 0);
    }

    public void validate() {
        ValidationExpressionEvaluator evaluator = new ValidationExpressionEvaluator();
        Iterator iter = fieldList.iterator();
        errors = new ArrayList();

        Boolean correct = new Boolean(true);

        while (iter.hasNext()) {
            UpdateField field = (UpdateField) iter.next();
            field.validate(symbolTable);

            if (field.getError() != null) {
                errors.add(field.getError());
            }
        }
    }
    
    public void setSymbolTable(Map table) {
        // call parseEntries first because there aren't names like .TODAY. in the metadata.
        Map symbols = parseEntries(table);
        symbols.put(".TODAY.", new DateTerm());
        symbols.put(".NOW.", new DateTerm(new Date()));
        symbols.put(".EMPTY.", new StringTerm(""));
        symbols.put(".USERCLASS.", userInfo.get(USER_CLASS_KEY));
        symbols.put(".USERLEVEL.", userInfo.get(USER_LEVEL_KEY));
        symbols.put(".USERID.", userInfo.get(USER_ID_KEY));
        symbols.put(".AGENTCODE.", userInfo.get(AGENT_CODE_KEY));
        symbolTable = symbols;
    }

    private Map parseEntries(Map table) {
        HashMap rv = new HashMap();
        RetsTokenParser parser = new RetsTokenParser();

        for (Iterator iter = table.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            Object value = table.get(key);
            String entry = null;
            String fieldType = getFieldTypeBySystemName(key);

            if (value instanceof String) {
                entry = (String) value;
            } else if (value instanceof String[]) {
                entry = ((String[]) value)[0];
            }

            if ((entry != null) && (entry.length() > 0)) {
                // get the term type class for the field type; assume StringTerm
                // if it's not specifically mapped
                Class termTypeClass = (Class) termTypeMap.get(fieldType);
                if (termTypeClass == null) {
                    termTypeClass = StringTerm.class;
                }
                try {
                    Constructor constructor = termTypeClass.getConstructor(new Class [] {String.class});
                    AbstractTerm term = (AbstractTerm) constructor.newInstance(new Object [] {entry});
                    rv.put(key, term);
                    cat.debug("Added value: " + key + " = " + term);
                }
                catch (Exception e) {
                    cat.error(e);
                }
            }
        }

        return rv;
    }

    /**
     * Get the field type from the system name.
     * @param systemName
     * @return The field type from the system name.
     */
    private String getFieldTypeBySystemName(String systemName) {
        String fieldType = null;
        String query = "//METADATA-RESOURCE/Resource[ResourceID=\"" +
            updateResource + "\"]/METADATA-CLASS/Class[ClassName=\"" +
            updateClass + "\"]/METADATA-TABLE[@Resource=\"" + updateResource +
            "\" and @Class=\"" + updateClass + "\"]/Field[SystemName=\"" +
            systemName + "\"]/DataType";

        try {
            Node dataType = XPathAPI.selectSingleNode(metaData.getFirstChild(),
                    query);

            if (dataType != null) {
                fieldType = dataType.getFirstChild().getNodeValue();
                cat.debug("SystemName [" + systemName + "] type [" + fieldType + "]");
            }
        } catch (DOMException e) {
            // TODO Auto-generated catch block
            cat.error("error: ", e);
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            cat.error("error: ", e);
        }

        return fieldType;
    }

    /**
     *  Sets the user information. This takes a comma delimited string
     *  of the type set by the login transaction. This string takes the following form
     *  user-id, user-level, user-class, agent-code.
     * @param info
     */
    public void setUserInfo(String info) {
        String[] infoArr = info.split(",");
        setUserInfoField(USER_ID_KEY, infoArr[0]);
        setUserInfoField(USER_LEVEL_KEY, infoArr[1]);
        setUserInfoField(USER_CLASS_KEY, infoArr[2]);
        setUserInfoField(AGENT_CODE_KEY, infoArr[3]);
    }

    private void setUserInfoField(String fieldName, String fieldValue) {
        if ("NULL".equals(fieldValue)) {
            fieldValue = "";
        }

        userInfo.put(fieldName, fieldValue);
    }

    private void getFields() {
        try {
            String query = "//METADATA-RESOURCE/Resource[ResourceID=\"" +
                updateResource + "\"]/METADATA-CLASS/Class[ClassName=\"" +
                updateClass + "\"]/METADATA-UPDATE/UpdateType[UpdateName=\"" +
                updateType + "\"]/METADATA-UPDATE_TYPE[@Resource=\"" +
                updateResource + "\" and @Class=\"" + updateClass +
                "\" and @Update=\"" + updateType + "\"]/UpdateField";

            NodeList updateFields = XMLUtils.executeXpathQuery(metaData.getFirstChild(),
                    query);
            fieldList = new ArrayList(updateFields.getLength());

            for (int i = 0; i < updateFields.getLength(); i++) {
                cat.debug("Update field# :" + i + "=" + updateFields.item(i));

                UpdateField uf = new UpdateField(updateFields.item(i));
                populateValidationExpression(uf, metaData.getFirstChild());
                fieldList.add(uf);
            }

            // this sorts the update fields by sequence number.
            Collections.sort(fieldList);
        } catch (TransformerException e) {
            cat.error("error: ", e);
        }
    }

    /**
     * @param uf
     * @param validation
     */
    private void populateValidationExpression(UpdateField uf, Node root) {
        if (uf.getValidationExpressions() != null) {
            List expressions = uf.getValidationExpressions();

            for (int j = 0; j < expressions.size(); j++) {
                ValidationExpression expr = (ValidationExpression) expressions.get(j);
                String query = "//METADATA-RESOURCE/Resource[ResourceID=\"" +
                    updateResource +
                    "\"]/METADATA-VALIDATION_EXPRESSION[@Resource=\"" +
                    updateResource +
                    "\"]/ValidationExpression[ValidationExpressionID=\"" +
                    expr.getID() + "\"]";

                try {
                    Node validation = XPathAPI.selectSingleNode(root, query);

                    if (validation != null) {
                        NodeList subnodes = validation.getChildNodes();
                        String expressionType = null;
                        String expression = null;

                        for (int i = 0; i < subnodes.getLength(); i++) {
                            Node current = subnodes.item(i);
                            String nodeName = current.getNodeName();

                            if ("ValidationExpressionType".equals(nodeName)) {
                                expressionType = current.getChildNodes().item(0)
                                                        .getNodeValue();
                            } else if ("Value".equals(nodeName)) {
                                expression = current.getChildNodes().item(0)
                                                    .getNodeValue();
                            }
                        }

                        expr.setExpression(expression);
                        expr.setType(expressionType);
                    }
                } catch (DOMException e) {
                    // TODO Auto-generated catch block
                    cat.error("error: ", e);
                } catch (TransformerException e) {
                    // TODO Auto-generated catch block
                    cat.error("error: ", e);
                }

                //expressions.add(expr);
            }

            //uf.setValidationExpressions(expressions);
        }
    }

    /**
     * @return
     */
    public Document getMetaData() {
        return metaData;
    }

    /**
     * @param document
     */
    public void setMetaData(Document document) {
        metaData = document;
        getFields();
    }

    public void setMetaData(String xml) {
        Document doc = XMLUtils.stringToDocument(xml);
        setMetaData(doc);
    }

    /**
     * @return
     */
    public List getErrors() {
        return errors;
    }

    /**
     * @param list
     */
    public void setErrors(List list) {
        errors = list;
    }
}
