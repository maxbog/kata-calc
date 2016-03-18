package calc;

import calc.ast.DivideExpression;
import calc.ast.Expression;
import org.testng.annotations.Test;

import java.math.BigInteger;

import static autofixture.publicinterface.Generate.anyInteger;
import static autofixture.publicinterface.Generate.anyIntegerOtherThan;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public class DivideExpressionSpecification {
    @Test
    public void shouldDivideTheValuesOfItsInnerExpressionsWhenRightIsNotZero() {
        // GIVEN
        int leftValue = anyInteger();
        int rightValue = anyIntegerOtherThan(0);

        Expression left = mock(Expression.class);
        when(left.computeValue()).thenReturn(BigInteger.valueOf(leftValue));

        Expression right = mock(Expression.class);
        when(right.computeValue()).thenReturn(BigInteger.valueOf(rightValue));

        DivideExpression divide = new DivideExpression(left, right);

        // WHEN
        BigInteger result = divide.computeValue();

        // THEN
        assertThat(result).isEqualTo(BigInteger.valueOf(leftValue/rightValue));
    }

    @Test
    public void shouldComputeNullWhenLeftIsNull() {
        // GIVEN
        int rightValue = anyInteger();

        Expression left = mock(Expression.class);
        when(left.computeValue()).thenReturn(null);

        Expression right = mock(Expression.class);
        when(right.computeValue()).thenReturn(BigInteger.valueOf(rightValue));

        DivideExpression divide = new DivideExpression(left, right);

        // WHEN
        BigInteger result = divide.computeValue();

        // THEN
        assertThat(result).isNull();
    }

    @Test
    public void shouldComputeNullWhenRightIsNull() {
        // GIVEN
        int leftValue = anyInteger();

        Expression left = mock(Expression.class);
        when(left.computeValue()).thenReturn(BigInteger.valueOf(leftValue));

        Expression right = mock(Expression.class);
        when(right.computeValue()).thenReturn(null);

        DivideExpression divide = new DivideExpression(left, right);

        // WHEN
        BigInteger result = divide.computeValue();

        // THEN
        assertThat(result).isNull();
    }

    @Test
    public void shouldComputeNullWhenRightIsZero() {
        // GIVEN
        int leftValue = anyInteger();

        Expression left = mock(Expression.class);
        when(left.computeValue()).thenReturn(BigInteger.valueOf(leftValue));

        Expression right = mock(Expression.class);
        when(right.computeValue()).thenReturn(BigInteger.ZERO);

        DivideExpression divide = new DivideExpression(left, right);

        // WHEN
        BigInteger result = divide.computeValue();

        // THEN
        assertThat(result).isNull();
    }
}
