package interpreter;

import java.io.*;
import java.util.Random;

import parser.ParserWrapper;
import ast.*;
import var.*;

import java.util.HashMap;

public class Interpreter {

    // Process return codes
    public static final int EXIT_SUCCESS = 0;
    public static final int EXIT_PARSING_ERROR = 1;
    public static final int EXIT_STATIC_CHECKING_ERROR = 2;
    public static final int EXIT_DYNAMIC_TYPE_ERROR = 3;
    public static final int EXIT_NIL_REF_ERROR = 4;
    public static final int EXIT_QUANDARY_HEAP_OUT_OF_MEMORY_ERROR = 5;
    public static final int EXIT_DATA_RACE_ERROR = 6;
    public static final int EXIT_NONDETERMINISM_ERROR = 7;

    static private Interpreter interpreter;

    public static Interpreter getInterpreter() {
        return interpreter;
    }

    public static void main(String[] args) {
        String gcType = "NoGC"; // default for skeleton, which only supports NoGC
        long heapBytes = 1 << 14;
        int i = 0;
        String filename;
        long quandaryArg;
        try {
            for (; i < args.length; i++) {
                String arg = args[i];
                if (arg.startsWith("-")) {
                    if (arg.equals("-gc")) {
                        gcType = args[i + 1];
                        i++;
                    } else if (arg.equals("-heapsize")) {
                        heapBytes = Long.valueOf(args[i + 1]);
                        i++;
                    } else {
                        throw new RuntimeException("Unexpected option " + arg);
                    }
                } else {
                    if (i != args.length - 2) {
                        throw new RuntimeException("Unexpected number of arguments");
                    }
                    break;
                }
            }
            filename = args[i];
            quandaryArg = Long.valueOf(args[i + 1]);
        } catch (Exception ex) {
            System.out.println("Expected format: quandary [OPTIONS] QUANDARY_PROGRAM_FILE INTEGER_ARGUMENT");
            System.out.println("Options:");
            System.out.println("  -gc (MarkSweep|Explicit|NoGC)");
            System.out.println("  -heapsize BYTES");
            System.out.println("BYTES must be a multiple of the word size (8)");
            return;
        }

        Program astRoot = null;
        Reader reader;
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try {
            astRoot = ParserWrapper.parse(reader);
        } catch (Exception ex) {
            ex.printStackTrace();
            Interpreter.fatalError("Uncaught parsing error: " + ex, Interpreter.EXIT_PARSING_ERROR);
        }
        //astRoot.println(System.out);
        interpreter = new Interpreter(astRoot);
        interpreter.initMemoryManager(gcType, heapBytes);
        String returnValueAsString = interpreter.executeRoot(astRoot, quandaryArg).toString();
        System.out.println("Interpreter returned " + returnValueAsString);
    }

    final Program astRoot;
    final Random random;

    private Interpreter(Program astRoot) {
        this.astRoot = astRoot;
        this.random = new Random();
    }

    void initMemoryManager(String gcType, long heapBytes) {
        if (gcType.equals("Explicit")) {
            throw new RuntimeException("Explicit not implemented");            
        } else if (gcType.equals("MarkSweep")) {
            throw new RuntimeException("MarkSweep not implemented");            
        } else if (gcType.equals("RefCount")) {
            throw new RuntimeException("RefCount not implemented");            
        } else if (gcType.equals("NoGC")) {
            // Nothing to do
        }
    }

    Q callBuiltInFunction(String name, Q... args) {
        if (name.equals("randomInt") && args.length == 1) {
            int value = (int) ((Int)args[0]).getValue();
            return new Int(random.nextInt(value));
        } else if (name.equals("left") && args.length == 1) {
            return ((Ref)args[0]).getLeft();
        } else if (name.equals("right") && args.length == 1) {
            return ((Ref)args[0]).getRight();
        } else if (name.equals("isAtom") && args.length == 1) {
            if (args[0] instanceof Int) {
                return new Int(1);
            } else if (args[0] instanceof Ref) {
                Ref ref = (Ref)args[0];
                return new Int(ref.getLeft() == null && ref.getRight() == null ? 1 : 0);
            } else {
                return new Int(0);
            }
        } else if (name.equals("isNil") && args.length == 1) {
            if (args[0] instanceof Int) {
                return new Int(0);
            } else if (args[0] instanceof Ref) {
                Ref ref = (Ref)args[0];
                return new Int(ref.getLeft() == null && ref.getRight() == null ? 1 : 0);
            } else {
                return new Int(0);
            }
        } else if (name.equals("setLeft") && args.length == 2) {
            Ref ref = (Ref)args[0];
            ref.setLeft(args[1]);
            return new Int(1);
        } else if (name.equals("setRight") && args.length == 2) {
            Ref ref = (Ref)args[0];
            ref.setRight(args[1]);
            return new Int(1);
        } else {
            throw new RuntimeException("Unhandled function " + name);
        }
    }

    Q executeCall(String name, ExprList exprList, FuncDef func) {
        FuncDef calledFunc = astRoot.getFuncDefList().findFunc(name);

        // If the function is not defined, try a built-in function
        if (calledFunc == null) {
            if (exprList == null) {
                return callBuiltInFunction(name);
            }

            NeExprList neExprList = exprList.getNeExprList();
            Q[] args = new Q[neExprList.size()];
            int i = 0;
            while (neExprList != null) {
                args[i++] = evaluateExpr(neExprList.getExpr(), func);
                neExprList = neExprList.getNeExprList();
            }
            return callBuiltInFunction(name, args);
        }

        // If the function is defined, execute it
        HashMap<String, Q> mutableMap = new HashMap<String, Q>();
        HashMap<String, Q> immutableMap = new HashMap<String, Q>();
        HashMap<String, String> typeMap = new HashMap<String, String>();

        FormalDeclList formalDeclList = calledFunc.getDeclList();
        NeFormalDeclList neFormalDeclList = formalDeclList == null ? null : formalDeclList.getDeclList();
        NeExprList neExprList = exprList == null ? null : exprList.getNeExprList();

        while (neFormalDeclList != null) {
            VarDecl varDecl = neFormalDeclList.getVarDecl();
            Q value = evaluateExpr(neExprList.getExpr(), func);
            if (varDecl.isMutable()) {
                mutableMap.put(varDecl.getIdent(), value);
            } else {
                immutableMap.put(varDecl.getIdent(), value);
            }
            typeMap.put(varDecl.getIdent(), varDecl.getType().toString());
            neFormalDeclList = neFormalDeclList.getDeclList();
            neExprList = neExprList.getNeExprList();
        }
        
        FuncDef newFunc = new FuncDef(calledFunc.getVarDecl(), formalDeclList, calledFunc.getStmtList(), null);
        newFunc.getMutableMap().putAll(mutableMap);
        newFunc.getImmutableMap().putAll(immutableMap);
        newFunc.getTypeMap().putAll(typeMap);

        return evaluateStmtList(calledFunc.getStmtList(), newFunc);
    }

    Q executeRoot(Program astRoot, long arg) {
        FuncDef mainFunc = astRoot.getFuncDefList().findFunc("main");
        HashMap<String, Q> mutableMap = mainFunc.getMutableMap();
        HashMap<String, Q> immutableMap = mainFunc.getImmutableMap();
        HashMap<String, String> typeMap = mainFunc.getTypeMap();
        
        FormalDeclList formalDeclList = mainFunc.getDeclList();
        NeFormalDeclList neFormalDeclList = formalDeclList.getDeclList();
        while (neFormalDeclList != null) {
            VarDecl varDecl = neFormalDeclList.getVarDecl();
            if (varDecl.isMutable()) {
                mutableMap.put(varDecl.getIdent(), new Int(arg));
            } else {
                immutableMap.put(varDecl.getIdent(), new Int(arg));
            }
            typeMap.put(varDecl.getIdent(), varDecl.getType().toString());
            neFormalDeclList = neFormalDeclList.getDeclList();
        }

        return evaluateStmtList(mainFunc.getStmtList(), mainFunc);
    }

    Q evaluateStmtList(StmtList stmtList, FuncDef func) {
        if (stmtList == null) {
            return null;
        }

        Q result = evaluateStmt(stmtList.getStmt(), func);
        if (result != null) {
            return result;
        }

        return evaluateStmtList(stmtList.getStmtList(), func);
    }

    Q evaluateStmt(Stmt stmt, FuncDef func) {
        if (stmt instanceof DeclStmt) {
            DeclStmt declStmt = (DeclStmt)stmt;
            VarDecl varDecl = declStmt.getVarDecl();
            String ident = varDecl.getIdent();
            Q value = evaluateExpr(declStmt.getExpr(), func);
            if (varDecl.isMutable()) {
                func.getMutableMap().put(ident, value);
            } else {
                func.getImmutableMap().put(ident, value);
            }
            func.getTypeMap().put(ident, varDecl.getType().toString());
            return null;
        } else if (stmt instanceof UpdateStmt) {
            UpdateStmt updateStmt = (UpdateStmt)stmt;
            String ident = updateStmt.getIdent();
            Q value = evaluateExpr(updateStmt.getExpr(), func);
            if (func.getMutableMap().containsKey(ident)) {
                func.getMutableMap().put(ident, value);
            } else if (func.getImmutableMap().containsKey(ident)) {
                throw new RuntimeException("Immutable variable cannot be updated: " + ident);
            } else {
                throw new RuntimeException("Variable not found: " + ident);
            }
            return null;
        } else if (stmt instanceof IfStmt) {
            IfStmt ifStmt = (IfStmt)stmt;
            if (evaluateCond(ifStmt.getCond(), func)) {
                return evaluateStmt(ifStmt.getStmt(), func);
            } else {
                return null;
            }
        } else if (stmt instanceof IfElseStmt) {
            IfElseStmt ifElseStmt = (IfElseStmt)stmt;
            if (evaluateCond(ifElseStmt.getCondition(), func)) {
                return evaluateStmt(ifElseStmt.getThenBlock(), func);
            } else {
                return evaluateStmt(ifElseStmt.getElseBlock(), func);
            }
        } else if (stmt instanceof WhileStmt) {
            WhileStmt whileStmt = (WhileStmt)stmt;
            while (evaluateCond(whileStmt.getCond(), func)) {
                Q value = evaluateStmt(whileStmt.getStmt(), func);
                if (value != null) {
                    return value;
                }
            }
            return null;
        } else if (stmt instanceof CallStmt) {
            CallStmt callStmt = (CallStmt)stmt;
            executeCall(callStmt.getIdent(), callStmt.getExprList(), func);
            return null;
        } else if (stmt instanceof PrintStmt) {
            Q value = evaluateExpr(((PrintStmt)stmt).getExpr(), func);
            System.out.println(value);
            return null;
        } else if (stmt instanceof ReturnStmt) {
            return evaluateExpr(((ReturnStmt)stmt).getExpr(), func);
        } else if (stmt instanceof BracketStmt) {
            return evaluateStmtList(((BracketStmt)stmt).getStmtList(), func);
        } else {
            throw new RuntimeException("Unhandled Stmt type");
        }
    }

    Q evaluateExpr(Expr expr, FuncDef func) {
        if (expr instanceof NilExpr) {
            return new Ref(null, null);
        } else if (expr instanceof ConstExpr) {
            ConstExpr constExpr = (ConstExpr)expr;
            return new Int(constExpr.getValue());
        } else if (expr instanceof IdentExpr) {
            IdentExpr identExpr = (IdentExpr)expr;
            HashMap<String, Q> mutableMap = func.getMutableMap();
            HashMap<String, Q> immutableMap = func.getImmutableMap();

            if (mutableMap.containsKey(identExpr.getIdent())) {
                return mutableMap.get(identExpr.getIdent());
            } else if (immutableMap.containsKey(identExpr.getIdent())) {
                return immutableMap.get(identExpr.getIdent());
            } else {
                throw new RuntimeException("Variable not found: " + identExpr.getIdent());
            }
        } else if (expr instanceof UnaryExpr) {
            UnaryExpr unaryExpr = (UnaryExpr)expr;
            Q value = evaluateExpr(unaryExpr.getExpr(), func);
            switch (unaryExpr.getOperator()) {
                case UnaryExpr.NEGATION: return new Int(-((Int)value).getValue());
                default: throw new RuntimeException("Unhandled operator");
            }
        } else if (expr instanceof TypecastExpr) {
            TypecastExpr typecastExpr = (TypecastExpr)expr;
            return evaluateExpr(typecastExpr.getExpr(), func);
        } else if (expr instanceof CallExpr) {
            CallExpr callExpr = (CallExpr)expr;
            return executeCall(callExpr.getIdent(), callExpr.getExprList(), func);
        } else if (expr instanceof BinaryExpr) {
            BinaryExpr binaryExpr = (BinaryExpr)expr;
            Q left = evaluateExpr(binaryExpr.getLeftExpr(), func);
            Q right = evaluateExpr(binaryExpr.getRightExpr(), func);
            switch (binaryExpr.getOperator()) {
                case BinaryExpr.PLUS: return new Int(((Int)left).getValue() + ((Int)right).getValue());
                case BinaryExpr.MINUS: return new Int(((Int)left).getValue() - ((Int)right).getValue());
                case BinaryExpr.TIMES: return new Int(((Int)left).getValue() * ((Int)right).getValue());
                case BinaryExpr.PERIOD: return new Ref(left, right);
                default: throw new RuntimeException("Unhandled operator");
            }
        } else {
            throw new RuntimeException("Unhandled Expr type");
        }
    }

    Boolean evaluateCond(Cond cond, FuncDef func) {
        if (cond instanceof BinaryCond) {
            BinaryCond binaryCond = (BinaryCond)cond;
            Int left = (Int) evaluateExpr(binaryCond.getLeft(), func);
            Int right = (Int) evaluateExpr(binaryCond.getRight(), func);
            Long lVal = left.getValue();
            Long rVal = right.getValue();

            switch (binaryCond.getOperator()) {
                case BinaryCond.LE: return lVal <= rVal;
                case BinaryCond.GE: return lVal >= rVal;
                case BinaryCond.EQ: return lVal.equals(rVal);
                case BinaryCond.NE: return !lVal.equals(rVal);
                case BinaryCond.LT: return lVal < rVal;
                case BinaryCond.GT: return lVal > rVal;
                default: throw new RuntimeException("Unhandled operator");
            }
        } else if (cond instanceof LogicalCond) {
            LogicalCond logicalCond = (LogicalCond)cond;
            switch (logicalCond.getOperator()) {
                case LogicalCond.AND: return evaluateCond(logicalCond.getLeft(), func) && evaluateCond(logicalCond.getRight(), func);
                case LogicalCond.OR: return evaluateCond(logicalCond.getLeft(), func) || evaluateCond(logicalCond.getRight(), func);
                default: throw new RuntimeException("Unhandled operator");
            }
        } else if (cond instanceof UnaryCond) {
            UnaryCond unaryCond = (UnaryCond)cond;
            switch (unaryCond.getOperator()) {
                case UnaryCond.NOT: return !evaluateCond(unaryCond.getExpr(), func);
                default: throw new RuntimeException("Unhandled operator");
            }
        } else {
            throw new RuntimeException("Unhandled Cond type");
        }
    }

	public static void fatalError(String message, int processReturnCode) {
        System.out.println(message);
        System.exit(processReturnCode);
	}
}
