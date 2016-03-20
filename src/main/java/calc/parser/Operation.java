package calc.parser;

import calc.Operator;
import calc.ast.BinaryExpressionFactory;
import calc.ast.Expression;

import java.util.Objects;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class Operation {
    private Operator operator;
    private Expression expression;

    public Operation(Operator operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }

    public Expression createExpression(Expression left, BinaryExpressionFactory expressionFactory) {
        return expressionFactory.createBinaryExpression(operator, left, expression);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return operator == operation.operator &&
                Objects.equals(expression, operation.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, expression);
    }
}

