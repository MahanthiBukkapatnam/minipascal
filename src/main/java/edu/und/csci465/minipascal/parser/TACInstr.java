package edu.und.csci465.minipascal.parser;

class TACInstr {
    public static String MY_C_VARIABLE = "__my_c__";
    // For binary: result = arg1 op arg2  (op in {"+", "*", "/"})
    // For assign: result = arg1          (op = "assign")
    // For read:   read result            (op = "read")
    // For print:  print arg1             (op = "print")
    String op;
    String arg1;
    String arg2;
    String result;

    TACInstr(String op, String arg1, String arg2, String result) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
        handleCVariable();
    }
    public void handleCVariable() {
        if("C".equalsIgnoreCase(this.arg1)) {
            this.arg1 = TACInstr.MY_C_VARIABLE;
        }
        if("C".equalsIgnoreCase(this.arg2)) {
            this.arg2 = TACInstr.MY_C_VARIABLE;
        }
        if("C".equalsIgnoreCase(this.result)) {
            this.result = TACInstr.MY_C_VARIABLE;
        }
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
            case "print":
                return "print " + arg1;
            default:
                return "// ? " + op + " " + arg1 + " " + arg2 + " " + result;
        }
    }
}