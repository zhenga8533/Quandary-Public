package ast;

public class FuncDef extends ASTNode {

    final VarDecl varDecl;
    final VarDecl arg;
    final StmtList stmtList;

    public FuncDef(VarDecl varDecl, VarDecl arg, StmtList stmtList, Location loc) {
        super(loc);
        this.varDecl = varDecl;
        this.arg = arg;
        this.stmtList = stmtList;
    }

    public VarDecl getVarDecl() {
        return varDecl;
    }

    public VarDecl getArg() {
        return arg;
    }

    public StmtList getStmtList() {
        return stmtList;
    }

    @Override
    public String toString() {
        return varDecl.toString() + "(" + arg + ")" + stmtList;
    }
}
