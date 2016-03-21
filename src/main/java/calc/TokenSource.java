package calc;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public interface TokenSource {
    Optional<Token> current();

    Optional<Token> match(Token token);

    Optional<String> matchIdentifier();

    Optional<BigInteger> matchNumber();

    Optional<Operator> matchOperator();
}
