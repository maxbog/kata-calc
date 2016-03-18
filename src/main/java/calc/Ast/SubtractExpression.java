package calc.ast;

import calc.ValueResolver;

import java.math.BigInteger;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public class SubtractExpression extends BinaryExpression {
    public SubtractExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return "[Subtract " + getLeft() + " " + getRight() + "]";
    }

    @Override
    public BigInteger computeValue(ValueResolver valueResolver) {
        BigInteger leftValue = getLeft().computeValue(valueResolver);
        BigInteger rightValue = getRight().computeValue(valueResolver);
        if(leftValue == null || rightValue == null)
            return null;
        return leftValue.subtract(rightValue);
    }
}
