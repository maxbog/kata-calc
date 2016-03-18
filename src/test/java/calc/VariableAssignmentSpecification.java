package calc;

import calc.ast.Expression;
import calc.ast.VariableAssignment;
import calc.ast.VariableReference;
import org.testng.annotations.Test;

import java.math.BigInteger;

import static autofixture.publicinterface.Generate.any;
import static autofixture.publicinterface.Generate.anyInteger;
import static autofixture.publicinterface.Generate.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public class VariableAssignmentSpecification {
    @Test
    public void shouldAssignVariableValueWhenExecuted() {
        // GIVEN
        BigInteger value = BigInteger.valueOf(anyInteger());
        String varName = anyString();
        VariableReference ref = new VariableReference(varName);

        ValueResolver resolver = any(ValueResolver.class);
        Expression expr = mock(Expression.class);
        when(expr.computeValue(resolver)).thenReturn(value);

        ValueAssigner assigner = mock(ValueAssigner.class);

        VariableAssignment assignment = new VariableAssignment(ref, expr);

        // WHEN
        assignment.execute(assigner, resolver);

        // THEN
        verify(assigner).assignValue(varName, value);
    }
}
