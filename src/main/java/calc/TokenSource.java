package calc;

import fj.F;
import fj.data.Option;

import java.math.BigInteger;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public interface TokenSource {
    Option<Token> current();

    Option<Token> match(Token token);

    Option<Token> match(F<Token, Boolean> token);

    Option<String> matchIdentifier();

    Option<BigInteger> matchNumber();

    Option<Operator> matchOperator();
    Option<Operator> matchOperator(Operator expectedOperator);
}
