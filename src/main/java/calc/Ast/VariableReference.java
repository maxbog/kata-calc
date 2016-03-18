package calc.Ast;

import java.util.Objects;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class VariableReference extends Expression {
    private String variableName;

    public VariableReference(String variableName) {

        this.variableName = variableName;
    }

    @Override
    public String toString() {
        return "[Var " + variableName + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableReference that = (VariableReference) o;
        return Objects.equals(variableName, that.variableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variableName);
    }
}
