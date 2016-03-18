package calc;

import calc.ast.NumberExpression;
import org.testng.annotations.Test;

import java.math.BigInteger;

import static autofixture.publicinterface.Generate.anyInteger;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public class NumberSpecification {
    @Test
    public void shouldComputeItsConstantValue()
    {
        // GIVEN
        int value = anyInteger();
        NumberExpression number = new NumberExpression(BigInteger.valueOf(value));

        // WHEN
        BigInteger result = number.computeValue();

        // THEN
        assertThat(result).isEqualTo(BigInteger.valueOf(value));
    }
}
