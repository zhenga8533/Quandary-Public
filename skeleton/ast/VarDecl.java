package ast;

public class VarDecl extends ASTNode {

    final Type type;
    final String ident;

    public VarDecl(Type type, String ident, Location loc) {
        super(loc);
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
