package calc;

import java.util.Objects;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class Token {
    private final String identifierName;
    private final TokenType type;
    private final int numberValue;
    private final Operator operatorValue;

    private Token(Operator operator) {
        this.type = TokenType.Operator;

        this.operatorValue = operator;
        this.identifierName = null;
        this.numberValue = 0;
    }

    private Token(int intValue) {
        this.type = TokenType.Number;

        this.operatorValue = null;
        this.identifierName = null;
        this.numberValue = intValue;
    }

    public Token(String identifierName) {
        this.type = TokenType.Identifier;

        this.operatorValue = null;
        this.identifierName = identifierName;
        this.numberValue = 0;
    }

    public TokenType getType() {
        return type;
    }

    public int numberValue() { return numberValue;}

    public Operator operatorValue() { return operatorValue;}


    public static Token ofNumber(int numberValue) {
        return new Token(numberValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return numberValue == token.numberValue &&
                type == token.type &&
                operatorValue == token.operatorValue &&
                Objects.equals(identifierName, token.identifierName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, numberValue, operatorValue, identifierName);
    }

    public static Token ofOperator(Operator operator) {
        return new Token(operator);
    }

    public static Token ofIdentifier(String identifierName) {
        return new Token(identifierName);
    }

    public String getIdentifierName() {
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
