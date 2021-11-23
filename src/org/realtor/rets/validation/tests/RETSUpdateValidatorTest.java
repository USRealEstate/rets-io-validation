/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/tests/RETSUpdateValidatorTest.java,v 1.1.1.1 2003/11/21 16:20:05 rsegelman Exp $  */
package org.realtor.rets.validation.tests;

import junit.framework.TestCase;

import org.realtor.rets.util.XMLUtils;
import org.realtor.rets.validation.RETSUpdateValidator;
import org.realtor.rets.validation.UpdateField;
import org.realtor.rets.validation.ValidationExpressionEvaluator;
import org.realtor.rets.validation.terms.InvalidTermException;
import org.realtor.rets.validation.terms.StringTerm;
import org.realtor.rets.validation.terms.Term;

import org.w3c.dom.Document;

import java.io.FileInputStream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *  RETSUpdateValidatorTest.java Created Sep 26, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.1.1.1 $
 *  @author scohen
 */
public class RETSUpdateValidatorTest extends TestCase {
    private static boolean setUp = false;
    private static Document doc;
    private static List list;
    private static RETSUpdateValidator validator;
    private String xmlFile = "/home/scohen/metadataSample2.xml";
    private ValidationExpressionEvaluator evaluator;

    /**
     *
     */
    public RETSUpdateValidatorTest() {
        evaluator = new ValidationExpressionEvaluator();

        // TODO Auto-generated constructor stub
    }

    public void setUp() {
        if (setUp) {
            return;
        }

        try {
            validator = new RETSUpdateValidator();

            FileInputStream fis = new FileInputStream("/tmp/retsmetadata.xml");
            StringBuffer xmlDoc = new StringBuffer();
            int read;

            while ((read = fis.read()) >= 0) {
                xmlDoc.append((char) read);
            }

            Document doc = XMLUtils.stringToDocument(xmlDoc.toString());
            System.out.println("Doc: " + doc);

            // set up Symbol table
            Map table = new HashMap();
            table.put(".ENTRY.", new StringTerm("Test"));
            table.put(".EMPTY.", new StringTerm(""));
            validator.setSymbolTable(table);
            validator.setMetaData(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testEmpty() {
        validator.validate();
    }

    /*
        public void testValidationID() {
            UpdateField yadda = (UpdateField) list.get(4);
            System.out.println("ID is " + yadda.getValidationExpressionIDs());
            assertEquals("Should be 551", "551", yadda.getValidationExpressionIDs());

            yadda = (UpdateField) list.get(5);
            System.out.println("ID is " + yadda.getValidationExpressionIDs());
            assertEquals("Should be 561", "561", yadda.getValidationExpressionIDs());
        }

        public void testValidationExpression() {
            UpdateField field = (UpdateField) list.get(4);
            assertNotNull(field.getValidationExpressions());
            assertEquals(
                field.getValidationExpressions()[0],
                "(List_Agent=.AGENTCODE. .OR. .USERCLASS. > 2) .AND. .USERLEVEL. > 2");

            field = (UpdateField) list.get(5);
            assertNotNull(field.getValidationExpressions());
            assertEquals(
                field.getValidationExpressions()[0],
                "(List_Office=.BROKERBRANCH. .OR. .USERCLASS. > 2) .AND. .USERLEVEL. > 2");
        }
        */
    public void testEntry() throws InvalidTermException {
        Term t = evaluator.eval(".ENTRY. = .EMPTY.");
        assertEquals(new Boolean(false), t.getValue());
    }
}
