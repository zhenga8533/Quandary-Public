package ast;

public class NeExprList extends ASTNode {
    final Expr expr;
    final NeExprList neExprList;

    public NeExprList(Expr expr, NeExprList neExprList, Location loc) {
        super(loc);
        this.expr = expr;
        this.neExprList = neExprList;
    }

    public int size() {
        if (neExprList == null) {
            return 1;
        } else {
            return 1 + neExprList.size();
        }
    }

    public Expr getExpr() {
        return expr;
    }

    public NeExprList getNeExprList() {
        return neExprList;
    }

    @Override
    public String toString() {
        if (neExprList == null)
            return expr.toString();
        return expr.toString() + ", " + neExprList.toString();
    }
}
