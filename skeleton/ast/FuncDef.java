package ast;

public class FuncDef extends ASTNode {

    final VarDecl varDecl;
    final FormalDeclList declList;
    final StmtList stmtList;

    public FuncDef(VarDecl varDecl, FormalDeclList declList, StmtList stmtList, Location loc) {
        super(loc);
        this.varDecl = varDecl;
        this.declList = declList;
        this.stmtList = stmtList;
    }

    public VarDecl getVarDecl() {
        return varDecl;
    }
    
    public FormalDeclList getDeclList() {
        return declList;
    }

    public StmtList getStmtList() {
        return stmtList;
    }

    @Override
    public String toString() {
        return varDecl.toString() + "(" + declList.toString() + ") " + stmtList.toString();
    }
}
