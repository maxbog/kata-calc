package calc;


import fj.data.Array;

import java.math.BigInteger;
import java.util.Optional;

public class ArrayTokenSource implements TokenSource {
    private int currentToken;
    private Array<Token> input;

    public ArrayTokenSource(Array<Token> input) {
        this.input = input;
        this.currentToken = 0;
    }


    @Override
    public Optional<Token> current() {
        if(eol())
            return Optional.empty();
        return Optional.of(input.get(currentToken));
    }

    @Override
    public Optional<Token> match(Token token) {
        return current()
                .filter(current -> current.equals(token))
                .map(this::advanceAndReturn);
    }

    @Override
    public Optional<String> matchIdentifier() {
        return current()
                .filter(current -> current.getType() == TokenType.Identifier)
                .map(current -> advanceAndReturn(current.identifierName()));
    }

    @Override
    public Optional<BigInteger> matchNumber() {
        return current()
                .filter(current -> current.getType() == TokenType.Number)
                .map(current -> advanceAndReturn(current.numberValue()));
    }

    @Override
    public Optional<Operator> matchOperator() {
        return current()
                .filter(current -> current.getType() == TokenType.Operator)
                .map(current -> advanceAndReturn(current.operatorValue()));
    }

    private boolean eol() {
        return currentToken >= input.length();
    }
    private <T> T advanceAndReturn(T value) {
        currentToken++;
        return value;
    }
}
