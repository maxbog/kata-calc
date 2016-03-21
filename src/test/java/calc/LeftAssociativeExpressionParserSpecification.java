package calc;

import autofixture.publicinterface.Generate;
import calc.ast.BinaryExpressionFactory;
import calc.ast.Expression;
import calc.parser.ExpressionCollector;
import calc.parser.ExpressionList;
import calc.parser.LeftAssociativeExpressionParser;
import calc.parser.Operation;
import fj.data.List;
import org.testng.annotations.Test;

import java.util.Optional;

import static fj.data.List.list;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class LeftAssociativeExpressionParserSpecification {
    @Test
    public void shouldReturnFirstExpressionWhenOperationListIsEmpty() {
        // GIVEN
        TokenSource source = Generate.any(TokenSource.class);
        ExpressionList list = new ExpressionList(Generate.any(Expression.class), List.nil());
        BinaryExpressionFactory expressionFactory = Generate.any(BinaryExpressionFactory.class);

        ExpressionCollector collector = mock(ExpressionCollector.class);
        when(collector.collectExpressions(source)).thenReturn(Optional.of(list));

        LeftAssociativeExpressionParser parser = new LeftAssociativeExpressionParser(collector, expressionFactory);

        // WHEN
        Optional<Expression> result = parser.parseLeftAssociativeExpressionList(source);

        // THEN
        assertThat(result).hasValue(list.getFirstExpression());
    }

    @Test
    public void shouldReturnCombinedExpressionWhenListHasOneElement() {
        // GIVEN
        TokenSource source = Generate.any(TokenSource.class);
        Expression firstExpression = Generate.any(Expression.class);
        Expression secondExpression = Generate.any(Expression.class);
        Expression combinedExpression = Generate.any(Expression.class);
        Operator firstOperator = Generate.any(Operator.class);

        ExpressionList list = new ExpressionList(firstExpression, list(new Operation(firstOperator, secondExpression)));
        BinaryExpressionFactory expressionFactory = mock(BinaryExpressionFactory.class);
        when(expressionFactory.createBinaryExpression(firstOperator, firstExpression, secondExpression)).thenReturn(combinedExpression);

        ExpressionCollector collector = mock(ExpressionCollector.class);
        when(collector.collectExpressions(source)).thenReturn(Optional.of(list));

        LeftAssociativeExpressionParser parser = new LeftAssociativeExpressionParser(collector, expressionFactory);

        // WHEN
        Optional<Expression> result = parser.parseLeftAssociativeExpressionList(source);

        // THEN
        assertThat(result).hasValue(combinedExpression);
    }

    @Test
    public void shouldReturnCombinedExpressionWhenListHasTwoElements() {
        // GIVEN
        TokenSource source = Generate.any(TokenSource.class);
        Expression firstExpression = Generate.any(Expression.class);
        Expression secondExpression = Generate.any(Expression.class);
        Expression thirdExpression = Generate.any(Expression.class);

        Expression firstCombinedExpression = Generate.any(Expression.class);
        Expression secondCombinedExpression = Generate.any(Expression.class);

        Operator firstOperator = Generate.any(Operator.class);
        Operator secondOperator = Generate.any(Operator.class);

        ExpressionList list = new ExpressionList(firstExpression, list(
                new Operation(firstOperator, secondExpression),
                new Operation(secondOperator, thirdExpression)));

        BinaryExpressionFactory expressionFactory = mock(BinaryExpressionFactory.class);
        when(expressionFactory.createBinaryExpression(firstOperator, firstExpression, secondExpression)).thenReturn(firstCombinedExpression);
        when(expressionFactory.createBinaryExpression(secondOperator, firstCombinedExpression, thirdExpression)).thenReturn(secondCombinedExpression);

        ExpressionCollector collector = mock(ExpressionCollector.class);
        when(collector.collectExpressions(source)).thenReturn(Optional.of(list));

        LeftAssociativeExpressionParser parser = new LeftAssociativeExpressionParser(collector, expressionFactory);

        // WHEN
        Optional<Expression> result = parser.parseLeftAssociativeExpressionList(source);

        // THEN
        assertThat(result).hasValue(secondCombinedExpression);
    }
}
