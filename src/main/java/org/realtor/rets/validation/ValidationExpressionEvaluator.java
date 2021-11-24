/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/ValidationExpressionEvaluator.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation;

import org.realtor.rets.validation.operators.CloseParen;
import org.realtor.rets.validation.operators.NotOperator;
import org.realtor.rets.validation.operators.OpenParen;
import org.realtor.rets.validation.operators.Operator;
import org.realtor.rets.validation.terms.InvalidTermException;
import org.realtor.rets.validation.terms.Term;
import org.realtor.rets.validation.terms.TermFactory;

import java.util.Map;
import java.util.Stack;


/**
 *  ValidationExpressionEvaluator.java Created Aug 22, 2003
 *   This class implements the RETS validation language version 1.5.
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class ValidationExpressionEvaluator {
    private String expression;
    private ParseTree tree;
    private Stack tokens;
    private TermFactory factory;

    public ValidationExpressionEvaluator() {
        expression = "(3 + 5)";
        factory = new TermFactory();
    }

    public ValidationExpressionEvaluator(String expr) {
        this();
        expression = expr;
    }

    /**
     * Evaluates an expression and returns a Term which is the result of the evaluation.
     *  the exact type of the term depends on the expression.
     * @param expr The expression to evaluate
     * @return a term representing the result of the evaluation
     * @throws InvalidTermException
     */
    public Term eval(String expr) throws InvalidTermException {
        setExpression(expr);
        eval(Operator.MIN_PRECEDENCE);

        return (Term) tokens.pop();
    }

    private void eval(int currentPrecedence) throws InvalidTermException {
        while (tokens.size() > 1) {
            Term firstTerm;
            Operator oper;
            Term secondTerm;
            Object firstObj = tokens.pop();

            if (firstObj instanceof OpenParen) {
                tokens.push(firstObj);
                parseParenthetical();
            } else if (firstObj instanceof NotOperator) {
                NotOperator not = (NotOperator) firstObj;
                eval(not.getPrecedence());
                tokens.push(not.apply((Term) tokens.pop()));
            } else {
                firstTerm = (Term) firstObj;
                oper = (Operator) tokens.pop();

                if (tokens.peek() instanceof OpenParen) {
                    parseParenthetical();
                }

                secondTerm = (Term) tokens.pop();

                if (tokens.size() > 0) {
                    Object lookAhead = tokens.peek();

                    if (lookAhead instanceof Operator) {
                        Operator nextOper = (Operator) lookAhead;

                        if (nextOper instanceof CloseParen) {
                            tokens.pop();
                        } else if (nextOper.getPrecedence() > oper.getPrecedence()) {
                            tokens.push(secondTerm);
                            eval(nextOper.getPrecedence());
                            secondTerm = (Term) tokens.pop();
                        } else if (nextOper.getPrecedence() < currentPrecedence) {
                            tokens.push(oper.apply(firstTerm, secondTerm));

                            break;
                        }
                    } else {
                        // invalid expression?
                        throw new InvalidTermException(
                            "Invalid Expression, expected an operator but got " +
                            lookAhead);
                    }
                }

                tokens.push(oper.apply(firstTerm, secondTerm));
            }
        }
    }

    /**
     * This method pulls out the parenthetical expression, evaluates it, and then places the result on the
     *  original stack in place of the expression.
     * @throws InvalidTermException
     */
    private void parseParenthetical() throws InvalidTermException {
        Stack paren = getParentheticalExpression(tokens);
        Stack tmpTokens = tokens;
        tokens = paren;
        eval(Operator.MIN_PRECEDENCE);
        tmpTokens.push(tokens.pop());

        tokens = tmpTokens;
    }

    /**
     * Returns a stack containing a parenthetical expresion.
     * If the expression is ( 3 + 5), 3 + 5 is returned. If the expression is ((3)) (3) is returned.
     * @param orig
     * @return
     */
    private Stack getParentheticalExpression(Stack orig) {
        Stack newEq = new Stack();
        orig.pop(); // remove the first paren

        int openParens = 1;

        while (orig.size() > 0) {
            Object current = orig.pop();

            if (current instanceof OpenParen) {
                openParens++;
            } else if (current instanceof CloseParen) {
                openParens--;

                if (openParens == 0) {
                    break;
                }
            }

            newEq.add(0, current);
        }

        return newEq;
    }

    /**
     *  This method tokenizes an expression. That is, it replaces the textual
     *  representation of the expression with
     * @param expr
     */
    private void tokenize(String expr) {
        RetsTokenParser tokenizer = new RetsTokenParser(expr);
        tokenizer.setTermFactory(factory);
        tokens = new Stack();

        int i = 0;
        Object token;

        while ((token = tokenizer.nextToken()) != null) {
            tokens.add(i, token);
        }
    }

    /**
     *  Return
     * @return
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Sets the expression to evaluate
     * @param string The expression to be evaluated.
     */
    public void setExpression(String string) {
        expression = string;
        tokenize(expression);
    }

    public void setSymbolTable(Map m) {
        factory.setSymbolTable(m);
    }

    public TermFactory getTermFactory() {
        return factory;
    }

    public static void main(String[] args) throws Exception {
        ValidationExpressionEvaluator parser = new ValidationExpressionEvaluator();
        parser.eval("((3))");
    }
}
