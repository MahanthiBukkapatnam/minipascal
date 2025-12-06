package edu.und.csci465.minipascal.parser;

class TACInstr {
        // For binary: result = arg1 op arg2  (op in {"+", "*", "/"})
        // For assign: result = arg1          (op = "assign")
        // For read:   read result            (op = "read")
        // For print:  print arg1             (op = "print")
        final String op;
        final String arg1;
        final String arg2;
        final String result;

        TACInstr(String op, String arg1, String arg2, String result) {
            this.op = op;
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.result = result;
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
                case "print":
                    return "print " + arg1;
                default:
                    return "// ? " + op + " " + arg1 + " " + arg2 + " " + result;
            }
        }
    }