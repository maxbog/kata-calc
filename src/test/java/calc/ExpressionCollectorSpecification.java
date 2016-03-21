package calc;

import autofixture.publicinterface.Generate;
import calc.ast.Expression;
import calc.parser.*;
import org.mockito.stubbing.OngoingStubbing;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        when(operatorMatcher.apply(source)).thenReturn(Optional.empty());

        Parser<Expression> lowerLevelParser = mock(Parser.class);
        when(lowerLevelParser.apply(source)).thenReturn(Optional.of(firstExpression));

        ExpressionCollector collector = new ExpressionCollector(lowerLevelParser, operatorMatcher);

        // WHEN
        Optional<ExpressionList> result = collector.collectExpressions(source);

        // THEN
        assertThat(result).hasValueSatisfying(list -> {
            assertThat(list.getFirstExpression()).isEqualTo(firstExpression);
            assertThat(list.getNextOperations()).isEmpty();
        });
    }

    @Test
    public void shouldCollectFirstExpressionAndOneOperationWhenOperatorDoesNotMatchAfterSecondExpression() {
        // GIVEN
        Operator operator = Generate.any(Operator.class);
        Expression firstExpression = Generate.any(Expression.class);
        Expression secondExpression = Generate.any(Expression.class);
        TokenSource source = Generate.any(TokenSource.class);

        TokenMatcher operatorMatcher = mock(TokenMatcher.class);
        when(operatorMatcher.apply(source)).thenReturn(Optional.of(operator)).thenReturn(Optional.empty());

        Parser<Expression> lowerLevelParser = mock(Parser.class);
        when(lowerLevelParser.apply(source)).thenReturn(Optional.of(firstExpression)).thenReturn(Optional.of(secondExpression));

        ExpressionCollector collector = new ExpressionCollector(lowerLevelParser, operatorMatcher);

        // WHEN
        Optional<ExpressionList> result = collector.collectExpressions(source);

        // THEN
        assertThat(result).hasValueSatisfying(list -> {
            assertThat(list.getFirstExpression()).isEqualTo(firstExpression);
            assertThat(list.getNextOperations()).hasSize(1);
            assertThat(list.getNextOperations().head()).isEqualTo(new Operation(operator, secondExpression));
        });

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

        OngoingStubbing<Optional<Operator>> operatorStubbing = when(operatorMatcher.apply(source));
        for (Operator operator : expectedOperators) {
            operatorStubbing = operatorStubbing.thenReturn(Optional.of(operator));
        }
        operatorStubbing.thenReturn(Optional.empty());

        Parser<Expression> lowerLevelParser = mock(Parser.class);
        OngoingStubbing<Optional<Expression>> expressionStubbing = when(lowerLevelParser.apply(source)).thenReturn(Optional.of(firstExpression));
        for (Expression expression : expectedExpressions) {
            expressionStubbing = expressionStubbing.thenReturn(Optional.of(expression));
        }

        ExpressionCollector collector = new ExpressionCollector(lowerLevelParser, operatorMatcher);

        // WHEN
        Optional<ExpressionList> result = collector.collectExpressions(source);

        // THEN
        assertThat(result).hasValueSatisfying(list -> {
            assertThat(list.getFirstExpression()).isEqualTo(firstExpression);
            assertThat(list.getNextOperations()).containsExactlyElementsOf(expectedOperations);
        });
    }
}
