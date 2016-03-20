package calc.parser;

import calc.Operator;
import calc.Token;
import calc.TokenSource;

import java.util.function.Function;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
@FunctionalInterface
public interface TokenMatcher extends Function<TokenSource, Operator> {

}
