package calc.ast;

import java.math.BigInteger;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public interface NodeFactory extends BinaryExpressionFactory{
    Expression createNumber(BigInteger value);

    VariableReference createVariableReference(String id);

    Expression createFunctionCall(String id, Expression argument);

    Negate createNegate(Expression expr);

    VariableAssignment createVariableAssignment(VariableReference ref, Expression expr);

    PrintValue createTopLevelStatement(Expression addExpression);
}
