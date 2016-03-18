package calc.symboltable;

import calc.ValueAssigner;
import calc.ValueResolver;
import calc.ast.Expression;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public class SymbolTable implements ValueAssigner, ValueResolver {
    private Map<String, BigInteger> variables = new HashMap<>();
    private Map<String, Function<BigInteger, BigInteger>> functions = new HashMap<>();

    @Override
    public void assignValue(String varName, BigInteger value) {
        variables.put(varName, value);
    }

    @Override
    public BigInteger resolveVariable(String varName) {
        return variables.getOrDefault(varName, null);
    }

    @Override
    public BigInteger resolveFunctionValue(String functionName, BigInteger argument) {
        Function<BigInteger, BigInteger> function = functions.getOrDefault(functionName, null);
        if(function == null)
            return null;
        return function.apply(argument);
    }

    public void addFunction(String functionName, Function<BigInteger, BigInteger> function) {
        functions.put(functionName, function);
    }
}
