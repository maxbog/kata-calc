package calc;

import calc.ast.AddExpression;
import calc.ast.Expression;
import org.testng.annotations.Test;

import java.math.BigInteger;

import static autofixture.publicinterface.Generate.any;
import static autofixture.publicinterface.Generate.anyInteger;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public class AddExpressionSpecification {
    @Test
    public void shouldAddTheValuesOfItsInnerExpressions() {
        // GIVEN
        int leftValue = anyInteger();
        int rightValue = anyInteger();
        ValueResolver resolver = any(ValueResolver.class);

        Expression left = mock(Expression.class);
        when(left.computeValue(resolver)).thenReturn(BigInteger.valueOf(leftValue));

        Expression right = mock(Expression.class);
        when(right.computeValue(resolver)).thenReturn(BigInteger.valueOf(rightValue));

        AddExpression add = new AddExpression(left, right);

        // WHEN
        BigInteger value = add.computeValue(resolver);

        // THEN
        assertThat(value).isEqualTo(BigInteger.valueOf(leftValue+rightValue));
    }

    @Test
    public void shouldComputeNullWhenLeftIsNull() {
        // GIVEN
        int rightValue = anyInteger();
        ValueResolver resolver = any(ValueResolver.class);

        Expression left = mock(Expression.class);
        when(left.computeValue(resolver)).thenReturn(null);

        Expression right = mock(Expression.class);
        when(right.computeValue(resolver)).thenReturn(BigInteger.valueOf(rightValue));

        AddExpression add = new AddExpression(left, right);

        // WHEN
        BigInteger value = add.computeValue(resolver);

        // THEN
        assertThat(value).isNull();
    }

    @Test
    public void shouldComputeNullWhenRightIsNull() {
        // GIVEN
        int leftValue = anyInteger();
        ValueResolver resolver = any(ValueResolver.class);

        Expression left = mock(Expression.class);
        when(left.computeValue(resolver)).thenReturn(BigInteger.valueOf(leftValue));

        Expression right = mock(Expression.class);
        when(right.computeValue(resolver)).thenReturn(null);

        AddExpression add = new AddExpression(left, right);

        // WHEN
        BigInteger value = add.computeValue(resolver);

        // THEN
        assertThat(value).isNull();
    }
}
