package calc.Ast;

import java.util.Objects;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class Number extends Expression {
    private int value;

    public Number(int value) {

        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Number number = (Number) o;
        return value == number.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "[Number " + value + "]";
    }
}
