package calc.ast;

import calc.ValueAssigner;
import calc.ValueResolver;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class VariableAssignment implements Statement {
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

    @Override
    public void execute(ValueAssigner assigner, ValueResolver resolver) {

        BigInteger value = this.value.computeValue(resolver);
        if(value != null) {
            variableReference.assignValue(value, assigner);
        }
    }
}
