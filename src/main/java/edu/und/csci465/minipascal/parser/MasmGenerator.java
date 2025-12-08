package edu.und.csci465.minipascal.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasmGenerator {

    Map<String, String> typeInfo = new HashMap<>();

    private final List<TACInstr> code;
    private final Map<String, Object> env = new HashMap<>();
    private final StringBuilder out = new StringBuilder();

    MasmGenerator(List<TACInstr> code) {
        this.code = code;
    }

    public String generateMasmCode() {
        StringBuffer sb = new StringBuffer();

        sb.append( generateHeader() );
        sb.append( dataSection() );
        sb.append( startProc() );
        sb.append( procBody() );
        sb.append( endProc() );

        return sb.toString();
    }

    public String generateHeader() {
        StringBuffer sb = new StringBuffer();

        sb.append(".386\n");
        sb.append(".model flat, stdcall\n");
        sb.append("option casemap:none\n");
        sb.append("\n");

        sb.append("include \\masm32\\include\\Irvine32.inc\n");
        sb.append("includelib \\masm32\\lib\\Irvine32.lib\n");
        sb.append("includelib \\masm32\\lib\\kernel32.lib\n");
        sb.append("includelib \\masm32\\lib\\user32.lib\n");
        sb.append("\n");

        return sb.toString();
    }

    public String dataSection() {
        StringBuffer sb = new StringBuffer();

        sb.append(".data\n");
        for (TACInstr instr : code) {
            if (instr.op.equals("declare")) {
                //Is it already declared?
                if (!typeInfo.keySet().contains(instr.arg1)) {
                    typeInfo.put(instr.arg1, instr.arg2);
                    String variable = instr.arg1;
                    if (variable.equalsIgnoreCase("c")) {
                        variable = TACInstr.MY_C_VARIABLE;
                    }
                    if (instr.arg2.equals("integer")) {
                        sb.append("    " + variable + "    DWORD ?\n");
                    } else if (instr.arg2.equals("boolean")) {
                        sb.append("    " + variable + "    BYTE ?\n");
                    } else if (instr.arg2.equals("char")) {
                        sb.append("    " + variable + "    BYTE ?\n");
                    }
                }
            } else if (instr.op.equals("declarePrompt")) {
                //sb.append("    " + instr.arg1 + " BYTE \"" + instr.arg2 + "\",0\n");
            }
        }
        sb.append("\n");

        return sb.toString();
    }

    public String startProc() {
        StringBuffer sb = new StringBuffer();

        sb.append(".code\n");

        sb.append("start PROC\n");
        sb.append("\n");

        return sb.toString();
    }

    public String procBody() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n");

        for (TACInstr instr : code) {
            String variableType = typeInfo.get(instr.result);

            if(instr.op.equals("declare")) {
            }
            else if(instr.op.equals("declarePrompt")) {
            }
            else if(instr.op.equals("assign")) {
                sb.append("\t;" + instr.toString() + "\n");
                if(variableType.equals("integer")) {
                    if( integerValue(instr.arg1) ) {
                        sb.append("\tmov " + instr.result + ", " + instr.arg1 + "\n");
                    }
                    else {
                        sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
                        sb.append("\tmov " + instr.result + ", eax\n");
                    }
                }
                else if(variableType.equals("boolean")) {
                    if(instr.arg1.equals("true") ) {
                        sb.append("\tmov " + instr.result + ", 1\n");
                    }
                    else if(instr.arg1.equals("false") ) {
                        sb.append("\tmov " + instr.result + ", 0\n");
                    }
                    else {
                        sb.append("\tmov al" + ", " + instr.arg1 + "\n");
                        sb.append("\tmov " + instr.result + ", al\n");
                    }
                }
                sb.append("\n");
            }
            else if(instr.op.equals("+")) {
                sb.append("\t;" + instr.toString() + "\n");
                sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
                sb.append("\tadd eax, " + instr.arg2 + "\n");
                sb.append("\tmov " + instr.result + ", eax\n");
                sb.append("\n");
            }
            else if(instr.op.equals("-")) {
                sb.append("\t;" + instr.toString() + "\n");
                sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
                sb.append("\tsub eax, " + instr.arg2 + "\n");
                sb.append("\tmov " + instr.result + ", eax\n");
                sb.append("\n");
            }
            else if(instr.op.equals("*")) {
                sb.append("\t;" + instr.toString() + "\n");
                sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
                sb.append("\tIMUL eax, " + instr.arg2 + "\n");
                sb.append("\tmov " + instr.result + ", eax\n");
                sb.append("\n");
            }
            else if(instr.op.equals("/")) {
                sb.append("\t;" + instr.toString() + "\n");
                sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
                sb.append("\tCDQ" + "\n");
                sb.append("\tIDIV " + instr.arg2 + "\n");
                sb.append("\tmov " + instr.result + ", eax\n");
                sb.append("\n");
            }
            else if(instr.op.equals("EQUAL")) {
                sb.append("\t;" + instr.toString() + "\n");
                String arg1Type = typeInfo.get(instr.arg1);

                if(arg1Type.equals("char")) {
                    sb.append("\tmov     al, "  + instr.arg1  + "      ; load ch\n");
                    sb.append("\tcmp     al, '" + instr.arg2 +  "'     ; compare with char literal \n");
                    sb.append("\tsetz    al            ; AL = 1 if equal, else 0\n");
                    sb.append("\tmov     " + instr.result + ", al        ; store boolean result\n");
                }
                else if(arg1Type.equals("integer")) {
                    sb.append("\tmov     al, "  + instr.arg1  + "      ; load ch\n");
                    sb.append("\tcmp     al, '" + instr.arg2 +  "'     ; compare with char literal \n");
                    sb.append("\tsetz    al            ; AL = 1 if equal, else 0\n");
                    sb.append("\tmovzx   eax, al\n");
                    sb.append("\tmov     " + instr.result + ", al        ; store boolean result\n");
                }
                sb.append("\n");
            }
            else if(instr.op.equals("LESSTHAN")) {
                sb.append("\t;" + instr.toString() + "\n");
                sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
                sb.append("\tcmp eax, " + instr.arg2 + "\n");
                sb.append("\tsetl al" + "\n");
                sb.append("\tmov " + instr.result + ", al\n");
                sb.append("\n");
            }
            else if(instr.op.equals("LESSEQUAL")) {
                sb.append("\t;" + instr.toString() + "\n");
                sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
                sb.append("\tcmp eax, " + instr.arg2 + "\n");
                sb.append("\tsetle al" + "\n");
                sb.append("\tmov " + instr.result + ", al\n");
                sb.append("\n");
            }
            else if(instr.op.equals("GREATER")) {
                sb.append("\t;" + instr.toString() + "\n");
                sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
                sb.append("\tcmp eax, " + instr.arg2 + "\n");
                sb.append("\tsetg al" + "\n");
                sb.append("\tmov " + instr.result + ", al\n");
                sb.append("\n");
            }
            else if(instr.op.equals("GREATEREQUAL")) {
                sb.append("\t;" + instr.toString() + "\n");
                sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
                sb.append("\tcmp eax, " + instr.arg2 + "\n");
                sb.append("\tsetge al" + "\n");
                sb.append("\tmov " + instr.result + ", al\n");
                sb.append("\n");
            }
            else if(instr.op.equals("and")) {
                sb.append("\t;" + instr.toString() + "\n");
                sb.append("\tmov al" + ", " + instr.arg1 + "\n");
                sb.append("\tand al" + ", " + instr.arg2 + "\n");
                sb.append("\tand al, 1" + "\n");
                sb.append("\tmov " + instr.result + ", al\n");
                sb.append("\n");
            }
            else if(instr.op.equals("or")) {
                sb.append("\t;" + instr.toString() + "\n");
                sb.append("\tmov al" + ", " + instr.arg1 + "\n");
                sb.append("\tor al" + ", " + instr.arg2 + "\n");
                sb.append("\tand al, 1" + "\n");
                sb.append("\tmov " + instr.result + ", al\n");
                sb.append("\n");
            }
            else if(instr.op.equals("IfZ")) {
                sb.append("\t;" + instr.toString() + "\n");
                sb.append("\tcmp " + instr.arg1 + ", 0\n");
                sb.append("\tje " + instr.arg2 +"\n");
                sb.append("\n");
            }
            else if(instr.op.equals("Label")) {
                sb.append("" + instr.arg1 + ":\n");
            }
            else if(instr.op.equals("GOTO")) {
                sb.append("\tjmp " + instr.arg1 + "\n");
                sb.append("\n");
            }
            else if(instr.op.equals("read")) {
//                sb.append("\t;" + instr.toString() + "\n");
//                sb.append("\tmov eax, " + instr.arg1 + "\n");
//                sb.append("\tcall WriteDec\n");
//                //sb.append("\tcall Crlf\n");
//                sb.append("\n");
            }
            else if(instr.op.equals("print")) {
                sb.append("\t;" + instr.toString() + "\n");
                sb.append("\tmov eax, " + instr.arg1 + "\n");
                sb.append("\tcall WriteDec\n");
                sb.append("\tcall Crlf\n");
                sb.append("\n");
            }
            else {
                sb.append(instr.toMyString());
                sb.append("\n");
                sb.append("\n");
            }
        }

        sb.append("\n");
        return sb.toString();
    }

    public String endProc() {
        StringBuffer sb = new StringBuffer();

        sb.append("    exit\n");
        sb.append("start ENDP\n");
        sb.append("END start\n");
        sb.append("\n");

        return sb.toString();
    }

    boolean integerValue(String value) {
        try {
            Integer.parseInt(value);
            return true;
        }
        catch(NumberFormatException ex) {
        }
        return false;
    }
}
