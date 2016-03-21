package calc.parser;

import calc.ArrayTokenSource;
import calc.Operator;
import calc.Token;
import calc.TokenSource;
import calc.ast.*;
import fj.data.Array;
import fj.data.List;

import java.util.Optional;
import java.util.function.Supplier;

import static fj.data.List.list;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class FullParser {
    private NodeFactory nodeFactory;
    private LeftAssociativeExpressionParser addExpressionParser;
    private LeftAssociativeExpressionParser multExpressionParser;

    public FullParser(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
        multExpressionParser = new LeftAssociativeExpressionParser(new ExpressionCollector(this::parsePowerExpression, FullParser::matchMultiplicativeOperator), this.nodeFactory);
        addExpressionParser = new LeftAssociativeExpressionParser(new ExpressionCollector(this::parseMultExpression, FullParser::matchAdditiveOperator), this.nodeFactory);
    }


    private static Optional<Operator> matchMultiplicativeOperator(TokenSource source) {
        return source.current()
                .filter(FullParser::isMultiplicativeOperator)
                .flatMap(x -> source.matchOperator());
    }

    private static Optional<Operator> matchAdditiveOperator(TokenSource source) {
        return source.current()
                .filter(FullParser::isAdditiveOperator)
                .flatMap(x -> source.matchOperator());
    }

    public Optional<Program> parse(Array<Token> input) {
        TokenSource source = new ArrayTokenSource(input);
        return parseProgram(source);
    }

    private Optional<Program> parseProgram(TokenSource source) {
        return parseStatementList(source)
                .map(Program::new);
    }

    private Optional<List<Statement>> parseStatementList(TokenSource source) {
        return parseStatement(source)
                .map(stmt ->
                        parseStatementList(source)
                                .map(tail -> tail.cons(stmt))
                                .orElse(list(stmt)));
    }

    private Optional<Statement> parseStatement(TokenSource source) {
        return Optionals.firstOf(
                () -> parseLetStatement(source),
                () -> parseAddExpression(source)
                        .map(addExpression -> nodeFactory.createTopLevelStatement(addExpression))
        ).filter(stmt -> source.match(Token.ofOperator(Operator.EndOfStatement)).isPresent());
    }

    private Optional<Statement> parseLetStatement(TokenSource source) {
        return source.match(Token.ofOperator(Operator.Let))
                .flatMap(let -> parseVariableReference(source))
                .flatMap(ref-> source.match(Token.ofOperator(Operator.Assign))
                        .flatMap(assign -> parseAddExpression(source)
                                .map(expr -> nodeFactory.createVariableAssignment(ref, expr))
                ));
    }

    private Optional<Expression> parseAddExpression(TokenSource source) {
        return addExpressionParser.parseLeftAssociativeExpressionList(source);
    }

    private Optional<Expression> parseMultExpression(TokenSource source) {
        return multExpressionParser.parseLeftAssociativeExpressionList(source);
    }

    private Optional<Expression> parsePowerExpression(TokenSource source) {
        return parseUnaryExpression(source)
                .flatMap(firstExpression ->
                        Optionals.firstOf(
                            () -> parsePowerTailExpression(source)
                                    .map(tail -> nodeFactory.createBinaryExpression(Operator.Power, firstExpression, tail)),
                            () -> Optional.of(firstExpression)
                ));
    }

    // `^` power_expr
    private Optional<Expression> parsePowerTailExpression(TokenSource source) {
        return source.match(Token.ofOperator(Operator.Power))
                .flatMap(power -> parsePowerExpression(source));
    }

    private Optional<Expression> parseUnaryExpression(TokenSource source) {
        return Optionals.firstOf(
                () -> source.match(Token.ofOperator(Operator.Minus))
                    .flatMap(minus -> parseAtom(source))
                    .map(nodeFactory::createNegate),
                () -> {
                    source.match(Token.ofOperator(Operator.Plus));
                    return parseAtom(source);
                }
        );
    }

    private Optional<Expression> parseAtom(TokenSource source) {
        return Optionals.firstOf(
                () -> parseNumber(source),
                () -> parseIdentifier(source),
                () -> parseSubExpression(source));
    }

    private Optional<Expression> parseNumber(TokenSource source) {
        return source.matchNumber()
                .map(nodeFactory::createNumber);
    }

    private Optional<Expression> parseSubExpression(TokenSource source) {
        return source.match(Token.ofOperator(Operator.LeftParen))
                .flatMap(leftParen -> parseAddExpression(source))
                .filter(innerExpression -> source.match(Token.ofOperator(Operator.RightParen)).isPresent());
    }

    private Optional<Expression> parseIdentifier(TokenSource source) {
        return source.matchIdentifier()
                .flatMap(id ->
                        Optionals.firstOf(
                                () -> parseArgumentList(source, id),
                                () -> Optional.of(nodeFactory.createVariableReference(id))
                        ));
    }

    private Optional<Expression> parseArgumentList(TokenSource source, String id) {
        return source.match(Token.ofOperator(Operator.LeftParen))
                .flatMap(leftParen -> parseAddExpression(source))
                .filter(argument -> source.match(Token.ofOperator(Operator.RightParen)).isPresent())
                .map(argument -> nodeFactory.createFunctionCall(id, argument));
    }

    private static boolean isAdditiveOperator(Token token) {
        return token.equals(Token.ofOperator(Operator.Plus)) || token.equals(Token.ofOperator(Operator.Minus));
    }

    private static boolean isMultiplicativeOperator(Token token) {
        return token.equals(Token.ofOperator(Operator.Times)) || token.equals(Token.ofOperator(Operator.Divide));
    }

    private Optional<VariableReference> parseVariableReference(TokenSource source) {
        return source.matchIdentifier()
                .map(nodeFactory::createVariableReference);
    }

}

final class Optionals {
    @SafeVarargs
    static <T> Optional<T> firstOf(Supplier<Optional<T>>... alternatives) {
        for (Supplier<Optional<T>> alternative: alternatives) {
            final Optional<T> optional = alternative.get();
            if(optional.isPresent())
                return optional;
        }
        return Optional.empty();
    }
}
