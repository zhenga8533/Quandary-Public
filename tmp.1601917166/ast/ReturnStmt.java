package ast;

public class ReturnStmt extends Stmt {
    final Expr expr;

    public ReturnStmt(Expr expr, Location loc) {
        super(loc);
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return "return " + expr.toString() + ";";
    }
}
