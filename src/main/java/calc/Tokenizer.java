package calc;

import fj.data.Array;
import fj.data.Collectors;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class Tokenizer {
    private static Map<Character, Operator> operatorMap = new HashMap<>();
    static {
        operatorMap.put('=', Operator.Assign);
        operatorMap.put('+', Operator.Plus);
        operatorMap.put('-', Operator.Minus);
        operatorMap.put('/', Operator.Divide);
        operatorMap.put('*', Operator.Times);
        operatorMap.put('^', Operator.Power);
        operatorMap.put('(', Operator.LeftParen);
        operatorMap.put(')', Operator.RightParen);
        operatorMap.put(':', Operator.Let);
        operatorMap.put(';', Operator.EndOfStatement);
    };

    public Array<Token> tokenize(String input) {
        ArrayList<Token> tokens = new ArrayList<Token>();
        TokenizerState state = new TokenizerState(input);
        while (!state.eol()) {
            if (operatorMap.containsKey(state.currentChar())) {
                tokens.add(Token.ofOperator(tokenizeSingleOperator(state)));
            } else if (Character.isDigit(state.currentChar())) {
                tokens.add(Token.ofNumber(tokenizeSingleNumber(state)));
            } else if (Character.isAlphabetic(state.currentChar())) {
                tokens.add(Token.ofIdentifier(tokenizeSingleIdentifier(state)));
            } else if (Character.isWhitespace(state.currentChar())) {
                state.advance();
            } else {
                return null;
            }
        }
        return tokens.stream().collect(Collectors.toArray());
    }

    private Operator tokenizeSingleOperator(TokenizerState state) {
        Operator operator = operatorMap.get(state.currentChar());
        state.advance();
        return operator;
    }

    private String tokenizeSingleIdentifier(TokenizerState state) {
        String currentIdentifier = "";

        while(!state.eol() && Character.isAlphabetic(state.currentChar())) {
            currentIdentifier += state.currentChar();
            state.advance();
        }
        return currentIdentifier;
    }

    private BigInteger tokenizeSingleNumber(TokenizerState state) {
        String currentNumber = "";

        while(!state.eol() && Character.isDigit(state.currentChar())) {
            currentNumber += state.currentChar();
            state.advance();
        }
        return new BigInteger(currentNumber);
    }

    private class TokenizerState {
        TokenizerState(String input) {
            this.input = input;
            this.currentIdx = 0;
        }

        String input;
        int currentIdx;

        char currentChar() {
            return input.charAt(currentIdx);
        }

        boolean eol() {
            return currentIdx >= input.length();
        }

        void advance() {
            ++currentIdx;
        }
    }
}
