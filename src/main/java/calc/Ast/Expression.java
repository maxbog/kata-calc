package calc.ast;

import calc.ValueAssigner;
import calc.ValueResolver;

import java.math.BigInteger;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public interface Expression {
    BigInteger computeValue(ValueResolver valueResolver);
}

