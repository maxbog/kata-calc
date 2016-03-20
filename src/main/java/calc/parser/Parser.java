package calc.parser;

import calc.TokenSource;

import java.util.function.Function;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
@FunctionalInterface
public interface Parser<T> extends Function<TokenSource, T> {
}
