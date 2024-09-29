package ast;

public class FuncDefList extends ASTNode {
    final FuncDef func;
    final FuncDefList funcList;

    public FuncDefList(FuncDef func, FuncDefList funcList, Location loc) {
        super(loc);
        this.func = func;
        this.funcList = funcList;
    }

    public FuncDef getFunc() {
        return func;
    }

    public FuncDefList getFuncList() {
        return funcList;
    }

    @Override
    public String toString() {
        return func.toString() + funcList.toString();
    }
}
