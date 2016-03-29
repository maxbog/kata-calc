package calc.parser;

import calc.ArrayTokenSource;
import calc.Operator;
import calc.Token;
import calc.TokenSource;
import calc.ast.*;
import fj.F;
import fj.Function;
import fj.data.Array;
import fj.data.Option;

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


    private static Option<Operator> tryMatchMultiplicativeOperator(TokenSource source) {
        return source.match(FullParser::isMultiplicativeOperator).map(Token::operatorValue);
    }

    private static Option<Operator> tryMatchAdditiveOperator(TokenSource source) {
        return source.match(FullParser::isAdditiveOperator).map(Token::operatorValue);
    }

    public Program parse(Array<Token> input) {
        TokenSource source = new ArrayTokenSource(input);
        return parseProgram(source);
    }

    private Program parseProgram(TokenSource source) {
        List<Statement> statements = new ArrayList<>();
        while (source.current().isSome()) {
            Statement statement = parseStatement(source);
            if(statement == null)
                return null;
            statements.add(statement);
        }

        return new Program(statements);
    }

    private Statement parseStatement(TokenSource source) {
        Statement parsedStatement;
        if(source.current().map(current-> current.equals(Token.ofOperator(Operator.Let))).orSome(false)) {
            parsedStatement = parseLetExpression(source);
        } else {
            parsedStatement = parseAddExpression(source).map(nodeFactory::createTopLevelStatement).toNull();
        }

        if(parsedStatement == null)
            return null;

        if(source.match(Token.ofOperator(Operator.EndOfStatement)).isNone())
            return null;

        return parsedStatement;
    }

    private VariableAssignment parseLetExpression(TokenSource source) {
        return source.match(Token.ofOperator(Operator.Let))
                .bind(let -> parseVariableReference(source))
                .bind(matchConstOperator(Operator.Assign, source))
                .bind(ref -> parseAddExpression(source).map(expr -> nodeFactory.createVariableAssignment(ref, expr))
                ).toNull();
    }

    private <T> F<T, Option<T>> matchConstOperator(Operator assign, TokenSource source) {
        return ref -> source.match(Token.ofOperator(assign)).map(Function.constant(ref));
    }

    private Option<Expression> parseAddExpression(TokenSource source) {
        return addExpressionParser.parseLeftAssociativeExpressionList(source);
    }

    private Option<Expression> parseMultExpression(TokenSource source) {
        return multExpressionParser.parseLeftAssociativeExpressionList(source);
    }

    private Option<Expression> parsePowerExpression(TokenSource source) {
        Expression firstExpression = parseUnaryExpression(source);
        if(firstExpression == null)
            return Option.none();
        Expression tail = parsePowerTailExpression(source);
        if(tail == null) {
            return Option.some(firstExpression);
        } else {
            return Option.some(nodeFactory.createBinaryExpression(Operator.Power, firstExpression, tail));
        }
    }

    // `^` power_expr
    private Expression parsePowerTailExpression(TokenSource source) {
        if(source.current().map(FullParser::isPowerOperator).orSome(false)) {
            source.matchOperator();
            return parsePowerExpression(source).toNull();
        }
        return null;
    }

    private Expression parseUnaryExpression(TokenSource source) {
        if(source.current().map(current -> current.equals(Token.ofOperator(Operator.Minus))).orSome(false)) {
            source.matchOperator();
            Expression expr = parseAtom(source);
            if(expr == null)
                return null;
            return nodeFactory.createNegate(expr);
        }

        if(source.current().map(current -> current.equals(Token.ofOperator(Operator.Plus))).orSome(false)) {
            source.matchOperator();
        }

        return parseAtom(source);
    }

    private Expression parseAtom(TokenSource source) {
        return source.matchNumber()
                .map(nodeFactory::createNumber)
                .orSome(() -> source.matchIdentifier()
                        .map(id ->
                                source.matchOperator(Operator.LeftParen)
                                .bind(leftParen -> parseAddExpression(source).map(argument -> {
                                    if (source.match(Token.ofOperator(Operator.RightParen)).isNone())
                                        return null;

                                    return nodeFactory.createFunctionCall(id, argument);
                                })).orSome(() -> nodeFactory.createVariableReference(id)))
                        .orSome(() -> {
                            if (source.current().map(current -> current.equals(Token.ofOperator(Operator.LeftParen))).orSome(false)) {
                                source.match(Token.ofOperator(Operator.LeftParen));
                                return parseAddExpression(source)
                                        .bind(innerExpression -> {
                                            if (source.match(Token.ofOperator(Operator.RightParen)).isNone())
                                                return Option.none();
                                            return Option.some(innerExpression);
                                        }).toNull();
                            }

                            return null;
                        }));
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

    private Option<VariableReference> parseVariableReference(TokenSource source) {
        return source.matchIdentifier()
                .map(nodeFactory::createVariableReference);
    }

}
