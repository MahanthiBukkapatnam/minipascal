package edu.und.csci465.minipascal.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasmGenerator {

    Map<String, String> typeInfo = new HashMap<>();
    Map<String, Integer> parameterIndex = new HashMap<>();

    private final List<TACInstr> code;
    private final Map<String, Object> env = new HashMap<>();
    private final StringBuilder out = new StringBuilder();

    MasmGenerator(List<TACInstr> code) {
        this.code = code;
    }

    public String generateMasmCode() {
        StringBuffer sb = new StringBuffer();

        preProcess();
        sb.append(generateHeader());
        sb.append(dataSection());
        sb.append(startCode());
        sb.append(startUserWrittenProc());

        sb.append(startProc());
        sb.append(procBody());
        sb.append(endProc());

        return sb.toString();
    }

    public void preProcess() {
        boolean inProc = false;
        for (TACInstr instr : code) {
            if (instr.op.equals("PROC")) {
                inProc = true;
                instr.setInProc(true);
            }
            else if (instr.op.equals("ENDPROC")) {
                instr.setInProc(true);
                inProc = false;
            }
            else {
                instr.setInProc(inProc);
            }
        }
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
        sb.append("    buf BYTE 128 DUP(0)\n");

        for (TACInstr instr : code) {
            if (instr.op.equals("declare")) {
                //Is it already declared?
                if (!typeInfo.keySet().contains(instr.arg1)) {
                    typeInfo.put(instr.arg1, instr.arg2);
                    String variable = instr.arg1;
                    if (variable.equalsIgnoreCase("c")) {
                        variable = TACInstr.MY_C_VARIABLE;
                    }
                    if (instr.arg2.equals("INTEGER")) {
                        sb.append("    " + variable + "    DWORD ?\n");
                    } else if (instr.arg2.equals("BOOLEAN")) {
                        sb.append("    " + variable + "    BYTE ?\n");
                    } else if (instr.arg2.equals("CHAR")) {
                        sb.append("    " + variable + "    BYTE ?\n");
                    } else if (instr.arg2.equals("INTEGER_ARRAY")) {
                        sb.append("    " + variable + "    SDWORD " + instr.result + " DUP(?)\n");
                    }
                }
            } else if (instr.op.equals("declarePrompt")) {
                sb.append("    " + instr.arg1 + " BYTE \"" + instr.arg2 + "\",0\n");
            }
            else if (instr.op.equals("declareParam")) {
                parameterIndex.put(instr.arg1, Integer.parseInt(instr.result));
            }
        }
        sb.append("\n");

        return sb.toString();
    }

    public String startCode() {
        StringBuffer sb = new StringBuffer();
        sb.append(".code\n");
        return sb.toString();
    }

    public String startUserWrittenProc() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n");

        sb.append(";------------------------------------------ Start PROC\n");

        for (TACInstr instr : code) {
            if(instr.isInProc()) {
                String variableType = typeInfo.get(instr.result);
                if(instr.op.equals("PROC") ) {
                    sb.append("" + instr.arg1.toString() + " PROC\n");
                    sb.append("\tpush ebp\n");
                    sb.append("\tmov  ebp, esp\n");
                }
                else if( instr.op.equals("BEGINPROC") ) {

                }
                else if( instr.op.equals("ENDPROC") ) {
                    sb.append("\tpop  ebp\n");
                    sb.append("\tret  8\n");
                    sb.append("" + instr.arg1.toString() + " ENDP\n");
                }
                else {
                    handleTacInstr(instr, sb, variableType);
                }
            }
        }
        sb.append(";------------------------------------------ END PROC\n");

        sb.append("\n");
        return sb.toString();
    }


    public String startProc() {
        StringBuffer sb = new StringBuffer();
        sb.append("start PROC\n");
        sb.append("\n");
        return sb.toString();
    }

    public String procBody() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n");

        for (TACInstr instr : code) {
            if(!instr.isInProc()) {
                String variableType = typeInfo.get(instr.result);
                handleTacInstr(instr, sb, variableType);
            }
        }

        sb.append("\n");
        return sb.toString();
    }

    private void handleTacInstr(TACInstr instr, StringBuffer sb, String variableType) {
        if (instr.op.equals("declare")) {
        } else if (instr.op.equals("declarePrompt")) {
        } else if (instr.op.equals("assignArray")) {
            sb.append("\t;" + instr.toString() + "\n");
            sb.append("\tmov  eax, " + instr.arg2 + "            ; eax = index (1..5)\n");
            sb.append("\tmov  edx, " + instr.arg1 + "            ; edx = value to store\n");
            sb.append("\tmov  " + instr.result + "[eax*4], edx ; values[index] = value   (1-based direct)\n");
            sb.append("\n");
        } else if (instr.op.equals("assign")) {
            sb.append("\t;" + instr.toString() + "\n");
            if (variableType.equals("INTEGER")) {
                if (integerValue(instr.arg1)) {
                    sb.append("\tmov " + instr.result + ", " + instr.arg1 + "\n");
                } else {
                    sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
                    sb.append("\tmov " + instr.result + ", eax\n");
                }
            } else if (variableType.equals("BOOLEAN")) {
                if (instr.arg1.equals("true")) {
                    sb.append("\tmov " + instr.result + ", 1\n");
                } else if (instr.arg1.equals("false")) {
                    sb.append("\tmov " + instr.result + ", 0\n");
                } else {
                    sb.append("\tmov al" + ", " + instr.arg1 + "\n");
                    sb.append("\tmov " + instr.result + ", al\n");
                }
            }
            sb.append("\n");
        } else if (instr.op.equals("computeArrayElem")) {
            sb.append("\t;" + instr.toString() + "\n");
            sb.append("\tmov  eax, " + instr.arg2 + "         ; EAX = num (1..5)\n");
            sb.append("\tmov  edx, " + instr.arg1 + "[eax*4] ; EDX = values[num]\n");
            sb.append("\tmov  " + instr.result + ", edx           ; sum = values[num]\n");
            sb.append("\n");
        } else if (instr.op.equals("+")) {
            sb.append("\t;" + instr.toString() + "\n");
            sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
            sb.append("\tadd eax, " + instr.arg2 + "\n");
            sb.append("\tmov " + instr.result + ", eax\n");
            sb.append("\n");
        } else if (instr.op.equals("-")) {
            sb.append("\t;" + instr.toString() + "\n");
            sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
            sb.append("\tsub eax, " + instr.arg2 + "\n");
            sb.append("\tmov " + instr.result + ", eax\n");
            sb.append("\n");
        } else if (instr.op.equals("*")) {
            sb.append("\t;" + instr.toString() + "\n");
            sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
            sb.append("\tIMUL eax, " + instr.arg2 + "\n");
            sb.append("\tmov " + instr.result + ", eax\n");
            sb.append("\n");
        } else if (instr.op.equals("/")) {
            sb.append("\t;" + instr.toString() + "\n");
            sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
            sb.append("\tCDQ" + "\n");
            sb.append("\tIDIV " + instr.arg2 + "\n");
            sb.append("\tmov " + instr.result + ", eax\n");
            sb.append("\n");
        } else if (instr.op.equals("EQUAL")) {
            sb.append("\t;" + instr.toString() + "\n");
            String arg1Type = typeInfo.get(instr.arg1);

            if (arg1Type.equals("CHAR")) {
                sb.append("\tmov     al, " + instr.arg1 + "      ; load ch\n");
                sb.append("\tcmp     al, '" + instr.arg2 + "'     ; compare with char literal \n");
                sb.append("\tsetz    al            ; AL = 1 if equal, else 0\n");
                sb.append("\tmov     " + instr.result + ", al        ; store boolean result\n");
            } else if (arg1Type.equals("integer")) {
                sb.append("\tmov     al, " + instr.arg1 + "      ; load ch\n");
                sb.append("\tcmp     al, '" + instr.arg2 + "'     ; compare with char literal \n");
                sb.append("\tsetz    al            ; AL = 1 if equal, else 0\n");
                sb.append("\tmovzx   eax, al\n");
                sb.append("\tmov     " + instr.result + ", al        ; store boolean result\n");
            }
            sb.append("\n");
        } else if (instr.op.equals("LESSTHAN")) {
            sb.append("\t;" + instr.toString() + "\n");
            sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
            sb.append("\tcmp eax, " + instr.arg2 + "\n");
            sb.append("\tsetl al" + "\n");
            sb.append("\tmov " + instr.result + ", al\n");
            sb.append("\n");
        } else if (instr.op.equals("LESSEQUAL")) {
            sb.append("\t;" + instr.toString() + "\n");
            sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
            sb.append("\tcmp eax, " + instr.arg2 + "\n");
            sb.append("\tsetle al" + "\n");
            sb.append("\tmov " + instr.result + ", al\n");
            sb.append("\n");
        } else if (instr.op.equals("GREATER")) {
            sb.append("\t;" + instr.toString() + "\n");
            sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
            sb.append("\tcmp eax, " + instr.arg2 + "\n");
            sb.append("\tsetg al" + "\n");
            sb.append("\tmov " + instr.result + ", al\n");
            sb.append("\n");
        } else if (instr.op.equals("GREATEREQUAL")) {
            sb.append("\t;" + instr.toString() + "\n");
            sb.append("\tmov eax" + ", " + instr.arg1 + "\n");
            sb.append("\tcmp eax, " + instr.arg2 + "\n");
            sb.append("\tsetge al" + "\n");
            sb.append("\tmov " + instr.result + ", al\n");
            sb.append("\n");
        } else if (instr.op.equals("and")) {
            sb.append("\t;" + instr.toString() + "\n");
            sb.append("\tmov al" + ", " + instr.arg1 + "\n");
            sb.append("\tand al" + ", " + instr.arg2 + "\n");
            sb.append("\tand al, 1" + "\n");
            sb.append("\tmov " + instr.result + ", al\n");
            sb.append("\n");
        } else if (instr.op.equals("or")) {
            sb.append("\t;" + instr.toString() + "\n");
            sb.append("\tmov al" + ", " + instr.arg1 + "\n");
            sb.append("\tor al" + ", " + instr.arg2 + "\n");
            sb.append("\tand al, 1" + "\n");
            sb.append("\tmov " + instr.result + ", al\n");
            sb.append("\n");
        } else if (instr.op.equals("IfZ")) {
            sb.append("\t;" + instr.toString() + "\n");
            sb.append("\tcmp " + instr.arg1 + ", 0\n");
            sb.append("\tje " + instr.arg2 + "\n");
            sb.append("\n");
        } else if (instr.op.equals("Label")) {
            sb.append("" + instr.arg1 + ":\n");
        } else if (instr.op.equals("GOTO")) {
            sb.append("\tjmp " + instr.arg1 + "\n");
            sb.append("\n");
        } else if (instr.op.equals("read-integer")) {
            sb.append("\t;" + instr.toString() + "\n");
            sb.append("\tcall ReadInt          ; EAX = signed integer" + "\n");
            sb.append("\tmov  " + instr.result + ", eax\n");
            sb.append("\n");
        } else if (instr.op.equals("readln-integer")) {
            sb.append("\t;" + instr.toString() + "\n");

            sb.append("\tmov  edx, OFFSET buf\n");
            sb.append("\tmov  ecx, LENGTHOF buf\n");
            sb.append("\tcall ReadInt       ; EAX <- typed integer\n");
            sb.append("\tmov  " + instr.result + ", eax\n");
            sb.append("\tcall Crlf          ; optional: move to next line\n");
        } else if (instr.op.equals("read-char")) {
            sb.append("\t;" + instr.toString() + "\n");
            sb.append("\tcall ReadChar" + "\n");
            sb.append("\tmov  " + instr.result + ", al\n");
            sb.append("\n");
        } else if (instr.op.equals("readln-char")) {
            sb.append("\t;" + instr.toString() + "\n");

            sb.append("\tmov  edx, OFFSET buf\n");
            sb.append("\tmov  ecx, LENGTHOF buf\n");
            sb.append("\tcall ReadString        ; reads a line (no CR/LF stored), count in EAX\n");
            sb.append("\tmov  al, buf[0]        ; first character of the line\n");
            sb.append("\tmov  " + instr.result + ", al\n");
            sb.append("\n");
        } else if (instr.op.equals("write-char")) {
            sb.append("\tmov  al, " + instr.result + "\n");
            sb.append("\tcall WriteChar" + "\n");
            sb.append("\n");
        } else if (instr.op.equals("write-integer")) {
            if(instr.isInProc() ) {
                String index = "";
                if(parameterIndex.get(instr.result) == 0) {
                    index = "8";
                }
                else if(parameterIndex.get(instr.result) == 1) {
                    index = "12";
                }
                sb.append("\tmov  eax, DWORD PTR [ebp+ " + index + "]\n");
                sb.append("\tcall WriteInt" + "\n");
                sb.append("\n");
            }
            else {
                sb.append("\tmov  eax, " + instr.result + "\n");
                sb.append("\tcall WriteInt" + "\n");
                sb.append("\n");
            }
        } else if (instr.op.equals("write-boolean")) {
            sb.append("\tmovzx  eax, " + instr.result + "\n");
            sb.append("\tcall WriteDec" + "\n");
            sb.append("\n");
        } else if (instr.op.equals("writeln-char")) {
            sb.append("\tmov  al, " + instr.result + "\n");
            sb.append("\tcall WriteChar" + "\n");
            sb.append("\tcall Crlf" + "\n");
            sb.append("\n");
        } else if (instr.op.equals("writeln-integer")) {
            sb.append("\tmov  eax, " + instr.result + "\n");
            sb.append("\tcall WriteInt" + "\n");
            sb.append("\tcall Crlf" + "\n");
            sb.append("\n");
        } else if (instr.op.equals("writeln-boolean")) {
            sb.append("\tmovzx  eax, " + instr.result + "\n");
            sb.append("\tcall WriteDec" + "\n");
            sb.append("\tcall Crlf" + "\n");
            sb.append("\n");
        } else if(instr.op.equals("write")) {
            sb.append("\t;" + instr.toString() + "\n");
            if(instr.arg1!=null && instr.arg1.toString().length() > 0) {
                sb.append("\tmov edx, OFFSET " + instr.arg1 + "\n");
                sb.append("\tcall WriteString\n");
            }
            sb.append("\n");
        } else if(instr.op.equals("writeln")) {
            sb.append("\t;" + instr.toString() + "\n");
            if(instr.arg1!=null && instr.arg1.toString().length() > 0) {
                sb.append("\tmov edx, OFFSET " + instr.arg1 + "\n");
                sb.append("\tcall WriteString\n");
            }
            sb.append("\tcall Crlf" + "\n");
            sb.append("\n");
        } else if(instr.op.equals("PARAM")) {
            sb.append("\t;" + instr.toString() + "\n");
            if(instr.arg1!=null && instr.arg1.toString().length() > 0) {
                sb.append("\tpush " + instr.arg1 + "\n");
            }
            sb.append("\n");
        } else if(instr.op.equals("callProc")) {
            sb.append("\t;" + instr.toString() + "\n");
            if(instr.arg1!=null && instr.arg1.toString().length() > 0) {
                sb.append("\tcall " + instr.arg1 + "\n");
            }
            sb.append("\n");
        } else if(instr.op.equals("declareParam")) {
            sb.append("\t;" + instr.toString() + "\n");
            parameterIndex.put(instr.arg1.toString(), Integer.parseInt(instr.result));
            sb.append("\n");
        } else {
            sb.append(instr.toMyString());
            sb.append("\n");
            sb.append("\n");
        }
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
        } catch (NumberFormatException ex) {
        }
        return false;
    }
}
