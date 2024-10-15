package ast;

public class NeExprList extends ASTNode {
    final Expr expr;
    final NeExprList neExprList;

    public NeExprList(Expr expr, NeExprList neExprList, Location loc) {
        super(loc);
        this.expr = expr;
        this.neExprList = neExprList;
    }

    public Expr getExpr() {
        return expr;
    }

    public NeExprList getNeExprList() {
        return neExprList;
    }

    @Override
    public String toString() {
        return expr.toString() + ", " + neExprList.toString();
    }
}
