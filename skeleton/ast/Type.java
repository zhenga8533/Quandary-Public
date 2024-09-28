package ast;

public class Type {
    public static final int INT = 0;
    public static final int REF = 1;
    public static final int SUPERTYPE = 2;

    private final int type;

    public Type(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        String s = null;
        switch (type) {
            case INT: s = "Int"; break;
            case REF: s = "Ref"; break;
            case SUPERTYPE: s = "Q"; break;
        }
        return s;
    }
}
