package calc.ast;

import calc.Operator;

import java.io.OutputStream;
import java.math.BigInteger;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public class PrintingNodeFactory implements NodeFactory {

    private OutputStream out;

    public PrintingNodeFactory(OutputStream out) {
        this.out = out;
    }

    public Expression createBinaryExpression(Operator oper, Expression left, Expression right) {
        switch (oper) {
            case Times:
                return new MultiplyExpression(left, right);
            case Divide:
                return new DivideExpression(left, right);
            case Plus:
                return new AddExpression(left, right);
            case Minus:
                return new SubtractExpression(left, right);
            case Power:
                return new PowerExpression(left, right);
        }
        return null;
    }

    @Override
    public Expression createNumber(BigInteger value) {
        return new NumberExpression(value);
    }

    @Override
    public VariableReference createVariableReference(String id) {
        return new VariableReference(id);
    }

    @Override
    public FunctionCall createFunctionCall(String id, Expression argument) {
        return new FunctionCall(id, argument);
    }

    @Override
    public Negate createNegate(Expression expr) {
        return new Negate(expr);
    }

    @Override
    public VariableAssignment createVariableAssignment(VariableReference ref, Expression expr) {
        return new VariableAssignment(ref, expr);
    }

    @Override
    public PrintValue createTopLevelStatement(Expression addExpression) {
        return new PrintValue(out, addExpression);
    }
}
