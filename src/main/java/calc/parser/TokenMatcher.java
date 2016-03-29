package calc.parser;

import calc.Operator;
import calc.TokenSource;
import fj.data.Option;

import java.util.function.Function;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
@FunctionalInterface
public interface TokenMatcher extends Function<TokenSource, Option<Operator>> {

}
