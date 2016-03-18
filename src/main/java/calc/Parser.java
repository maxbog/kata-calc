package calc;

import calc.ast.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class Parser {
    private NodeFactory nodeFactory;

    public Parser(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }

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
            parsedStatement = parseLetExpression(source);
        } else {
            Expression addExpression = parseAddExpression(source);
            parsedStatement = nodeFactory.createTopLevelStatement(addExpression);
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

        return nodeFactory.createVariableAssignment(ref, expr);
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
            topLevelExpression = nodeFactory.createBinaryExpression(oper, topLevelExpression, nextExpr);
            if(topLevelExpression == null)
                return null;
        }
        return topLevelExpression;
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
            topLevelExpression = nodeFactory.createBinaryExpression(oper, topLevelExpression, nextExpr);
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
            topLevelExpression = nodeFactory.createBinaryExpression(Operator.Power, nextExpression, topLevelExpression);
        }
        return topLevelExpression;
    }

    private Expression parseUnaryExpression(TokenSource source) {
        if(!source.eol() && source.current().equals(Token.ofOperator(Operator.Minus))) {
            source.matchOperator();
            Expression expr = parseAtom(source);
            if(expr == null)
                return null;
            return nodeFactory.createNegate(expr);
        }

        if(!source.eol() && source.current().equals(Token.ofOperator(Operator.Plus))) {
            source.matchOperator();
        }

        return parseAtom(source);
    }

    private Expression parseAtom(TokenSource source) {
        if(!source.eol() && source.current().getType() == TokenType.Number) {
            return nodeFactory.createNumber(source.matchNumber());
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

                return nodeFactory.createFunctionCall(id, argument);
            }

            return nodeFactory.createVariableReference(id);
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

        return nodeFactory.createVariableReference(id);
    }

}
