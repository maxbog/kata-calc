package calc.ast;

import calc.ValueResolver;

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
    public BigInteger computeValue(ValueResolver valueResolver) {
        BigInteger leftValue = getLeft().computeValue(valueResolver);
        BigInteger rightValue = getRight().computeValue(valueResolver);
        if(leftValue == null || rightValue == null)
            return null;
        return leftValue.add(rightValue);
    }
}
