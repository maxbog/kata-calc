package calc.ast;

import calc.ValueAssigner;
import calc.ValueResolver;

import java.io.OutputStream;
import java.util.Objects;

public class PrintValue implements Statement {
    private OutputStream out;
    private Expression expression;

    public PrintValue(OutputStream out, Expression expression) {
        this.out = out;
        this.expression = expression;
    }

    @Override
    public void execute(ValueAssigner assigner, ValueResolver resolver) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintValue that = (PrintValue) o;
        return Objects.equals(out, that.out) &&
                Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(out, expression);
    }

    @Override
    public String toString() {
        return "[Print " + expression + "]";
    }
}
