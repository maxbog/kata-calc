package calc;

import autofixture.publicinterface.InstanceOf;
import calc.ast.Expression;
import calc.symboltable.SymbolTable;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.function.Function;

import static autofixture.publicinterface.Generate.any;
import static autofixture.publicinterface.Generate.anyInteger;
import static autofixture.publicinterface.Generate.anyString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public class SymbolTableSpecification {
    @Test
    public void shouldStoreVariableValues() {
        // GIVEN
        String varName = anyString();
        BigInteger varValue = BigInteger.valueOf(anyInteger());
        SymbolTable table = new SymbolTable();

        // WHEN
        table.assignValue(varName, varValue);
        BigInteger resolvedValue = table.resolveVariable(varName);

        // THEN
        assertThat(resolvedValue).isEqualTo(varValue);
    }

    @Test
    public void shouldReturnNullWhenVariableWasNotAssignedValue() {
        // GIVEN
        String varName = anyString();
        SymbolTable table = new SymbolTable();

        // WHEN
        BigInteger resolvedValue = table.resolveVariable(varName);

        // THEN
        assertThat(resolvedValue).isNull();
    }

    @Test
    public void shouldReturnNullFunctionValueWhenFunctionDoesNotExist() {
        // GIVEN
        String functionName = anyString();
        BigInteger argument = any(BigInteger.class);
        SymbolTable table = new SymbolTable();

        // WHEN
        BigInteger resolvedValue = table.resolveFunctionValue(functionName, argument);

        // THEN
        assertThat(resolvedValue).isNull();
    }

    @Test
    public void shouldComputeFunctionValueWhenFunctionExists() {
        // GIVEN
        String functionName = anyString();
        BigInteger argumentValue = any(BigInteger.class);
        BigInteger varValue = BigInteger.valueOf(anyInteger());
        Function<BigInteger, BigInteger> function = (Function<BigInteger, BigInteger>)mock(Function.class);
        when(function.apply(argumentValue)).thenReturn(varValue);

        SymbolTable table = new SymbolTable();

        // WHEN

        table.addFunction(functionName, function);
        BigInteger resolvedValue = table.resolveFunctionValue(functionName, argumentValue);

        // THEN
        assertThat(resolvedValue).isEqualTo(varValue);
    }

}
