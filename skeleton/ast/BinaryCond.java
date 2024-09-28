package ast;

public class BinaryCond extends Cond {
    public static final int LE = 0;
    public static final int GE = 1;
    public static final int EQ = 2;
    public static final int NE = 3;
    public static final int LT = 4;
    public static final int GT = 5;

    private final Expr left;
    private final int operator;
    private final Expr right;

    public BinaryCond(Expr left, int operator, Expr right, Location loc) {
        super(loc);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expr getLeft() {
        return left;
    }

    public int getOperator() {
        return operator;
    }

    public Expr getRight() {
        return right;
    }

    @Override
    public String toString() {
        String s = null;
        switch (operator) {
            case LE: s = "<="; break;
            case GE: s = ">="; break;
            case EQ: s = "=="; break;
            case NE: s = "!="; break;
            case LT: s = "<"; break;
            case GT: s = ">"; break;
        }
        return left + " " + s + " " + right;
    }
}
