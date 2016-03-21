package calc.util;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public final class Optionals {
    @SafeVarargs
    public static <T> Optional<T> firstOf(Supplier<Optional<T>>... alternatives) {
        for(Supplier<Optional<T>> alternative : alternatives) {
            final Optional<T> optional = alternative.get();
            if(optional.isPresent())
                return optional;
        }
        return Optional.empty();
    }
}
