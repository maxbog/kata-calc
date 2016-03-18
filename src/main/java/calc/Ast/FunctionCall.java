package calc.Ast;

import java.util.Objects;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class FunctionCall extends Expression {
    private final String functionName;
    private final Expression argument;

    public FunctionCall(String functionName, Expression argument) {

        this.functionName = functionName;
        this.argument = argument;
    }

    @Override
    public String toString() {
        return "[Call " + functionName + " " + argument + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionCall that = (FunctionCall) o;
        return Objects.equals(functionName, that.functionName) &&
                Objects.equals(argument, that.argument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionName, argument);
    }
}
