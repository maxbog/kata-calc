package calc.ast;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class Program {
    private List<Statement> statements;

    public Program(Statement... statements) {
        this.statements = Arrays.asList(statements);
    }
    public Program(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public String toString() {
        return "[Program \n" + String.join("\n", statements.stream().map(Statement::toString).collect(Collectors.toList())) +"\n]";
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
