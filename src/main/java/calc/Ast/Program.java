package calc.Ast;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class Program implements Ast {
    private List<Statement> statements;

    public Program(Statement... statements) {
        this.statements = Arrays.asList(statements);
    }
}
