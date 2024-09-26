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
        switch (type) {
            case INT: return "int";
            case REF: return "Ref";
            case SUPERTYPE: return "Q";
        }
    }
}
