package calc.Ast;

import calc.Operator;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class AddExpression extends BinaryExpression {
    public AddExpression(Expression left, Expression right) {
        super(left, right);
    }
}
