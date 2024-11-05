package ast;

public class FormalDeclList extends ASTNode {
    NeFormalDeclList declList;

    public FormalDeclList(NeFormalDeclList declList, Location loc) {
        super(loc);
        this.declList = declList;
    }

    public NeFormalDeclList getDeclList() {
        return declList;
    }

    @Override
    public String toString() {
        return declList.toString();
    }
}
