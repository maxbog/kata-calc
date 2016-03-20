package calc;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public class ArrayTokenSource implements TokenSource {
    private int currentToken;
    private List<Token> input;

    public ArrayTokenSource(List<Token> input) {
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
    public Token match(Token token) {
        return current()
                .filter(current -> current.equals(token))
                .map(this::advanceAndReturn)
                .orElse(null);
    }

    @Override
    public String matchIdentifier() {
        return current()
                .filter(current -> current.getType() == TokenType.Identifier)
                .map(current -> advanceAndReturn(current.identifierName()))
                .orElse(null);
    }

    @Override
    public BigInteger matchNumber() {
        return current()
                .filter(current -> current.getType() == TokenType.Number)
                .map(current -> advanceAndReturn(current.numberValue()))
                .orElse(null);
    }

    @Override
    public Operator matchOperator() {
        return current()
                .filter(current -> current.getType() == TokenType.Operator)
                .map(current -> advanceAndReturn(current.operatorValue()))
                .orElse(null);
    }

    private boolean eol() {
        return currentToken >= input.size();
    }
    private <T> T advanceAndReturn(T value) {
        currentToken++;
        return value;
    }
}
