package calc.ast;

import calc.ValueAssigner;
import calc.ValueResolver;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public interface Statement extends Ast {

    void execute(ValueAssigner assigner, ValueResolver resolver);
}
