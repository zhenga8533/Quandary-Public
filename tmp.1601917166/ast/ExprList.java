package ast;

public class ExprList extends ASTNode {
    NeExprList neExprList;

    public ExprList(NeExprList neExprList, Location loc) {
        super(loc);
        this.neExprList = neExprList;
    }

    public NeExprList getNeExprList() {
        return neExprList;
    }

    @Override
    public String toString() {
        return neExprList.toString();
    }
}
