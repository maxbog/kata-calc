package calc.Ast;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class Number extends Expression {
    private int value;

    public Number(int value) {

        this.value = value;
    }
}
