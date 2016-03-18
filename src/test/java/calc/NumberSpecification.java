package calc;

import calc.ast.NumberExpression;
import org.testng.annotations.Test;

import java.math.BigInteger;

import static autofixture.publicinterface.Generate.any;
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
        ValueResolver resolver = any(ValueResolver.class);
        NumberExpression number = new NumberExpression(BigInteger.valueOf(value));

        // WHEN
        BigInteger result = number.computeValue(resolver);

        // THEN
        assertThat(result).isEqualTo(BigInteger.valueOf(value));
    }
}
