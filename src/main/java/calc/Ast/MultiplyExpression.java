package calc.ast;

import calc.ValueResolver;

import java.math.BigInteger;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class MultiplyExpression extends BinaryExpression {
    public MultiplyExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return "[Multiply " + getLeft() + " " + getRight() + "]";
    }

    @Override
    public BigInteger computeValue(ValueResolver valueResolver) {
        BigInteger leftValue = getLeft().computeValue(valueResolver);
        BigInteger rightValue = getRight().computeValue(valueResolver);
        if(leftValue == null || rightValue == null)
            return null;
        return leftValue.multiply(rightValue);
    }
}
