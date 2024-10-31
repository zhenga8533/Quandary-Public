package ast;

public class ConstExpr extends Expr {

    final Long value;

    public ConstExpr(long value, Location loc) {
        super(loc);
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
