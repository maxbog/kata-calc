package calc.ast;

import calc.ValueResolver;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class NumberExpression implements Expression {
    private BigInteger value;

    public NumberExpression(BigInteger value) {
        this.value = value;
    }
    public NumberExpression(int value) {
        this.value = BigInteger.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberExpression number = (NumberExpression) o;
        return Objects.equals(value,number.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "[Number " + value + "]";
    }

    @Override
    public BigInteger computeValue(ValueResolver valueResolver) {
        return value;
    }
}
