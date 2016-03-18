package calc.ast;

import java.math.BigInteger;
import java.util.Objects;

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
    public BigInteger computeValue() {
        BigInteger leftValue = getLeft().computeValue();
        BigInteger rightValue = getRight().computeValue();
        if(leftValue == null || rightValue == null || rightValue.equals(BigInteger.ZERO))
            return null;
        return leftValue.divide(rightValue);
    }
}
