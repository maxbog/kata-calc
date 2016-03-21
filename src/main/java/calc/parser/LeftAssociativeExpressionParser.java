package calc.parser;

import calc.TokenSource;
import calc.ast.BinaryExpressionFactory;
import calc.ast.Expression;

import java.util.Optional;

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
        return list.getNextOperations()
                .foldLeft(
                        (current, operation) -> operation.createExpression(current, nodeFactory),
                        list.getFirstExpression()
                );
    }

    public Optional<Expression> parseLeftAssociativeExpressionList(TokenSource source) {
        return expressionCollector.collectExpressions(source)
                .map(this::leftAssociativeCombine);
    }
}

