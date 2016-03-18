package calc;

import calc.ast.Expression;
import calc.ast.Negate;
import org.testng.annotations.Test;

import java.math.BigInteger;

import static autofixture.publicinterface.Generate.anyInteger;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public class NegateSpecification {
    @Test
    public void shouldNegateItsInnerExpression() {
        // GIVEN
        int innerValue = anyInteger();

        Expression innerExpr = mock(Expression.class);
        when(innerExpr.computeValue()).thenReturn(BigInteger.valueOf(innerValue));

        Negate negate = new Negate(innerExpr);

        // WHEN
        BigInteger result = negate.computeValue();

        // THEN
        assertThat(result).isEqualTo(BigInteger.valueOf(-innerValue));
    }

    @Test
    public void shouldComputeNullWhenInnerValueIsNull() {
        // GIVEN
        Expression innerExpr = mock(Expression.class);
        when(innerExpr.computeValue()).thenReturn(null);

        Negate negate = new Negate(innerExpr);

        // WHEN
        BigInteger result = negate.computeValue();

        // THEN
        assertThat(result).isNull();
    }
}
