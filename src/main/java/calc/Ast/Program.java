package calc.ast;

import fj.data.List;

import java.util.Objects;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class Program {
    private List<Statement> statements;

    public Program(Statement... statements) {
        this.statements = List.list(statements);
    }
    public Program(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public String toString() {
        return "[Program \n" + String.join("\n", statements.map(Statement::toString)) +"\n]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Program program = (Program) o;
        return Objects.equals(statements, program.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statements);
    }
}
