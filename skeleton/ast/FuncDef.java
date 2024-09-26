package ast;

public abstract class FuncDef extends ASTNode {

    final VarDecl varDecl;
    final int arg;
    final StmtList stmtList;

    FuncDef(VarDecl varDecl, int arg, StmtList stmtList, Location loc) {
        super(loc);
        this.varDecl = varDecl;
        this.arg = arg;
        this.stmtList = stmtList;
    }

    public VarDecl getVarDecl() {
        return varDecl;
    }

    public int getArg() {
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
