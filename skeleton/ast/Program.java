package ast;

import java.io.PrintStream;

public class Program extends ASTNode {

    final FuncDef func;

    public Program(FuncDef func, Location loc) {
        super(loc);
        this.func = func;
    }

    public FuncDef getFunc() {
        return func;
    }

    public void println(PrintStream ps) {
        ps.println(func);
    }
}
