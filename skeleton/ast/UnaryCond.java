package ast;

public class UnaryCond extends Cond {
    public static final int NOT = 0;

    private final int operator;
    private final Cond expr;

    public UnaryCond(int operator, Cond expr, Location loc) {
        super(loc);
        this.operator = operator;
        this.expr = expr;
    }

    public int getOperator() {
        return operator;
    }

    public Cond getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        String s = null;
        switch (operator) {
            case NOT: s = "!"; break;
        }
        return s + expr;
    }
}
