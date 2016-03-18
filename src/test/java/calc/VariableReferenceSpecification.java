package calc;

import calc.ast.VariableReference;
import org.testng.annotations.Test;

import java.math.BigInteger;

import static autofixture.publicinterface.Generate.anyInteger;
import static autofixture.publicinterface.Generate.anyString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public class VariableReferenceSpecification {
    @Test
    public void shouldComputeVariableValueWhenVariableHasValue() {
        // GIVEN
        String varName = anyString();
        BigInteger variableValue = BigInteger.valueOf(anyInteger());
        ValueResolver resolver = mock(ValueResolver.class);
        when(resolver.resolveVariable(varName)).thenReturn(variableValue);

        VariableReference ref = new VariableReference(varName);

        // WHEN
        BigInteger result = ref.computeValue(resolver);

        // THEN
        assertThat(result).isEqualTo(variableValue);

    }
    @Test
    public void shouldComputeNullWhenVariableDoesNotHaveValue() {
        // GIVEN
        String varName = anyString();
        ValueResolver resolver = mock(ValueResolver.class);
        when(resolver.resolveVariable(varName)).thenReturn(null);

        VariableReference ref = new VariableReference(varName);

        // WHEN
        BigInteger result = ref.computeValue(resolver);

        // THEN
        assertThat(result).isEqualTo(null);

    }
}
