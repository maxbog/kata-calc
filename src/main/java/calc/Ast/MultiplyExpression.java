package calc.Ast;

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
}
