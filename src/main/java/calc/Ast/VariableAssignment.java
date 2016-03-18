package calc.Ast;

import java.util.Objects;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class VariableAssignment extends Statement {
    private final VariableReference variableReference;
    private final Expression value;

    public VariableAssignment(VariableReference variableReference, Expression value) {

        this.variableReference = variableReference;
        this.value = value;
    }


    @Override
    public String toString() {
        return "[Assign " + variableReference + " " + value + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableAssignment that = (VariableAssignment) o;
        return Objects.equals(variableReference, that.variableReference) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variableReference, value);
    }
}
