package ast;

public class BracketStmt extends Stmt {
    final StmtList stmtList;

    public BracketStmt(StmtList stmtList, Location loc) {
        super(loc);
        this.stmtList = stmtList;
    }

    public StmtList getStmtList() {
        return stmtList;
    }

    @Override
    public String toString() {
        return "{" + stmtList.toString() + "}";
    }
}
