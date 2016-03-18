package calc.Ast;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class BinaryExpression extends Expression{
    private final Expression left;
    private final Expression right;

    public BinaryExpression(Expression left, Expression right) {

        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }
}
