package ast;

import java.util.HashMap;

public class FuncDef extends ASTNode {

    final VarDecl varDecl;
    final FormalDeclList declList;
    final StmtList stmtList;

    final HashMap<String, Object> mutableMap = new HashMap<String, Object>();
    final HashMap<String, Object> immutableMap = new HashMap<String, Object>();
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

    public HashMap<String, Object> getMutableMap() {
        return mutableMap;
    }

    public HashMap<String, Object> getImmutableMap() {
        return immutableMap;
    }

    public HashMap<String, String> getTypeMap() {
        return typeMap;
    }

    public Location getLocation() {
        return loc;
    }

    @Override
    public String toString() {
        return varDecl.toString() + "(" + declList.toString() + ") " + stmtList.toString();
    }
}
