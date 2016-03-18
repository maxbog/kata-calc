package calc.Ast;

/**
 * Created by dvkc73 on 2016-03-18.
 */
public class PowerExpression extends BinaryExpression {
    public PowerExpression(Expression left, Expression right) {
        super(left, right);
    }


    @Override
    public String toString() {
        return "[Power " + getLeft() + " " + getRight() + "]";
    }
}
