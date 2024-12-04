package ast;

public class ConcurrentExpr extends Expr {
    private final BinaryExpr expr;

    public ConcurrentExpr(BinaryExpr expr, Location loc) {
        super(loc);
        this.expr = expr;
    }

    public BinaryExpr getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return expr.toString();
    }
}