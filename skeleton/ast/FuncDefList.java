package ast;

public class FuncDefList extends ASTNode {
    final FuncDef func;
    final FuncDefList funcList;

    public FuncDefList(FuncDef func, FuncDefList funcList, Location loc) {
        super(loc);
        this.func = func;
        this.funcList = funcList;
    }

    public FuncDef findFunc(String name) {
        if (func.getVarDecl().getIdent().equals(name)) {
            return func;
        } else if (funcList != null) {
            return funcList.findFunc(name);
        } else {
            return null;
        }
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
