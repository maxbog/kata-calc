package calc;

import autofixture.publicinterface.Generate;
import calc.ast.BinaryExpressionFactory;
import calc.ast.Expression;
import calc.parser.ExpressionCollector;
import calc.parser.ExpressionList;
import calc.parser.LeftAssociativeExpressionParser;
import calc.parser.Operation;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
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
        ExpressionList list = new ExpressionList(Generate.any(Expression.class), Collections.emptyList());
        BinaryExpressionFactory expressionFactory = Generate.any(BinaryExpressionFactory.class);

        ExpressionCollector collector = mock(ExpressionCollector.class);
        when(collector.collectExpressions(source)).thenReturn(list);

        LeftAssociativeExpressionParser parser = new LeftAssociativeExpressionParser(collector, expressionFactory);

        // WHEN
        Expression result = parser.parseLeftAssociativeExpressionList(source);

        // THEN
        assertThat(result).isEqualTo(list.getFirstExpression());
    }

    @Test
    public void shouldReturnCombinedExpressionWhenListHasOneElement() {
        // GIVEN
        TokenSource source = Generate.any(TokenSource.class);
        Expression firstExpression = Generate.any(Expression.class);
        Expression secondExpression = Generate.any(Expression.class);
        Expression combinedExpression = Generate.any(Expression.class);
        Operator firstOperator = Generate.any(Operator.class);

        ExpressionList list = new ExpressionList(firstExpression, Collections.singletonList(new Operation(firstOperator, secondExpression)));
        BinaryExpressionFactory expressionFactory = mock(BinaryExpressionFactory.class);
        when(expressionFactory.createBinaryExpression(firstOperator, firstExpression, secondExpression)).thenReturn(combinedExpression);

        ExpressionCollector collector = mock(ExpressionCollector.class);
        when(collector.collectExpressions(source)).thenReturn(list);

        LeftAssociativeExpressionParser parser = new LeftAssociativeExpressionParser(collector, expressionFactory);

        // WHEN
        Expression result = parser.parseLeftAssociativeExpressionList(source);

        // THEN
        assertThat(result).isEqualTo(combinedExpression);
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

        ExpressionList list = new ExpressionList(firstExpression, Arrays.asList(
                new Operation(firstOperator, secondExpression),
                new Operation(secondOperator, thirdExpression)));

        BinaryExpressionFactory expressionFactory = mock(BinaryExpressionFactory.class);
        when(expressionFactory.createBinaryExpression(firstOperator, firstExpression, secondExpression)).thenReturn(firstCombinedExpression);
        when(expressionFactory.createBinaryExpression(secondOperator, firstCombinedExpression, thirdExpression)).thenReturn(secondCombinedExpression);

        ExpressionCollector collector = mock(ExpressionCollector.class);
        when(collector.collectExpressions(source)).thenReturn(list);

        LeftAssociativeExpressionParser parser = new LeftAssociativeExpressionParser(collector, expressionFactory);

        // WHEN
        Expression result = parser.parseLeftAssociativeExpressionList(source);

        // THEN
        assertThat(result).isEqualTo(secondCombinedExpression);
    }
}
