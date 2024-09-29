package interpreter;

import java.io.*;
import java.util.Random;

import parser.ParserWrapper;
import ast.*;

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

    Object executeRoot(Program astRoot, long arg) {
        return evaluateStmtList(astRoot.getFunc().getStmtList());
    }

    Object evaluateStmtList(StmtList stmtList) {
    if (stmtList == null) {
        return null;
    }

    Object result = evaluateStmt(stmtList.getStmt());
    if (result != null) {
        return result;
    }

    return evaluateStmtList(stmtList.getStmtList());
}

    Object evaluateStmt(Stmt stmt) {
        if (stmt instanceof PrintStmt) {
            Object value = evaluateExpr(((PrintStmt)stmt).getExpr());
            System.out.println(value);
            return null;
        } else if (stmt instanceof ReturnStmt) {
            return evaluateExpr(((ReturnStmt)stmt).getExpr());
        } else {
            throw new RuntimeException("Unhandled Stmt type");
        }
    }

    Object evaluateExpr(Expr expr) {
        if (expr instanceof ConstExpr) {
            return ((ConstExpr)expr).getValue();
        } else if (expr instanceof UnaryExpr) {
            UnaryExpr unaryExpr = (UnaryExpr)expr;
            switch (unaryExpr.getOperator()) {
                case UnaryExpr.NEGATION: return -(Long)evaluateExpr(unaryExpr.getExpr());
                default: throw new RuntimeException("Unhandled operator");
            }
        } else if (expr instanceof BinaryExpr) {
            BinaryExpr binaryExpr = (BinaryExpr)expr;
            switch (binaryExpr.getOperator()) {
                case BinaryExpr.PLUS: return (Long)evaluateExpr(binaryExpr.getLeftExpr()) + (Long)evaluateExpr(binaryExpr.getRightExpr());
                case BinaryExpr.MINUS: return (Long)evaluateExpr(binaryExpr.getLeftExpr()) - (Long)evaluateExpr(binaryExpr.getRightExpr());
                case BinaryExpr.TIMES: return (Long)evaluateExpr(binaryExpr.getLeftExpr()) * (Long)evaluateExpr(binaryExpr.getRightExpr());
                default: throw new RuntimeException("Unhandled operator");
            }
        } else {
            throw new RuntimeException("Unhandled Expr type");
        }
    }

    Object evaluateCond(Cond cond) {
        if (cond instanceof BinaryCond) {
            BinaryCond binaryCond = (BinaryCond)cond;
            switch (binaryCond.getOperator()) {
                case BinaryCond.LE: return (Long)evaluateExpr(binaryCond.getLeft()) <= (Long)evaluateExpr(binaryCond.getRight());
                case BinaryCond.GE: return (Long)evaluateExpr(binaryCond.getLeft()) >= (Long)evaluateExpr(binaryCond.getRight());
                case BinaryCond.EQ: return evaluateExpr(binaryCond.getLeft()).equals(evaluateExpr(binaryCond.getRight()));
                case BinaryCond.NE: return !evaluateExpr(binaryCond.getLeft()).equals(evaluateExpr(binaryCond.getRight()));
                case BinaryCond.LT: return (Long)evaluateExpr(binaryCond.getLeft()) < (Long)evaluateExpr(binaryCond.getRight());
                case BinaryCond.GT: return (Long)evaluateExpr(binaryCond.getLeft()) > (Long)evaluateExpr(binaryCond.getRight());
                default: throw new RuntimeException("Unhandled operator");
            }
        } else if (cond instanceof LogicalCond) {
            LogicalCond logicalCond = (LogicalCond)cond;
            switch (logicalCond.getOperator()) {
                case LogicalCond.AND: return (Boolean)evaluateCond(logicalCond.getLeft()) && (Boolean)evaluateCond(logicalCond.getRight());
                case LogicalCond.OR: return (Boolean)evaluateCond(logicalCond.getLeft()) || (Boolean)evaluateCond(logicalCond.getRight());
                default: throw new RuntimeException("Unhandled operator");
            }
        } else if (cond instanceof UnaryCond) {
            UnaryCond unaryCond = (UnaryCond)cond;
            switch (unaryCond.getOperator()) {
                case UnaryCond.NOT: return !(Boolean)evaluateCond(unaryCond.getExpr());
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
