package ast;

public class IfElseStmt extends Stmt {
    final Cond condition;
    final Stmt thenBlock;
    final Stmt elseBlock;

    public IfElseStmt(Cond condition, Stmt thenBlock, Stmt elseBlock, Location loc) {
        super(loc);
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    public Cond getCondition() {
        return condition;
    }

    public Stmt getThenBlock() {
        return thenBlock;
    }

    public Stmt getElseBlock() {
        return elseBlock;
    }

    @Override
    public String toString() {
        return "if " + condition.toString() + " " + thenBlock.toString() + " else " + elseBlock.toString();
    }
}
