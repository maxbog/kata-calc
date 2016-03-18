package calc.ast;

import calc.ValueResolver;

import java.math.BigInteger;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class DivideExpression extends BinaryExpression {
    public DivideExpression(Expression left, Expression right) {
        super(left, right);
    }
    @Override
    public String toString() {
        return "[Divide " + getLeft() + " " + getRight() + "]";
    }

    @Override
    public BigInteger computeValue(ValueResolver valueResolver) {
        BigInteger leftValue = getLeft().computeValue(valueResolver);
        BigInteger rightValue = getRight().computeValue(valueResolver);
        if(leftValue == null || rightValue == null || rightValue.equals(BigInteger.ZERO))
            return null;
        return leftValue.divide(rightValue);
    }
}
