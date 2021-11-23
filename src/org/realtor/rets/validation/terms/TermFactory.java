/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/terms/TermFactory.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation.terms;

import java.util.*;


/**
 *  Factory object for creating VariableTerms. This object houses the Symbol Table so
 *  all the variable terms in a thread can share the resource while allowing other threads to
 *  have their own symbol table.
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class TermFactory {
    private Map symbolTable;

    public TermFactory() {
        symbolTable = new HashMap();
    }

    /**
     *  Returns the symbol table for this factory
     * @return a map of name -> term mappings
     */
    public Map getSymbolTable() {
        return symbolTable;
    }

    /**
     *  Instantiates a new variable term with the specified name.
     * @param varName The name of the variable
     * @return a newly instantiated variable term
     */
    public VariableTerm newVariableTerm(String varName) {
        VariableTerm vt = new VariableTerm(varName);
        vt.setTermFactory(this);

        return vt;
    }

    /**
     * Sets the symbol table for this factory. Also creates the .EMPTY. variable if none exists.
     * @param toSet the map to use as the Symbol Table. It must contain name-> Term mappings.
     */
    public void setSymbolTable(Map toSet) {
        symbolTable = toSet;

        if (symbolTable.get(".EMPTY.") == null) {
            symbolTable.put(".EMPTY.", new StringTerm(""));
        }
    }

    /**
     *  Sets a symbol in the symbol table
     * @param name the name of the symbol
     * @param value a term representing the symbol's value
     */
    public void setSymbol(String name, Term value) {
        symbolTable.put(name, value);
    }

    /**
     *  This method designates the symbol with the name passed in as the current entry
     *  in the validation language.
     * @param termName The name of the variable to use as the entry.
     */
    public void setEntry(String termName) {
        VariableTerm entry = newVariableTerm(termName);
        entry.setValue(symbolTable.get(termName));

        if (entry.getValue() == null) {
            symbolTable.put(".ENTRY.", symbolTable.get(".EMPTY."));
        } else {
            symbolTable.put(".ENTRY.", entry);
        }
    }
}
