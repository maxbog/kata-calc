package calc;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class Token {
    private final String identifierName;
    private final TokenType type;
    private final BigInteger numberValue;
    private final Operator operatorValue;

    private Token(Operator operator) {
        this.type = TokenType.Operator;

        this.operatorValue = operator;
        this.identifierName = null;
        this.numberValue = null;
    }

    private Token(BigInteger intValue) {
        this.type = TokenType.Number;

        this.operatorValue = null;
        this.identifierName = null;
        this.numberValue = intValue;
    }

    private Token(String identifierName) {
        this.type = TokenType.Identifier;

        this.operatorValue = null;
        this.identifierName = identifierName;
        this.numberValue = null;
    }

    public TokenType getType() {
        return type;
    }

    public BigInteger numberValue() { return numberValue;}

    public Operator operatorValue() { return operatorValue;}


    public static Token ofNumber(BigInteger numberValue) {
        return new Token(numberValue);
    }
    public static Token ofNumber(int numberValue) {
        return new Token(BigInteger.valueOf(numberValue));
    }

    public static Token ofOperator(Operator operator) {
        return new Token(operator);
    }

    public static Token ofIdentifier(String identifierName) {
        return new Token(identifierName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(numberValue, token.numberValue) &&
                type == token.type &&
                operatorValue == token.operatorValue &&
                Objects.equals(identifierName, token.identifierName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, numberValue, operatorValue, identifierName);
    }

    public String identifierName() {
        return identifierName;
    }

    @Override
    public String toString() {
        switch(type) {
            case Identifier:
                return "Token{type=identifier, id="+identifierName+"}";
            case Number:
                return "Token{type=number, number="+numberValue+"}";
            case Operator:
                return "Token{type=operator, oper="+operatorValue+"}";
        }
        return "Token{unknown}";
    }
}
