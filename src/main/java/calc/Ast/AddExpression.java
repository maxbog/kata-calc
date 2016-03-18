package calc.ast;

import java.math.BigInteger;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class AddExpression extends BinaryExpression {
    public AddExpression(Expression left, Expression right) {
        super(left, right);
    }


    @Override
    public String toString() {
        return "[Add " + getLeft() + " " + getRight() + "]";
    }

    @Override
    public BigInteger computeValue() {
        BigInteger leftValue = getLeft().computeValue();
        BigInteger rightValue = getRight().computeValue();
        if(leftValue == null || rightValue == null)
            return null;
        return leftValue.add(rightValue);
    }
}
