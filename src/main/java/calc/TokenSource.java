package calc;

import java.math.BigInteger;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public interface TokenSource {
    boolean eol();

    Token current();

    boolean match(Token token);

    String matchIdentifier();

    BigInteger matchNumber();

    Operator matchOperator();
}
