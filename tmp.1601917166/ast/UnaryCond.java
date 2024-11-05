package ast;

public class UnaryCond extends Cond {
    public static final int NOT = 0;

    private final int operator;
    private final Cond cond;

    public UnaryCond(int operator, Cond cond, Location loc) {
        super(loc);
        this.operator = operator;
        this.cond = cond;
    }

    public int getOperator() {
        return operator;
    }

    public Cond getCond() {
        return cond;
    }

    @Override
    public String toString() {
        String s = null;
        switch (operator) {
            case NOT:
                s = "!";
                break;
        }
        return s + cond.toString();
    }
}
