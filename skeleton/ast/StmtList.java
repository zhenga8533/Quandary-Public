package ast;

public class StmtList extends ASTNode {

    final Stmt stmt;
    final StmtList stmtList;

    public StmtList(Stmt stmt, StmtList stmtList, Location loc) {
        super(loc);
        this.stmt = stmt;
        this.stmtList = stmtList;
    }

    public Stmt getStmt() {
        return stmt;
    }

    public StmtList getStmtList() {
        return stmtList;
    }

    @Override
    public String toString() {
        return stmt.toString() + " " + stmtList.toString();
    }
}
