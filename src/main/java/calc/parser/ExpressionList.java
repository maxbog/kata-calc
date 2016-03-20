package calc.parser;

import calc.ast.Expression;

import java.util.List;
import java.util.Objects;

public class ExpressionList {
    Expression firstExpression;
    List<Operation> nextExpressions;

    public ExpressionList(Expression firstExpression, List<Operation> nextExpressions) {
        this.nextExpressions = nextExpressions;
        this.firstExpression = firstExpression;
    }

    public List<Operation> getNextOperations() {
        return nextExpressions;
    }

    public Expression getFirstExpression() {
        return firstExpression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpressionList that = (ExpressionList) o;
        return Objects.equals(firstExpression, that.firstExpression) &&
                Objects.equals(nextExpressions, that.nextExpressions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstExpression, nextExpressions);
    }
}
