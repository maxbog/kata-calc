package calc;

import autofixture.publicinterface.Generate;
import calc.ast.Expression;
import calc.parser.*;
import fj.data.Option;
import org.mockito.stubbing.OngoingStubbing;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class ExpressionCollectorSpecification {
    @Test
    public void shouldCollectOnlyFirstExpressionWhenOperatorIsNotMatchedAfterFirstExpression() {
        // GIVEN
        Expression firstExpression = Generate.any(Expression.class);
        TokenSource source = Generate.any(TokenSource.class);

        TokenMatcher operatorMatcher = mock(TokenMatcher.class);
        when(operatorMatcher.apply(source)).thenReturn(Option.none());

        Parser<Expression> lowerLevelParser = mock(Parser.class);
        when(lowerLevelParser.apply(source)).thenReturn(firstExpression);

        ExpressionCollector collector = new ExpressionCollector(lowerLevelParser, operatorMatcher);

        // WHEN
        ExpressionList result = collector.collectExpressions(source);

        // THEN
        assertThat(result.getFirstExpression()).isEqualTo(firstExpression);
        assertThat(result.getNextOperations()).isEmpty();
    }

    @Test
    public void shouldCollectFirstExpressionAndOneOperationWhenOperatorDoesNotMatchAfterSecondExpression() {
        // GIVEN
        Operator operator = Generate.any(Operator.class);
        Expression firstExpression = Generate.any(Expression.class);
        Expression secondExpression = Generate.any(Expression.class);
        TokenSource source = Generate.any(TokenSource.class);

        TokenMatcher operatorMatcher = mock(TokenMatcher.class);
        when(operatorMatcher.apply(source)).thenReturn(Option.some(operator)).thenReturn(Option.none());

        Parser<Expression> lowerLevelParser = mock(Parser.class);
        when(lowerLevelParser.apply(source)).thenReturn(firstExpression).thenReturn(secondExpression);

        ExpressionCollector collector = new ExpressionCollector(lowerLevelParser, operatorMatcher);

        // WHEN
        ExpressionList result = collector.collectExpressions(source);

        // THEN
        assertThat(result.getFirstExpression()).isEqualTo(firstExpression);
        assertThat(result.getNextOperations()).hasSize(1);
        assertThat(result.getNextOperations().get(0)).isEqualTo(new Operation(operator, secondExpression));
    }


    void addAnyOperation(List<Operation> expected) {
        Operator operator = Generate.any(Operator.class);
        Expression secondExpression = Generate.any(Expression.class);
        expected.add(new Operation(operator, secondExpression));
    }

    @Test
    public void shouldCollectFirstExpressionAndRestOfExpressionsAsOperations() {
        // GIVEN
        List<Expression> expectedExpressions = Generate.manyAsListOf(Expression.class);
        List<Operator> expectedOperators = new ArrayList<>();
        List<Operation> expectedOperations = expectedExpressions.stream().map(ex -> {
            final Operator operator = Generate.any(Operator.class);
            expectedOperators.add(operator);
            return new Operation(operator, ex);
        }).collect(Collectors.toList());

        Expression firstExpression = Generate.any(Expression.class);
        TokenSource source = Generate.any(TokenSource.class);

        TokenMatcher operatorMatcher = mock(TokenMatcher.class);

        OngoingStubbing<Option<Operator>> operatorStubbing = when(operatorMatcher.apply(source));
        for (Operator operator : expectedOperators) {
            operatorStubbing = operatorStubbing.thenReturn(Option.some(operator));
        }
        operatorStubbing.thenReturn(Option.none());

        Parser<Expression> lowerLevelParser = mock(Parser.class);
        OngoingStubbing<Expression> expressionStubbing = when(lowerLevelParser.apply(source)).thenReturn(firstExpression);
        for (Expression expression : expectedExpressions) {
            expressionStubbing = expressionStubbing.thenReturn(expression);
        }

        ExpressionCollector collector = new ExpressionCollector(lowerLevelParser, operatorMatcher);

        // WHEN
        ExpressionList result = collector.collectExpressions(source);

        // THEN
        assertThat(result.getFirstExpression()).isEqualTo(firstExpression);
        assertThat(result.getNextOperations()).containsExactlyElementsOf(expectedOperations);
    }
}
