/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/ParseTree.java,v 1.2 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation;

import org.realtor.rets.validation.operators.*;
import org.realtor.rets.validation.terms.*;


/**
 *  ParseTree.java Created Aug 22, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.2 $
 *  @author scohen
 */
public class ParseTree {
    private ParseTree left = null;
    private Operator operator = null;
    private ParseTree right = null;
    private Term term;
    private ParseTree parent;

    public ParseTree() {
    }

    public ParseTree(Term t) {
        term = t;
    }

    public ParseTree(ParseTree l, ParseTree r) {
        left = l;
        right = r;
    }

    public ParseTree(ParseTree l, ParseTree r, Operator oper) {
        left = l;
        right = r;
        operator = oper;
    }

    /**
     * @return
     */
    public ParseTree getLeft() {
        return left;
    }

    /**
     * @return
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * @return
     */
    public ParseTree getRight() {
        return right;
    }

    /**
     * @param tree
     */
    public void setLeft(ParseTree tree) {
        left = tree;
        tree.setParent(this);
    }

    /**
     * @param operator
     */
    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    /**
     * @param tree
     */
    public void setRight(ParseTree tree) {
        right = tree;
        tree.setParent(this);
    }

    /**
     * @return
     */
    public Term getTerm() {
        return term;
    }

    /**
     * @param term
     */
    public void setTerm(Term term) {
        this.term = term;
    }

    public boolean isLeaf() {
        return (left == null) && (right == null);
    }

    /**
     * @return
     */
    public ParseTree getParent() {
        return parent;
    }

    /**
     * @param tree
     */
    public void setParent(ParseTree tree) {
        parent = tree;
    }

    public Term execute() {
        Term leftSide;
        Term rightSide;

        if (!left.isLeaf()) {
            leftSide = left.execute();
        } else {
            leftSide = left.getTerm();
        }

        // our left is a leaf.
        if (!right.isLeaf()) {
            rightSide = right.execute();
        } else {
            rightSide = right.getTerm();
        }

        try {
            return operator.apply(leftSide, rightSide);
        } catch (InvalidTermException ite) {
            System.out.println("Could not execute: " + ite.getMessage());

            return null;
        }
    }

    public String getExpression() {
        StringBuffer expr = new StringBuffer();
        printExpression(expr);

        return expr.toString();
    }

    private void printExpression(StringBuffer sb) {
        if (left != null) {
            sb.append("( ");
            left.printExpression(sb);
        }

        if (getOperator() != null) {
            sb.append(getOperator().getSymbol());
        } else {
            if (getTerm() != null) {
                sb.append(getTerm().getValue());
            }
        }

        sb.append(" ");

        if (right != null) {
            right.printExpression(sb);
            sb.append(" ) ");
        }
    }

    public void print(int level, String side) {
        if (left != null) {
            left.print(level + 1, "left");
        }

        if (right != null) {
            right.print(level + 1, "right");
        }

        System.out.println();
        System.out.print("level: " + level + " ");

        if (side.length() > 0) {
            System.out.print(side + " side ");
        }

        if (getOperator() != null) {
            System.out.print(getOperator());
        } else {
            System.out.print("Term:" + getTerm().getValue().toString());
        }
    }

    public static void main(String[] args) throws Exception {
        ParseTree root;
        ParseTree valueOne = new ParseTree();

        ParseTree valueTwo = new ParseTree();
        ParseTree valueThree = new ParseTree();
        ParseTree valueFour = new ParseTree();

        ParseTree leftEquals;
        ParseTree rightEquals;

        valueOne.setTerm(new StringTerm("ListAgent"));
        valueTwo.setTerm(new StringTerm("ListRecord"));

        valueThree.setTerm(new DateTerm("2003-04-07"));
        valueFour.setTerm(new DateTerm("2003-08-12"));

        leftEquals = new ParseTree(valueOne, valueTwo, new EqualsOperator());
        rightEquals = new ParseTree(valueThree, valueFour,
                new LessThanOperator());

        root = new ParseTree(leftEquals, rightEquals, new OrOperator());
        System.out.println(rightEquals.execute());
        root.print(0, "");
        System.out.println();
        System.out.println(root.getExpression());

        //System.out.println(root.execute());
    }
}
