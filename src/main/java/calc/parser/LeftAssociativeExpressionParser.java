package calc.parser;

import calc.TokenSource;
import calc.ast.BinaryExpressionFactory;
import calc.ast.Expression;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class LeftAssociativeExpressionParser {
    private ExpressionCollector expressionCollector;
    private BinaryExpressionFactory nodeFactory;

    public LeftAssociativeExpressionParser(ExpressionCollector expressionCollector, BinaryExpressionFactory nodeFactory) {
        this.expressionCollector = expressionCollector;
        this.nodeFactory = nodeFactory;
    }

    private Expression leftAssociativeCombine(ExpressionList list) {
        Expression result = list.getFirstExpression();
        for (Operation operation : list.getNextOperations()) {
            result = operation.createExpression(result, nodeFactory);
        }
        return result;
    }

    public Expression parseLeftAssociativeExpressionList(TokenSource source) {

        ExpressionList operations = expressionCollector.collectExpressions(source);
        if(operations == null)
            return null;
        return leftAssociativeCombine(operations);
    }
}

