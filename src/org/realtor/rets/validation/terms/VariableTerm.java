/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/terms/VariableTerm.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.terms;

import java.util.*;


/**
 *  VariableTerm.java Created Sep 29, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class VariableTerm extends AbstractTerm {
    private static final String EMPTY = ".EMPTY.";
    private String variableName;
    private TermFactory factory;

    protected VariableTerm(String name) {
        variableName = name;
    }

    public void setTermFactory(TermFactory f) {
        factory = f;
    }

    public void setSymbol(String symbolName, Term value) {
        factory.getSymbolTable().put(symbolName, value);
    }

    public void setEntry(String termName) {
        VariableTerm entry = factory.newVariableTerm(termName);
        System.out.println("Setting entry to " + termName + "::" + entry);
        entry.setValue(factory.getSymbolTable().get(termName));

        if (entry.getValue() == null) {
            System.out.println("Couldn't find " + termName +
                " in symbol factory.getSymbolTable().");
        }

        factory.getSymbolTable().put(".ENTRY.", entry);
    }

    public void setVariableName(String varName) {
        variableName = varName;
    }

    public String getVariableName() {
        return variableName;
    }

    /* (non-Javadoc)
     * @see org.realtor.rets.validation.terms.Term#getValue()
     */
    public Object getValue() {
        Object test = factory.getSymbolTable().get(variableName);

        if (test == null) {
            System.out.println("Couldn't find symbol " + variableName);
        }

        Term rv = (Term) test;

        if (rv == null) {
            rv = (Term) factory.getSymbolTable().get(EMPTY);
        }

        return rv.getValue();
    }

    public boolean isClass(Class toTest) {
        System.out.println("isClass: toTest is " + toTest + " variable is " +
            variableName);

        Object binding = factory.getSymbolTable().get(variableName);

        if (binding == null) {
            // the variable was not found.
            binding = factory.getSymbolTable().get(EMPTY);
        }

        return binding.getClass().equals(toTest);
    }

    public Term getTerm() {
        return (Term) factory.getSymbolTable().get(variableName);
    }
}
