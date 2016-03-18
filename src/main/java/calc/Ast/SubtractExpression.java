package calc.Ast;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public class SubtractExpression extends BinaryExpression {
    public SubtractExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return "[Subtract " + getLeft() + " " + getRight() + "]";
    }
}
