package calc.parser;

import calc.Operator;
import calc.TokenSource;
import calc.ast.Expression;
import fj.data.List;
import fj.data.Option;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class ExpressionCollector {
    private Parser<Expression> lowerLevelParser;
    private TokenMatcher delimiterMatcher;

    public ExpressionCollector(Parser<Expression> lowerLevelParser, TokenMatcher delimiterMatcher) {
        this.lowerLevelParser = lowerLevelParser;
        this.delimiterMatcher = delimiterMatcher;
    }

    private Option<List<Operation>> collectTailExpression(TokenSource source) {
        return delimiterMatcher.apply(source)
                .bind(operator -> parseNextExpression(source, operator));
    }

    private Option<List<Operation>> parseNextExpression(TokenSource source, Operator operator) {
        return lowerLevelParser.apply(source).map(currentExpression -> extendExpressionList(source, operator, currentExpression));
    }

    private List<Operation> extendExpressionList(TokenSource source, Operator operator, Expression currentExpression) {
        Operation currentOperation = new Operation(operator, currentExpression);

        return collectTailExpression(source)
            .map(operations -> operations.cons(currentOperation))
            .orSome(() -> List.single(currentOperation));
    }

    public Option<ExpressionList> collectExpressions(TokenSource source) {
        return lowerLevelParser.apply(source)
                .bind(firstExpression -> collectTailExpression(source)
                        .map(nextExpressions -> new ExpressionList(firstExpression, nextExpressions)));
    }
}
