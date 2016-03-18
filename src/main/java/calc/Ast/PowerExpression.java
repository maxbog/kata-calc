package calc.ast;

import java.math.BigInteger;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public class PowerExpression extends BinaryExpression {
    public PowerExpression(Expression left, Expression right) {
        super(left, right);
    }


    @Override
    public String toString() {
        return "[Power " + getLeft() + " " + getRight() + "]";
    }

    @Override
    public BigInteger computeValue() {
        BigInteger leftValue = getLeft().computeValue();
        BigInteger rightValue = getRight().computeValue();
        if(leftValue == null || rightValue == null)
            return null;
        return leftValue.pow(rightValue.intValue());
    }
}
