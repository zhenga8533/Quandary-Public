package ast;

import java.io.PrintStream;

public class Program extends ASTNode {
    final FuncDefList funcList;

    public Program(FuncDefList funcList, Location loc) {
        super(loc);
        this.funcList = funcList;
    }

    public FuncDefList getFuncDefList() {
        return funcList;
    }

    public void println(PrintStream ps) {
        ps.println(funcList);
    }
}
