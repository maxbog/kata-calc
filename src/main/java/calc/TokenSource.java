package calc;

import java.math.BigInteger;
import java.util.List;

public class TokenSource {
    private int currentToken;
    private List<Token> input;

    public TokenSource(List<Token> input) {
        this.input = input;
        this.currentToken = 0;
    }

    public boolean eol() {
        return currentToken >= input.size();
    }

    public Token current() {
        return input.get(currentToken);
    }

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
