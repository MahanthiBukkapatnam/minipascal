package edu.und.csci465.minipascal.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TACInterpreter {
    private final List<TACInstr> code;
    private final Map<String, Object> env = new HashMap<>();
    private final StringBuilder out = new StringBuilder();

    TACInterpreter(List<TACInstr> code) {
        this.code = code;
    }

    public String run() {
        for (TACInstr i : code) {
            switch (i.op) {
                case "assign":
                    env.put(i.result, intValueOf(i.arg1));
                    break;
                case "+":
                    env.put(i.result, intValueOf(i.arg1) + intValueOf(i.arg2));
                    break;
                case "-":
                    env.put(i.result, intValueOf(i.arg1) - intValueOf(i.arg2));
                    break;
                case "*":
                    env.put(i.result, intValueOf(i.arg1) * intValueOf(i.arg2));
                    break;
                case "/":
                    int b = intValueOf(i.arg2);
                    if (b == 0) throw new RuntimeException("Division by zero in TAC");
                    env.put(i.result, intValueOf(i.arg1) / b);
                    break;
                case "read":
                    env.put(i.result, 100);
                    break;
                case "print":
                    out.append(intValueOf(i.arg1)).append("\n");
                    break;
                default:
                    throw new RuntimeException("Unknown TAC op: " + i.op);
            }
        }
        return out.toString();
    }

    public String evaluateBooleanExpr() {
        for (TACInstr i : code) {
            switch (i.op) {
                case "assign":
                    env.put(i.result, valueOf(i.arg1));
                    break;
                case "+":
                    env.put(i.result, intValueOf(i.arg1) + intValueOf(i.arg2));
                    break;
                case "-":
                    env.put(i.result, intValueOf(i.arg1) - intValueOf(i.arg2));
                    break;
                case "*":
                    env.put(i.result, intValueOf(i.arg1) * intValueOf(i.arg2));
                    break;
                case "/":
                    int b = intValueOf(i.arg2);
                    if (b == 0) throw new RuntimeException("Division by zero in TAC");
                    env.put(i.result, intValueOf(i.arg1) / b);
                    break;
                case "and":
                    boolean arg1ValueB1 = ((Boolean) valueOf(i.arg1)).booleanValue();
                    boolean arg2ValueB1 = ((Boolean) valueOf(i.arg2)).booleanValue();
                    env.put(i.result, arg1ValueB1 && arg2ValueB1);
                    break;
                case "or":
                    boolean arg1ValueB2 = ((Boolean) valueOf(i.arg1)).booleanValue();
                    boolean arg2ValueB2 = ((Boolean) valueOf(i.arg2)).booleanValue();
                    env.put(i.result, arg1ValueB2 || arg2ValueB2);
                    break;
                case "GREATER":
                    int arg1Value = intValueOf(i.arg1);
                    int arg2Value = intValueOf(i.arg2);
                    env.put(i.result, arg1Value > arg2Value);
                    break;
                case "GREATEREQUAL":
                    int arg1Value2 = intValueOf(i.arg1);
                    int arg2Value2 = intValueOf(i.arg2);
                    env.put(i.result, arg1Value2 >= arg2Value2);
                    break;
                case "LESSTHAN":
                    int arg1Value3 = intValueOf(i.arg1);
                    int arg2Value3 = intValueOf(i.arg2);
                    env.put(i.result, arg1Value3 < arg2Value3);
                    break;
                case "LESSEQUAL":
                    int arg1Value4 = intValueOf(i.arg1);
                    int arg2Value4 = intValueOf(i.arg2);
                    env.put(i.result, arg1Value4 <= arg2Value4);
                    break;
                case "read":
                    env.put(i.result, 100);
                    break;
                case "print":
                    out.append(valueOf(i.arg1)).append("\n");
                    break;
                case "declare":
                    //FILLER
                    break;
                case "IfZ":
                    //FILLER
                    break;
                case "GOTO":
                    //FILLER
                    break;
                case "Label":
                    //FILLER
                    break;
                case "readln":
                    //FILLER
                    break;
                case "writeln":
                    //FILLER
                    break;
                case "write-integer":
                    out.append(valueOf(i.result));
                    break;
                case "write-boolean":
                    out.append(valueOf(i.result));
                    break;
                case "writeln-integer":
                    out.append(valueOf(i.result)).append("\n");
                    break;
                case "writeln-boolean":
                    out.append(valueOf(i.result)).append("\n");
                    break;
                default:
                    //throw new RuntimeException("Unknown TAC op: " + i.op);
            }
        }
        return out.toString();
    }

    private int intValueOf(String nameOrLit) {
        if (nameOrLit == null) throw new RuntimeException("Null operand");
        // int literal?
        try {
            return Integer.parseInt(nameOrLit);
        } catch (NumberFormatException e) {
            Object v = env.get(nameOrLit);
            if (v == null) {
                throw new RuntimeException("Use of undefined: " + nameOrLit);
            }
            return ((Integer) v).intValue();
        }
    }

    private Object valueOf(String nameOrLit) {
        if (nameOrLit == null) throw new RuntimeException("Null operand");
        // int literal?
        if (nameOrLit.equalsIgnoreCase("true")) {
            return true;
        } else if (nameOrLit.equalsIgnoreCase("false")) {
            return false;
        }
        Object v = env.get(nameOrLit);
        if (v != null) {
            if(v.toString().equalsIgnoreCase("true")) {
                return true;
            }
            else if(v.toString().equalsIgnoreCase("false")) {
                return false;
            }
        }

        return intValueOf(nameOrLit);
    }

}
