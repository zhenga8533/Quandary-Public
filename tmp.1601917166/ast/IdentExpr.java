package ast;

public class IdentExpr extends Expr {
    private final String ident;

    public IdentExpr(String ident, Location loc) {
        super(loc);
        this.ident = ident;
    }

    public String getIdent() {
        return ident;
    }

    @Override
    public String toString() {
        return ident;
    }
}