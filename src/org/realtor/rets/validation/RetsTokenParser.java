/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/RetsTokenParser.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation;

import org.realtor.rets.validation.operators.*;
import org.realtor.rets.validation.terms.*;

import java.io.IOException;
import java.io.StringReader;

import java.text.ParseException;

import java.util.Map;
import java.util.StringTokenizer;


/**
 *  RetsValidationTokenizer.java Created Sep 10, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class RetsTokenParser {
    private StringReader expression;
    private StringTokenizer st;
    private String lookAheadToken = null;
    private String singleOperators = "+-*/><=()!";
    private TermFactory termFactory;

    public RetsTokenParser() {
    }

    public RetsTokenParser(String expr) {
        this();
        setExpression(expr);
    }

    /**
     * @param expr
     */
    public void setExpression(String expr) {
        expression = new StringReader(expr);
    }

    public Object nextToken() {
        Object token = null;
        int nextChar;

        try {
            if ((nextChar = expression.read()) >= 0) {
                char next = (char) nextChar;

                while (Character.isWhitespace(next)) {
                    nextChar = expression.read();

                    if (nextChar < 0) {
                        return null;
                    }

                    next = (char) nextChar;
                }

                if (singleOperators.indexOf(next) >= 0) {
                    token = getNextOperator(next);
                } else if (next == '.') {
                    token = getDottedToken();
                } else if (next == '"') {
                    token = readString();
                } else if (next == 'P') {
                    // could be a date interval
                    token = readDateInterval();

                    if (token == null) {
                        token = readVariableTerm(next);
                    }
                } else if (Character.isDigit(next)) {
                    // first see if it's a date. If it isn't, treat it like a number
                    token = readDate(next);

                    if (token == null) {
                        token = readNumeric(next);
                    }
                } else {
                    // it must be a system name.
                    //token = new 
                    token = readVariableTerm(next);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return token;
    }

    private Term readVariableTerm(char start)
        throws IOException, InvalidTermException {
        StringBuffer sb = new StringBuffer();
        sb.append(start);
        expression.mark(1);

        int nextChar;

        while ((nextChar = expression.read()) >= 0) {
            char next = (char) nextChar;

            if ((next == ' ') || (singleOperators.indexOf(next) > 0)) {
                break;
            } else {
                sb.append(next);
            }
        }

        VariableTerm vt = termFactory.newVariableTerm(sb.toString());

        return (Term) vt;
    }

    private DateIntervalTerm readDateInterval() throws IOException {
        DateIntervalTerm interval = null;
        StringBuffer sb = new StringBuffer("P");
        int nextChar;
        expression.mark(1);

        while ((nextChar = expression.read()) >= 0) {
            char next = (char) nextChar;

            if (Character.isLetterOrDigit(next)) {
                sb.append(next);
            } else {
                break;
            }
        }

        try {
            interval = new DateIntervalTerm(sb.toString());
        } catch (ParseException e) {
            expression.reset();
        }

        return interval;
    }

    private StringTerm readString() throws IOException {
        StringTerm term;
        String quoted = readThrough('"');
        term = new StringTerm(quoted.substring(0, quoted.length() - 1));

        return term;
    }

    private DateTerm readDate(char start) throws IOException {
        StringBuffer sb = new StringBuffer();
        DateTerm rv = null;
        expression.mark(1);

        int next;
        char nextChar;

        char[] dateChars = new char[10];
        dateChars[0] = start;

        int numread = expression.read(dateChars, 1, 9);

        if (numread != 9) {
            expression.reset();
        } else {
            String dateStr = new String(dateChars);

            if (dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                try {
                    rv = new DateTerm(dateStr);
                } catch (ParseException e) {
                    // since we've validated with a regex, this parse exception can't really happen.
                    e.printStackTrace();
                }
            } else {
                expression.reset();
            }
        }

        return rv;
    }

    private NumericTerm readNumeric(char start) throws IOException {
        NumericTerm rv = null;
        StringBuffer sb = new StringBuffer();
        sb.append(start);

        int next;
        char nextChar;
        expression.mark(1);

        while ((next = expression.read()) >= 0) {
            nextChar = (char) next;

            if (Character.isDigit(nextChar)) {
                sb.append(nextChar);
                expression.mark(1);
            } else {
                expression.reset();

                break;
            }
        }

        try {
            rv = new NumericTerm(Double.parseDouble(sb.toString()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return rv;
    }

    private Object getDottedToken() throws IOException {
        Object token = null;

        String tokenString = "." + readThrough('.');
        token = OperatorMap.map(tokenString);

        if (token == null) {
            // then it's one of those 'always there' variables
            VariableTerm vt = termFactory.newVariableTerm(tokenString);
            token = vt;
        }

        return token;
    }

    private Operator getNextOperator(char current) throws IOException {
        expression.mark(2);

        char nextChar = (char) expression.read();
        char[] operStr = new char[2];
        operStr[0] = current;

        String operator;

        if (nextChar == '=') {
            operStr[1] = nextChar;
            operator = new String(operStr, 0, 2);
        } else {
            expression.reset();
            operator = new String(operStr, 0, 1);
        }

        return OperatorMap.map(operator);
    }

    private String readThrough(char stopChar) throws IOException {
        StringBuffer sb = new StringBuffer();
        int next;

        while ((next = expression.read()) >= 0) {
            char nextChar = (char) next;
            sb.append(nextChar);

            if (nextChar == stopChar) {
                break;
            }
        }

        return sb.toString();
    }

    public Term matchTerm(String token) {
        if (token.length() == 10) {
            // possible date
            try {
                return new DateTerm(token);
            } catch (ParseException e) {
            }
        }

        try {
            return new NumericTerm(Long.parseLong(token));
        } catch (NumberFormatException nfe) {
        }

        return new StringTerm(token);
    }

    /**
     * @return
     */
    public TermFactory getTermFactory() {
        return termFactory;
    }

    /**
     * @param factory
     */
    public void setTermFactory(TermFactory factory) {
        termFactory = factory;
    }
}
