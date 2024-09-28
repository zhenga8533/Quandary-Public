package ast;

public class VarDecl extends ASTNode {

    final Type type;
    final String id;

    public VarDecl(Type type, String id, Location loc) {
        super(loc);
        this.type = type;
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return type + " " + id;
    }
}
