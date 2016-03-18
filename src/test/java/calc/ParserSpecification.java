package calc;

import calc.ast.*;
import calc.ast.NumberExpression;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright 2016 Maksymilian Boguń.
 */
public class ParserSpecification {
    @Test(dataProvider = "parserDataProvider")
    public void shouldParseInputCorrectly(List<Token> tokens, Ast expectedParserOutput) {
        // GIVEN
        Parser parser = new Parser();

        // WHEN
        Ast ast = parser.parse(tokens);

        // THEN
        assertThat(ast).isEqualTo(expectedParserOutput);
    }

    @DataProvider
    public static Object[][] parserDataProvider() {
        Tokenizer lexer = new Tokenizer();
        return new Object[][] {
                {lexer.tokenize("1+2;"),
                        new Program(
                                new AddExpression(new NumberExpression(1), new NumberExpression(2)))},
                {lexer.tokenize(": x = 2; x*sqrt(8);"),
                        new Program(
                                new VariableAssignment(new VariableReference("x"), new NumberExpression(2)),
                                new MultiplyExpression(
                                        new VariableReference("x"),
                                        new FunctionCall("sqrt", new NumberExpression(8)))
                        )},
                {lexer.tokenize("1 / 0;"),
                        new Program(
                                new DivideExpression(new NumberExpression(1), new NumberExpression(0))
                        )},
                {lexer.tokenize(": x = sqrt(-5); : y = 7; x / y;"),
                        new Program(
                                new VariableAssignment(
                                        new VariableReference("x"),
                                        new FunctionCall("sqrt",
                                                new Negate(new NumberExpression(5)))),
                                new VariableAssignment(
                                        new VariableReference("y"),
                                        new NumberExpression(7)),
                                new DivideExpression(new VariableReference("x"), new VariableReference("y"))
                        )},
                {lexer.tokenize("1 + 2*3 - 4 * 5 / 6^7^8 ;"),
                        new Program(
                                new SubtractExpression(
                                        new AddExpression(
                                                new NumberExpression(1),
                                                new MultiplyExpression(new NumberExpression(2), new NumberExpression(3))
                                        ),
                                        new DivideExpression(
                                                new MultiplyExpression(new NumberExpression(4), new NumberExpression(5)),
                                                new PowerExpression(new NumberExpression(6),
                                                        new PowerExpression(new NumberExpression(7), new NumberExpression(8)))
                                        )
                                )
                        )},
        };
    }
}
