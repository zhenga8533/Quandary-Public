package ast;

public class IdentExpr extends Expr {
    private final String name;

    public IdentExpr(String name, Location loc) {
        super(loc);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}