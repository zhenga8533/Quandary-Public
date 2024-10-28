package ast;

public class VarDecl extends ASTNode {

    final Type type;
    final String ident;
    final boolean mutable;

    public VarDecl(boolean mutable, Type type, String ident, Location loc) {
        super(loc);
        this.mutable = mutable;
        this.type = type;
        this.ident = ident;
    }

    public Type getType() {
        return type;
    }

    public String getIdent() {
        return ident;
    }

    @Override
    public String toString() {
        return type.toString() + " " + ident;
    }
}
