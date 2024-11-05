package ast;

public class PrintStmt extends Stmt {
    final Expr expr;

    public PrintStmt(Expr expr, Location loc) {
        super(loc);
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return "print " + expr.toString() + ";";
    }
}
