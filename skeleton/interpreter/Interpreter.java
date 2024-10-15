package interpreter;

import java.io.*;
import java.util.Random;

import parser.ParserWrapper;
import ast.*;

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

    Object callBuiltInFunction(String name, Object... args) {
        if (name.equals("randomInt") && args.length == 1 && args[0] instanceof Long) {
            long arg = (Long) args[0];
            return (long) random.nextInt((int) arg);
        } else {
            throw new RuntimeException("Unhandled function " + name);
        }
    }

    public Object executeCall(String name, ExprList exprList, FuncDef func) {
        FuncDef calledFunc = astRoot.getFuncDefList().findFunc(name);

        // If the function is not defined, try a built-in function
        if (calledFunc == null) {
            if (exprList == null) {
                return callBuiltInFunction(name);
            }

            NeExprList neExprList = exprList.getNeExprList();
            Object[] args = new Object[neExprList.size()];
            int i = 0;
            while (neExprList != null) {
                args[i++] = evaluateExpr(neExprList.getExpr(), func);
                neExprList = neExprList.getNeExprList();
            }
            return callBuiltInFunction(name, args);
        }

        // If the function is defined, evaluate the arguments and call the function
        HashMap<String, Long> varMap = new HashMap<>(calledFunc.getVarMap());
        FormalDeclList formalDeclList = calledFunc.getDeclList();
        if (formalDeclList != null) {
            NeFormalDeclList neFormalDeclList = formalDeclList.getDeclList();
            NeExprList neExprList = exprList.getNeExprList();
            
            while (neFormalDeclList != null) {
                varMap.put(neFormalDeclList.getVarDecl().getIdent(), (Long)evaluateExpr(neExprList.getExpr(), func));
                neFormalDeclList = neFormalDeclList.getDeclList();
                neExprList = neExprList.getNeExprList();
            }
        }

        // Create a new function for recursion
        FuncDef newFunc = new FuncDef(calledFunc.getVarDecl(), calledFunc.getDeclList(), calledFunc.getStmtList(), calledFunc.getLocation());
        newFunc.getVarMap().putAll(varMap);
        return evaluateStmtList(newFunc.getStmtList(), newFunc);
    }

    Object executeRoot(Program astRoot, long arg) {
        FuncDef mainFunc = astRoot.getFuncDefList().findFunc("main");

        HashMap<String, Long> varMap = mainFunc.getVarMap();
        FormalDeclList formalDeclList = mainFunc.getDeclList();
        NeFormalDeclList neFormalDeclList = formalDeclList.getDeclList();
        while (neFormalDeclList != null) {
            varMap.put(neFormalDeclList.getVarDecl().getIdent(), arg);
            neFormalDeclList = neFormalDeclList.getDeclList();
        }

        return evaluateStmtList(mainFunc.getStmtList(), mainFunc);
    }

    Object evaluateStmtList(StmtList stmtList, FuncDef func) {
        if (stmtList == null) {
            return null;
        }

        Object result = evaluateStmt(stmtList.getStmt(), func);
        if (result != null) {
            return result;
        }

        return evaluateStmtList(stmtList.getStmtList(), func);
    }

    Object evaluateStmt(Stmt stmt, FuncDef func) {
        if (stmt instanceof DeclStmt) {
            DeclStmt declStmt = (DeclStmt)stmt;
            func.getVarMap().put(declStmt.getVarDecl().getIdent(), (Long)evaluateExpr(declStmt.getExpr(), func));
            return null;
        } else if (stmt instanceof IfStmt) {
            IfStmt ifStmt = (IfStmt)stmt;
            if ((Boolean)evaluateCond(ifStmt.getCond(), func)) {
                return evaluateStmt(ifStmt.getStmt(), func);
            } else {
                return null;
            }
        } else if (stmt instanceof IfElseStmt) {
            IfElseStmt ifElseStmt = (IfElseStmt)stmt;
            if ((Boolean)evaluateCond(ifElseStmt.getCondition(), func)) {
                return evaluateStmt(ifElseStmt.getThenBlock(), func);
            } else {
                return evaluateStmt(ifElseStmt.getElseBlock(), func);
            }
        } else if (stmt instanceof CallStmt) {
            CallStmt callStmt = (CallStmt)stmt;
            return executeCall(callStmt.getIdent(), callStmt.getExprList(), func);
        } else if (stmt instanceof PrintStmt) {
            Object value = evaluateExpr(((PrintStmt)stmt).getExpr(), func);
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

    Object evaluateExpr(Expr expr, FuncDef func) {
        if (expr instanceof ConstExpr) {
            return ((ConstExpr)expr).getValue();
        } else if (expr instanceof IdentExpr) {
            return func.getVarMap().get(((IdentExpr)expr).getIdent());
        } else if (expr instanceof UnaryExpr) {
            UnaryExpr unaryExpr = (UnaryExpr)expr;
            switch (unaryExpr.getOperator()) {
                case UnaryExpr.NEGATION: return -(Long)evaluateExpr(unaryExpr.getExpr(), func);
                default: throw new RuntimeException("Unhandled operator");
            }
        } else if (expr instanceof CallExpr) {
            CallExpr callExpr = (CallExpr)expr;
            return executeCall(callExpr.getIdent(), callExpr.getExprList(), func);
        } else if (expr instanceof BinaryExpr) {
            BinaryExpr binaryExpr = (BinaryExpr)expr;
            switch (binaryExpr.getOperator()) {
                case BinaryExpr.PLUS: return (Long)evaluateExpr(binaryExpr.getLeftExpr(), func) + (Long)evaluateExpr(binaryExpr.getRightExpr(), func);
                case BinaryExpr.MINUS: return (Long)evaluateExpr(binaryExpr.getLeftExpr(), func) - (Long)evaluateExpr(binaryExpr.getRightExpr(), func);
                case BinaryExpr.TIMES: return (Long)evaluateExpr(binaryExpr.getLeftExpr(), func) * (Long)evaluateExpr(binaryExpr.getRightExpr(), func);
                default: throw new RuntimeException("Unhandled operator");
            }
        } else {
            throw new RuntimeException("Unhandled Expr type");
        }
    }

    Object evaluateCond(Cond cond, FuncDef func) {
        if (cond instanceof BinaryCond) {
            BinaryCond binaryCond = (BinaryCond)cond;
            switch (binaryCond.getOperator()) {
                case BinaryCond.LE: return (Long)evaluateExpr(binaryCond.getLeft(), func) <= (Long)evaluateExpr(binaryCond.getRight(), func);
                case BinaryCond.GE: return (Long)evaluateExpr(binaryCond.getLeft(), func) >= (Long)evaluateExpr(binaryCond.getRight(), func);
                case BinaryCond.EQ: return evaluateExpr(binaryCond.getLeft(), func).equals(evaluateExpr(binaryCond.getRight(), func));
                case BinaryCond.NE: return !evaluateExpr(binaryCond.getLeft(), func).equals(evaluateExpr(binaryCond.getRight(), func));
                case BinaryCond.LT: return (Long)evaluateExpr(binaryCond.getLeft(), func) < (Long)evaluateExpr(binaryCond.getRight(), func);
                case BinaryCond.GT: return (Long)evaluateExpr(binaryCond.getLeft(), func) > (Long)evaluateExpr(binaryCond.getRight(), func);
                default: throw new RuntimeException("Unhandled operator");
            }
        } else if (cond instanceof LogicalCond) {
            LogicalCond logicalCond = (LogicalCond)cond;
            switch (logicalCond.getOperator()) {
                case LogicalCond.AND: return (Boolean)evaluateCond(logicalCond.getLeft(), func) && (Boolean)evaluateCond(logicalCond.getRight(), func);
                case LogicalCond.OR: return (Boolean)evaluateCond(logicalCond.getLeft(), func) || (Boolean)evaluateCond(logicalCond.getRight(), func);
                default: throw new RuntimeException("Unhandled operator");
            }
        } else if (cond instanceof UnaryCond) {
            UnaryCond unaryCond = (UnaryCond)cond;
            switch (unaryCond.getOperator()) {
                case UnaryCond.NOT: return !(Boolean)evaluateCond(unaryCond.getExpr(), func);
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
