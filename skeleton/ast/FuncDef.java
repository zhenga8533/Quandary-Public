package ast;

import java.util.HashMap;

public class FuncDef extends ASTNode {

    final VarDecl varDecl;
    final FormalDeclList declList;
    final StmtList stmtList;

    final HashMap<String, Long> mutableMap = new HashMap<String, Long>();
    final HashMap<String, Long> immutableMap = new HashMap<String, Long>();

    public FuncDef(VarDecl varDecl, FormalDeclList declList, StmtList stmtList, Location loc) {
        super(loc);
        this.varDecl = varDecl;
        this.declList = declList;
        this.stmtList = stmtList;
    }

    public VarDecl getVarDecl() {
        return varDecl;
    }
    
    public FormalDeclList getDeclList() {
        return declList;
    }

    public StmtList getStmtList() {
        return stmtList;
    }

    public HashMap<String, Long> getMutableMap() {
        return mutableMap;
    }

    public HashMap<String, Long> getImmutableMap() {
        return immutableMap;
    }

    public Location getLocation() {
        return loc;
    }

    @Override
    public String toString() {
        return varDecl.toString() + "(" + declList.toString() + ") " + stmtList.toString();
    }
}
