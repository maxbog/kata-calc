package calc.ast;

import calc.Operator;
import calc.ast.Expression;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
@FunctionalInterface
public interface BinaryExpressionFactory {
    Expression createBinaryExpression(Operator oper, Expression left, Expression right);
}
