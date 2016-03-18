package calc;

import calc.ast.Expression;
import calc.ast.FunctionCall;
import calc.ast.VariableReference;
import org.testng.annotations.Test;

import java.math.BigInteger;

import static autofixture.publicinterface.Generate.any;
import static autofixture.publicinterface.Generate.anyInteger;
import static autofixture.publicinterface.Generate.anyString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public class FunctionCallSpecification {
    @Test
    public void shouldComputeFunctionValueWhenFunctionHasValue() {
        // GIVEN
        String functionName = anyString();
        BigInteger variableValue = BigInteger.valueOf(anyInteger());
        BigInteger argumentValue = any(BigInteger.class);
        ValueResolver resolver = mock(ValueResolver.class);
        when(resolver.resolveFunctionValue(functionName, argumentValue)).thenReturn(variableValue);

        Expression argument = mock(Expression.class);
        when(argument.computeValue(resolver)).thenReturn(argumentValue);

        FunctionCall ref = new FunctionCall(functionName, argument);

        // WHEN
        BigInteger result = ref.computeValue(resolver);

        // THEN
        assertThat(result).isEqualTo(variableValue);
    }

    @Test
    public void shouldComputeNullWhenFunctionDoesNotHaveValue() {
        // GIVEN
        String functionName = anyString();
        BigInteger argumentValue = any(BigInteger.class);
        ValueResolver resolver = mock(ValueResolver.class);
        when(resolver.resolveFunctionValue(functionName, argumentValue)).thenReturn(null);

        Expression argument = mock(Expression.class);
        when(argument.computeValue(resolver)).thenReturn(argumentValue);

        FunctionCall ref = new FunctionCall(functionName, argument);

        // WHEN
        BigInteger result = ref.computeValue(resolver);

        // THEN
        assertThat(result).isEqualTo(null);

    }
}
