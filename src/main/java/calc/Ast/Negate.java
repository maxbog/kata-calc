package calc.Ast;

import java.util.Objects;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class Negate extends Expression {
    private Expression expression;

    public Negate(Expression expression) {

        this.expression = expression;
    }

    @Override
    public String toString() {
        return "[Negate " + expression + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Negate negate = (Negate) o;
        return Objects.equals(expression, negate.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression);
    }
}
