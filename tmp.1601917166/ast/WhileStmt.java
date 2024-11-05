package ast;

public class WhileStmt extends Stmt {
    public Cond cond;
    public Stmt stmt;

    public WhileStmt(Cond cond, Stmt stmt, Location loc) {
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
        return "while (" + cond.toString() + ") " + stmt.toString();
    }
}
