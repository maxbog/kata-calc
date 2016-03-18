package calc;

import calc.ast.*;
import calc.ast.NumberExpression;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class Parser {
    public Ast parse(List<Token> input) {
        TokenSource source = new TokenSource(input);
        return parseProgram(source);
    }

    private Program parseProgram(TokenSource source) {
        List<Statement> statements = new ArrayList<>();
        while (!source.eol()) {
            Statement statement = parseStatement(source);
            if(statement == null)
                return null;
            statements.add(statement);
        }

        return new Program(statements);
    }

    private Statement parseStatement(TokenSource source) {
        Statement parsedStatement = null;
        if(!source.eol() && source.current().equals(Token.ofOperator(Operator.Let))) {
            parsedStatement =parseLetExpression(source);
        } else {
            parsedStatement = parseAddExpression(source);
        }

        if(parsedStatement == null)
            return null;

        if(!source.match(Token.ofOperator(Operator.EndOfStatement)))
            return null;

        return parsedStatement;
    }

    private VariableAssignment parseLetExpression(TokenSource source) {
        if(!source.match(Token.ofOperator(Operator.Let)))
            return null;

        VariableReference ref = parseVariableReference(source);

        if(ref == null)
            return null;

        if(!source.match(Token.ofOperator(Operator.Assign)))
            return null;

        Expression expr = parseAddExpression(source);

        if(expr == null)
            return null;

        return new VariableAssignment(ref, expr);
    }

    private Expression parseMultExpression(TokenSource source) {
        Expression topLevelExpression = parsePowerExpression(source);
        if(topLevelExpression == null)
            return null;

        while (!source.eol() && isMultiplicativeOperator(source.current())) {
            Operator oper = source.matchOperator();
            Expression nextExpr = parsePowerExpression(source);
            if(nextExpr == null)
                return null;
            topLevelExpression = createBinaryExpression(oper, topLevelExpression, nextExpr);
            if(topLevelExpression == null)
                return null;
        }
        return topLevelExpression;
    }

    private Expression createBinaryExpression(Operator oper, Expression left, Expression right) {
        switch (oper) {
            case Times:
                return new MultiplyExpression(left, right);
            case Divide:
                return new DivideExpression(left, right);
            case Plus:
                return new AddExpression(left, right);
            case Minus:
                return new SubtractExpression(left, right);
            case Power:
                return new PowerExpression(left, right);
        }
        return null;
    }

    private boolean isMultiplicativeOperator(Token token) {
        return token.equals(Token.ofOperator(Operator.Times)) || token.equals(Token.ofOperator(Operator.Divide));
    }

    private Expression parseAddExpression(TokenSource source) {
        Expression topLevelExpression = parseMultExpression(source);
        if(topLevelExpression == null)
            return null;

        while (!source.eol() && isAdditiveOperator(source.current())) {
            Operator oper = source.matchOperator();
            Expression nextExpr = parseMultExpression(source);
            if(nextExpr == null)
                return null;
            topLevelExpression = createBinaryExpression(oper, topLevelExpression, nextExpr);
            if(topLevelExpression == null)
                return null;
        }
        return topLevelExpression;
    }

    private Expression parsePowerExpression(TokenSource source) {
        Stack<Expression> exprStack = new Stack<>();
        Expression firstExpression = parseUnaryExpression(source);
        if(firstExpression == null)
            return null;
        exprStack.push(firstExpression);

        while (!source.eol() && isPowerOperator(source.current())) {
            source.matchOperator();
            Expression nextExpr = parseUnaryExpression(source);
            if(nextExpr == null)
                return null;
            exprStack.push(nextExpr);
        }
        Expression topLevelExpression = exprStack.pop();
        while (!exprStack.empty()) {
            Expression nextExpression = exprStack.pop();
            topLevelExpression = createBinaryExpression(Operator.Power, nextExpression, topLevelExpression);
        }
        return topLevelExpression;
    }

    private Expression parseUnaryExpression(TokenSource source) {
        if(!source.eol() && source.current().equals(Token.ofOperator(Operator.Minus))) {
            source.matchOperator();
            Expression expr = parseAtom(source);
            if(expr == null)
                return null;
            return new Negate(expr);
        }

        if(!source.eol() && source.current().equals(Token.ofOperator(Operator.Plus))) {
            source.matchOperator();
        }

        return parseAtom(source);
    }

    private Expression parseAtom(TokenSource source) {
        if(!source.eol() && source.current().getType() == TokenType.Number) {
            return new NumberExpression(source.matchNumber());
        }

        if(!source.eol() && source.current().getType() == TokenType.Identifier) {
            String id = source.matchIdentifier();

            if(!source.eol() && source.current().equals(Token.ofOperator(Operator.LeftParen))) {
                source.match(Token.ofOperator(Operator.LeftParen));

                Expression argument = parseAddExpression(source);

                if(argument == null)
                    return null;

                if(!source.match(Token.ofOperator(Operator.RightParen)))
                    return null;

                return new FunctionCall(id, argument);
            }

            return new VariableReference(id);
        }

        if(!source.eol() && source.current().equals(Token.ofOperator(Operator.LeftParen))) {
            source.match(Token.ofOperator(Operator.LeftParen));

            Expression innerExpression = parseAddExpression(source);

            if(innerExpression == null)
                return null;

            if(!source.match(Token.ofOperator(Operator.RightParen)))
                return null;
            return innerExpression;
        }

        return null;
    }

    private boolean isAdditiveOperator(Token token) {
        return token.equals(Token.ofOperator(Operator.Plus)) || token.equals(Token.ofOperator(Operator.Minus));
    }

    private boolean isPowerOperator(Token token) {
        return token.equals(Token.ofOperator(Operator.Power));
    }

    private VariableReference parseVariableReference(TokenSource source) {
        String id = source.matchIdentifier();

        if(id == null)
            return null;

        return new VariableReference(id);
    }

    private class TokenSource {
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
}
