package calc;

import fj.data.Array;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class TokenizerSpecification {

    @DataProvider(name = "numberTokenizerDataProvider")
    public Object[][] numberTokenizerDataProvider() {
        return new Object[][]{
            {"234", 234},
            {"0", 0}
        };
    }

    @DataProvider(name = "operatorTokenizerDataProvider")
    public Object[][] operatorTokenizerDataProvider() {
        return new Object[][]{
                {"=", Operator.Assign},
                {"(", Operator.LeftParen},
                {")", Operator.RightParen},
                {"-", Operator.Minus},
                {"+", Operator.Plus},
                {"*", Operator.Times},
                {"/", Operator.Divide},
                {"^", Operator.Power},
                {":", Operator.Let},
                {";", Operator.EndOfStatement}
        };
    }

    @DataProvider(name = "tokenStringsDataProvider")
    public Object[][] tokenStringsDataProvider() {
        return new Object[][]{
                {"12+15", new Token[] {
                        Token.ofNumber(12),
                        Token.ofOperator(Operator.Plus),
                        Token.ofNumber(15)
                }},
                {"8^sqrt(64)", new Token[] {
                        Token.ofNumber(8),
                        Token.ofOperator(Operator.Power),
                        Token.ofIdentifier("sqrt"),
                        Token.ofOperator(Operator.LeftParen),
                        Token.ofNumber(64),
                        Token.ofOperator(Operator.RightParen)
                }},
                {": x = 7*3/-2", new Token[] {
                        Token.ofOperator(Operator.Let),
                        Token.ofIdentifier("x"),
                        Token.ofOperator(Operator.Assign),
                        Token.ofNumber(7),
                        Token.ofOperator(Operator.Times),
                        Token.ofNumber(3),
                        Token.ofOperator(Operator.Divide),
                        Token.ofOperator(Operator.Minus),
                        Token.ofNumber(2)
                }},
                {"6*(6+6)", new Token[] {
                        Token.ofNumber(6),
                        Token.ofOperator(Operator.Times),
                        Token.ofOperator(Operator.LeftParen),
                        Token.ofNumber(6),
                        Token.ofOperator(Operator.Plus),
                        Token.ofNumber(6),
                        Token.ofOperator(Operator.RightParen)
                }},
        };
    }

    @Test(dataProvider = "numberTokenizerDataProvider")
    public void shouldTokenizeIntegersAsNumbers(String input, int lexedNumber) {
        // GIVEN
        Tokenizer lexer = new Tokenizer();

        // WHEN
        Array<Token> tokens = lexer.tokenize(input);

        // THEN
        assertThat(tokens.get(0)).isEqualTo(Token.ofNumber(lexedNumber));
    }

    @Test(dataProvider = "operatorTokenizerDataProvider")
    public void shouldTokenizeOperatorsAsSuch(String input, Operator operator) {
        // GIVEN
        Tokenizer lexer = new Tokenizer();

        // WHEN
        Array<Token> tokens = lexer.tokenize(input);

        // THEN
        assertThat(tokens.get(0)).isEqualTo(Token.ofOperator(operator));
    }

    @Test(dataProvider = "tokenStringsDataProvider")
    public void shouldProperlyTokenizeStrings(String input, Token[] expectedTokens) {
        // GIVEN
        Tokenizer lexer = new Tokenizer();

        // WHEN
        Array<Token> tokens = lexer.tokenize(input);

        // THEN
        assertThat(tokens).containsExactlyElementsOf(Arrays.asList(expectedTokens));
    }
}
