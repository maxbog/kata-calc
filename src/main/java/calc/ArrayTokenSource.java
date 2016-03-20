package calc;

import java.math.BigInteger;
import java.util.List;

public class ArrayTokenSource implements TokenSource {
    private int currentToken;
    private List<Token> input;

    public ArrayTokenSource(List<Token> input) {
        this.input = input;
        this.currentToken = 0;
    }

    @Override
    public boolean eol() {
        return currentToken >= input.size();
    }

    @Override
    public Token current() {
        return input.get(currentToken);
    }

    @Override
    public boolean match(Token token) {
        if(eol())
            return false;
        if(current().equals(token)) {
            advance();
            return true;
        }
        return false;
    }

    private void advance() {
        currentToken++;
    }

    @Override
    public String matchIdentifier() {
        if(eol())
            return null;
        Token current = current();
        if(current.getType() == TokenType.Identifier) {
            advance();
            return current.getIdentifierName();
        }
        return null;
    }

    @Override
    public BigInteger matchNumber() {
        if(eol())
            return null;
        Token current = current();
        if(current.getType() == TokenType.Number) {
            advance();
            return current.numberValue();
        }
        return null;
    }

    @Override
    public Operator matchOperator() {
        if(eol())
            return null;
        Token current = current();
        if(current.getType() == TokenType.Operator) {
            advance();
            return current.operatorValue();
        }
        return null;
    }
}
