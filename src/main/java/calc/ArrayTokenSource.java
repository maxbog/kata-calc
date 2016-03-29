package calc;

import fj.F;
import fj.data.Array;
import fj.data.Option;

import java.math.BigInteger;

public class ArrayTokenSource implements TokenSource {
    private int currentToken;
    private Array<Token> input;

    public ArrayTokenSource(Array<Token> input) {
        this.input = input;
        this.currentToken = 0;
    }


    @Override
    public Option<Token> current() {
        if(eol())
            return Option.none();
        return Option.some(input.get(currentToken));
    }

    @Override
    public Option<Token> match(Token token) {
        return match(current -> current.equals(token));
    }

    @Override
    public Option<Token> match(F<Token, Boolean> matcher) {
        return current()
                .filter(matcher)
                .map(this::advanceAndReturn);
    }

    @Override
    public Option<String> matchIdentifier() {
        return match(current -> current.getType() == TokenType.Identifier)
                .map(Token::identifierName);
    }

    @Override
    public Option<BigInteger> matchNumber() {
        return match(current -> current.getType() == TokenType.Number)
                .map(Token::numberValue);
    }

    @Override
    public Option<Operator> matchOperator() {
        return match(current -> current.getType() == TokenType.Operator)
                .map(Token::operatorValue);
    }
    @Override
    public Option<Operator> matchOperator(Operator expectedOperator) {
        return match(current -> current.getType() == TokenType.Operator && current.operatorValue() == expectedOperator)
                .map(Token::operatorValue);
    }

    private boolean eol() {
        return currentToken >= input.length();
    }
    private <T> T advanceAndReturn(T value) {
        currentToken++;
        return value;
    }
}
