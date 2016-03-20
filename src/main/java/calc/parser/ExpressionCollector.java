package calc.parser;

import calc.*;
import calc.ast.Expression;

import java.util.ArrayList;
import java.util.List;

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

    private boolean collectTailExpression(TokenSource source, List<Operation> operations) {
        Operator operator = delimiterMatcher.apply(source);

        if (operator != null) {
            Expression currentExpression = lowerLevelParser.apply(source);

            if (currentExpression == null)
                return false;

            operations.add(new Operation(operator, currentExpression));

            return collectTailExpression(source, operations);
        }

        return true;
    }

    public ExpressionList collectExpressions(TokenSource source) {
        Expression firstExpression = lowerLevelParser.apply(source);
        if(firstExpression == null)
            return null;

        List<Operation> nextExpressions = new ArrayList<>();
        if(!collectTailExpression(source, nextExpressions))
            return null;

        return new ExpressionList(firstExpression, nextExpressions);
    }
}
