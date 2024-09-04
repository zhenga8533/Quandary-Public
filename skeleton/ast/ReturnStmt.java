package ast;

public class ReturnStmt extends Expr {

    final Expr expr;

    public ReturnStmt(Expr expr, Location loc) {
        // Not in use, found it easier to just change Parser.cup :)
        super(loc);
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return "return " + expr + ";";
    }
}
