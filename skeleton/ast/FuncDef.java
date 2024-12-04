package ast;

import java.util.HashMap;

import var.Q;

public class FuncDef extends ASTNode {
    final VarDecl varDecl;
    final FormalDeclList declList;
    final StmtList stmtList;

    final HashMap<String, Q> mutableMap = new HashMap<String, Q>();
    final HashMap<String, Q> immutableMap = new HashMap<String, Q>();
    final HashMap<String, String> typeMap = new HashMap<String, String>();

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

    public HashMap<String, Q> getMutableMap() {
        return mutableMap;
    }

    public HashMap<String, Q> getImmutableMap() {
        return immutableMap;
    }

    public HashMap<String, String> getTypeMap() {
        return typeMap;
    }

    @Override
    public String toString() {
        return varDecl.toString() + "(" + declList.toString() + ") " + stmtList.toString();
    }
}
