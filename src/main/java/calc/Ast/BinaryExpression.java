package calc.Ast;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryExpression that = (BinaryExpression) o;
        return Objects.equals(left, that.left) &&
                Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
