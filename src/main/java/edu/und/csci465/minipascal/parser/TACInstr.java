package edu.und.csci465.minipascal.parser;

import java.util.HashMap;
import java.util.Map;

class TACInstr {
    public static String MY_C_VARIABLE = "__my_c__";
    public static String MY_CH_VARIABLE = "__my_ch__";

    public static Map<String, String> MY_VARIABLE_MAP = new HashMap();
    static {
        MY_VARIABLE_MAP.put("c", MY_C_VARIABLE);
        MY_VARIABLE_MAP.put("ch", MY_CH_VARIABLE);
    }

    // For binary: result = arg1 op arg2  (op in {"+", "*", "/"})
    // For assign: result = arg1          (op = "assign")
    // For read:   read result            (op = "read")
    // For print:  print arg1             (op = "print")
    String op;
    String arg1;
    String arg2;
    String result;

    boolean inProc = false;

    TACInstr(String op, String arg1, String arg2, String result) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
        handleMasmVariables();
    }

    public void handleMasmVariables() {
        this.arg1 = getMappedValue(this.arg1);
        this.arg2 = getMappedValue(this.arg2);
        this.result = getMappedValue(this.result);
    }

    private String getMappedValue(String value) {
        if(value==null) {
            return null;
        }
        if (MY_VARIABLE_MAP.get(value.toLowerCase()) != null) {
            return MY_VARIABLE_MAP.get(value.toLowerCase());
        }
        return value;
    }

    public String toMyString() {
        return "op=[" + this.op + "]" +
               ",arg1=[" + this.arg1 + "]" +
               ",arg2=[" + this.arg2 + "]" +
               ",result=[" + this.result + "]";
    }

    @Override
    public String toString() {
        switch (op) {
            case "assign":
                return result + " = " + arg1;
            case "+":
            case "-":
            case "*":
            case "/":
                return result + " = " + arg1 + " " + op + " " + arg2;
            case "and":
                return result + " = " + arg1 + " and " + arg2;
            case "or":
                return result + " = " + arg1 + " or " + arg2;
            case "GREATER":
                return result + " = " + arg1 + " > " + arg2;
            case "GREATEREQUAL":
                return result + " = " + arg1 + " >= " + arg2;
            case "LESSTHAN":
                return result + " = " + arg1 + " < " + arg2;
            case "LESSEQUAL":
                return result + " = " + arg1 + " <= " + arg2;
            case "read":
                return "read " + result;
            case "IfZ":
                return "IfZ " + arg1 + " GOTO " + arg2;
            case "Label":
                return arg1 + ":";
            case "declare":
                return "declare " + arg1 + " as " + arg2;
            case "GOTO":
                return "GOTO " + arg1;
            case "readln-integer":
                return "read " + result;
            case "write-integer":
                return "print " + result;
            case "write-boolean":
                return "print " + result;
            case "writeln":
                return "writeln " + arg1;
            default:
                return "// ? " + op + " " + arg1 + " " + arg2 + " " + result;
        }
    }

    public boolean isInProc() {
        return inProc;
    }

    public void setInProc(boolean inProc) {
        this.inProc = inProc;
    }
}