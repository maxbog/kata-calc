package calc.ast;

import calc.ValueAssigner;
import calc.ValueResolver;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public interface Statement {

    void execute(ValueAssigner assigner, ValueResolver resolver);
}
