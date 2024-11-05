package ast;

public class LogicalCond extends Cond {
    public static final int AND = 0;
    public static final int OR = 1;

    private final Cond left;
    private final int operator;
    private final Cond right;

    public LogicalCond(Cond left, int operator, Cond right, Location loc) {
        super(loc);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Cond getLeft() {
        return left;
    }

    public int getOperator() {
        return operator;
    }

    public Cond getRight() {
        return right;
    }

    @Override
    public String toString() {
        String s = null;
        switch (operator) {
            case AND:
                s = "&&";
                break;
            case OR:
                s = "||";
                break;
        }
        return left.toString() + " " + s + " " + right.toString();
    }
}
