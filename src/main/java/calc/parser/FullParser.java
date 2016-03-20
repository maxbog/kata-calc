package calc.parser;

import calc.*;
import calc.ast.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class FullParser {
    private NodeFactory nodeFactory;
    private LeftAssociativeExpressionParser addExpressionParser;
    private LeftAssociativeExpressionParser multExpressionParser;

    public FullParser(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
        multExpressionParser = new LeftAssociativeExpressionParser(new ExpressionCollector(this::parsePowerExpression, FullParser::tryMatchMultiplicativeOperator), this.nodeFactory);
        addExpressionParser = new LeftAssociativeExpressionParser(new ExpressionCollector(this::parseMultExpression, FullParser::tryMatchAdditiveOperator), this.nodeFactory);
    }


    private static Operator tryMatchMultiplicativeOperator(TokenSource source) {
        final boolean operatorMatches = source.current().map(FullParser::isMultiplicativeOperator).orElse(false);
        if(operatorMatches) {
            return source.matchOperator();
        }
        return null;
    }

    private static Operator tryMatchAdditiveOperator(TokenSource source) {
        final boolean operatorMatches = source.current().map(FullParser::isAdditiveOperator).orElse(false);
        if(operatorMatches) {
            return source.matchOperator();
        }
        return null;
    }

    public Program parse(List<Token> input) {
        TokenSource source = new ArrayTokenSource(input);
        return parseProgram(source);
    }

    private Program parseProgram(TokenSource source) {
        List<Statement> statements = new ArrayList<>();
        while (source.current().isPresent()) {
            Statement statement = parseStatement(source);
            if(statement == null)
                return null;
            statements.add(statement);
        }

        return new Program(statements);
    }

    private Statement parseStatement(TokenSource source) {
        Statement parsedStatement;
        if(source.current().map(current-> current.equals(Token.ofOperator(Operator.Let))).orElse(false)) {
            parsedStatement = parseLetExpression(source);
        } else {
            Expression addExpression = parseAddExpression(source);
            parsedStatement = nodeFactory.createTopLevelStatement(addExpression);
        }

        if(parsedStatement == null)
            return null;

        if(source.match(Token.ofOperator(Operator.EndOfStatement)) == null)
            return null;

        return parsedStatement;
    }

    private VariableAssignment parseLetExpression(TokenSource source) {
        if(source.match(Token.ofOperator(Operator.Let)) == null)
            return null;

        VariableReference ref = parseVariableReference(source);

        if(ref == null)
            return null;

        if(source.match(Token.ofOperator(Operator.Assign)) == null)
            return null;

        Expression expr = parseAddExpression(source);

        if(expr == null)
            return null;

        return nodeFactory.createVariableAssignment(ref, expr);
    }

    private Expression parseAddExpression(TokenSource source) {
        return addExpressionParser.parseLeftAssociativeExpressionList(source);
    }

    private Expression parseMultExpression(TokenSource source) {
        return multExpressionParser.parseLeftAssociativeExpressionList(source);
    }

    private Expression parsePowerExpression(TokenSource source) {
        Expression firstExpression = parseUnaryExpression(source);
        if(firstExpression == null)
            return null;
        Expression tail = parsePowerTailExpression(source);
        if(tail == null) {
            return firstExpression;
        } else {
            return nodeFactory.createBinaryExpression(Operator.Power, firstExpression, tail);
        }
    }

    // `^` power_expr
    private Expression parsePowerTailExpression(TokenSource source) {
        if(source.current().map(FullParser::isPowerOperator).orElse(false)) {
            source.matchOperator();
            return parsePowerExpression(source);
        }
        return null;
    }

    private Expression parseUnaryExpression(TokenSource source) {
        if(source.current().map(current -> current.equals(Token.ofOperator(Operator.Minus))).orElse(false)) {
            source.matchOperator();
            Expression expr = parseAtom(source);
            if(expr == null)
                return null;
            return nodeFactory.createNegate(expr);
        }

        if(source.current().map(current -> current.equals(Token.ofOperator(Operator.Plus))).orElse(false)) {
            source.matchOperator();
        }

        return parseAtom(source);
    }

    private Expression parseAtom(TokenSource source) {
        if(source.current().map(current -> current.getType() == TokenType.Number).orElse(false)) {
            return nodeFactory.createNumber(source.matchNumber());
        }

        if(source.current().map(current -> current.getType() == TokenType.Identifier).orElse(false)) {
            String id = source.matchIdentifier();

            if(source.current().map(current -> current.equals(Token.ofOperator(Operator.LeftParen))).orElse(false)) {
                source.match(Token.ofOperator(Operator.LeftParen));

                Expression argument = parseAddExpression(source);

                if(argument == null)
                    return null;

                if(source.match(Token.ofOperator(Operator.RightParen)) == null)
                    return null;

                return nodeFactory.createFunctionCall(id, argument);
            }

            return nodeFactory.createVariableReference(id);
        }

        if(source.current().map(current -> current.equals(Token.ofOperator(Operator.LeftParen))).orElse(false)) {
            source.match(Token.ofOperator(Operator.LeftParen));

            Expression innerExpression = parseAddExpression(source);

            if(innerExpression == null)
                return null;

            if(source.match(Token.ofOperator(Operator.RightParen)) == null)
                return null;
            return innerExpression;
        }

        return null;
    }

    private static boolean isAdditiveOperator(Token token) {
        return token.equals(Token.ofOperator(Operator.Plus)) || token.equals(Token.ofOperator(Operator.Minus));
    }

    private static boolean isMultiplicativeOperator(Token token) {
        return token.equals(Token.ofOperator(Operator.Times)) || token.equals(Token.ofOperator(Operator.Divide));
    }

    private static boolean isPowerOperator(Token token) {
        return token.equals(Token.ofOperator(Operator.Power));
    }

    private VariableReference parseVariableReference(TokenSource source) {
        String id = source.matchIdentifier();

        if(id == null)
            return null;

        return nodeFactory.createVariableReference(id);
    }

}
