package calc.ast;

import calc.ValueResolver;

import java.math.BigInteger;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public interface Expression extends Statement {
    BigInteger computeValue(ValueResolver valueResolver);
}
