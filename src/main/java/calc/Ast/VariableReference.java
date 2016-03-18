package calc.Ast;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class VariableReference extends Expression {
    private String variableName;

    public VariableReference(String variableName) {

        this.variableName = variableName;
    }
}
