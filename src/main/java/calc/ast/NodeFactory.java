package calc.ast;

import calc.Operator;
import calc.TokenSource;

import java.math.BigInteger;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public interface NodeFactory {
    Expression createBinaryExpression(Operator oper, Expression left, Expression right);

    NumberExpression createNumber(BigInteger value);

    VariableReference createVariableReference(String id);

    FunctionCall createFunctionCall(String id, Expression argument);

    Negate createNegate(Expression expr);

    VariableAssignment createVariableAssignment(VariableReference ref, Expression expr);

    PrintValue createTopLevelStatement(Expression addExpression);
}
