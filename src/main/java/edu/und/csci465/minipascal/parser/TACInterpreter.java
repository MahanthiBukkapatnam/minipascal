package edu.und.csci465.minipascal.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TACInterpreter {
    private final List<TACInstr> code;
    private final Map<String,Integer> env = new HashMap<>();
    private final StringBuilder out = new StringBuilder();

    TACInterpreter(List<TACInstr> code) {
        this.code = code;
    }

    public String run() {
        for (TACInstr i : code) {
            switch (i.op) {
                case "assign":
                    env.put(i.result, valueOf(i.arg1));
                    break;
                case "+":
                    env.put(i.result, valueOf(i.arg1) + valueOf(i.arg2));
                    break;
                case "-":
                    env.put(i.result, valueOf(i.arg1) - valueOf(i.arg2));
                    break;
                case "*":
                    env.put(i.result, valueOf(i.arg1) * valueOf(i.arg2));
                    break;
                case "/":
                    int b = valueOf(i.arg2);
                    if (b == 0) throw new RuntimeException("Division by zero in TAC");
                    env.put(i.result, valueOf(i.arg1) / b);
                    break;
                case "read":
                    env.put(i.result, 100);
                    break;
                case "print":
                    out.append(valueOf(i.arg1)).append("\n");
                    break;
                default:
                    throw new RuntimeException("Unknown TAC op: " + i.op);
            }
        }
        return out.toString();
    }

    private int valueOf(String nameOrLit) {
        if (nameOrLit == null) throw new RuntimeException("Null operand");
        // int literal?
        try {
            return Integer.parseInt(nameOrLit);
        } catch (NumberFormatException e) {
            Integer v = env.get(nameOrLit);
            if (v == null) {
                throw new RuntimeException("Use of undefined: " + nameOrLit);
            }
            return v;
        }
    }
}
