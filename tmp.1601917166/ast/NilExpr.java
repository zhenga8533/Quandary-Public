package ast;

public class NilExpr extends Expr {
    public NilExpr(Location loc) {
        super(loc);
    }

    @Override
    public String toString() {
        return "nil";
    }
}
