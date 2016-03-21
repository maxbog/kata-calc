package calc.parser;

import calc.TokenSource;
import calc.ast.Expression;
import fj.data.List;

import java.util.Optional;
import java.util.function.BiFunction;

import static fj.data.List.list;

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

    private Optional<List<Operation>> collectTailExpression(TokenSource source) {
        return delimiterMatcher.apply(source)
                .map(operator -> collectExpressions(source, (currentExpression, tail) -> tail.cons(new Operation(operator, currentExpression))))
                .orElse(Optional.of(list()));
    }

    private <T> Optional<T> collectExpressions(TokenSource source, BiFunction<Expression, List<Operation>, T> resultCreator) {
        return lowerLevelParser.apply(source)
                .flatMap(currentExpression ->
                        collectTailExpression(source)
                                .map(tail -> resultCreator.apply(currentExpression, tail)));
    }

    public Optional<ExpressionList> collectExpressions(TokenSource source) {
        return collectExpressions(source, ExpressionList::new);
    }

}
