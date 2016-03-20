package calc;

import java.math.BigInteger;
import java.util.Optional;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public interface TokenSource {
    Optional<Token> current();

    Token match(Token token);

    String matchIdentifier();

    BigInteger matchNumber();

    Operator matchOperator();
}
