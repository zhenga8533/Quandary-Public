package ast;

public class NeFormalDeclList extends ASTNode {
    VarDecl varDecl;
    NeFormalDeclList declList;

    public NeFormalDeclList(VarDecl varDecl, NeFormalDeclList declList, Location loc) {
        super(loc);
        this.varDecl = varDecl;
        this.declList = declList;
    }

    public VarDecl getVarDecl() {
        return varDecl;
    }

    public NeFormalDeclList getDeclList() {
        return declList;
    }

    @Override
    public String toString() {
        return varDecl.toString() + (declList == null ? "" : ", " + declList.toString());
    }
}
