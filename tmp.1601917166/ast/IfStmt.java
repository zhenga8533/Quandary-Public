package ast;

public class IfStmt extends Stmt {
    final Cond cond;
    final Stmt stmt;

    public IfStmt(Cond cond, Stmt stmt, Location loc) {
        super(loc);
        this.cond = cond;
        this.stmt = stmt;
    }

    public Cond getCond() {
        return cond;
    }

    public Stmt getStmt() {
        return stmt;
    }

    @Override
    public String toString() {
        return "if (" + cond.toString() + ") " + stmt.toString();
    }
}
