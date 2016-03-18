package calc;

import calc.ast.AddExpression;
import calc.ast.Expression;
import calc.ast.MultiplyExpression;
import calc.ast.PowerExpression;
import org.testng.annotations.Test;

import java.math.BigInteger;

import static autofixture.publicinterface.Generate.anyInteger;
import static autofixture.publicinterface.Generate.anyLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public class PowerExpressionSpecification {
    @Test
    public void shouldComputePowerOnItsInnerExpressions() {
        // GIVEN
        int leftValue = anyInteger();
        int rightValue = anyInteger();

        Expression left = mock(Expression.class);
        when(left.computeValue()).thenReturn(BigInteger.valueOf(leftValue));

        Expression right = mock(Expression.class);
        when(right.computeValue()).thenReturn(BigInteger.valueOf(rightValue));

        PowerExpression power = new PowerExpression(left, right);

        // WHEN
        BigInteger result = power.computeValue();

        // THEN
        assertThat(result).isEqualTo(BigInteger.valueOf(leftValue).pow(rightValue));
    }

    @Test
    public void shouldComputeNullWhenLeftIsNull() {
        // GIVEN
        int rightValue = anyInteger();

        Expression left = mock(Expression.class);
        when(left.computeValue()).thenReturn(null);

        Expression right = mock(Expression.class);
        when(right.computeValue()).thenReturn(BigInteger.valueOf(rightValue));

        PowerExpression add = new PowerExpression(left, right);

        // WHEN
        BigInteger value = add.computeValue();

        // THEN
        assertThat(value).isNull();
    }

    @Test
    public void shouldComputeNullWhenRightIsNull() {
        // GIVEN
        int leftValue = anyInteger();

        Expression left = mock(Expression.class);
        when(left.computeValue()).thenReturn(BigInteger.valueOf(leftValue));

        Expression right = mock(Expression.class);
        when(right.computeValue()).thenReturn(null);

        PowerExpression add = new PowerExpression(left, right);

        // WHEN
        BigInteger value = add.computeValue();

        // THEN
        assertThat(value).isNull();
    }
}
