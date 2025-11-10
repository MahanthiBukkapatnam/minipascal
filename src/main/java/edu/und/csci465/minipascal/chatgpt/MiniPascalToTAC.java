package edu.und.csci465.minipascal.chatgpt;

import java.io.*;
import java.util.*;

/**
 * Mini-Pascal to Three-Address Code compiler
 *
 * Supported subset:
 *
 * program Demo;
 * var
 *   x, y, z : integer;
 * begin
 *   readln(x);
 *   z := x * 2 + 3;
 *   writeln(z)
 * end.
 *
 * Outputs TAC like:
 * read x
 * t1 = x * 2
 * t2 = t1 + 3
 * z = t2
 * print z
 */
public class MiniPascalToTAC {

    // ===== Lexer =====

    enum TokenType {
        PROGRAM, VAR, BEGIN, END, INTEGER, READLN, WRITELN,
        ID, NUMBER,
        SEMI, COLON, COMMA, DOT,
        ASSIGN, // :=
        LPAREN, RPAREN,
        PLUS, MINUS, STAR, SLASH,
        EOF
    }

    static class Token {
        final TokenType type;
        final String text;
        final int intValue; // for NUMBER

        Token(TokenType type, String text) {
            this(type, text, 0);
        }

        Token(TokenType type, String text, int intValue) {
            this.type = type;
            this.text = text;
            this.intValue = intValue;
        }

        @Override
        public String toString() {
            if (type == TokenType.NUMBER) return "NUMBER(" + intValue + ")";
            return type + "(" + text + ")";
        }
    }

    static class Lexer {
        private final String input;
        private int p = 0;
        private final int n;

        Lexer(String input) {
            this.input = input;
            this.n = input.length();
        }

        private char LA(int i) {
            int idx = p + i - 1;
            return (idx >= n) ? '\0' : input.charAt(idx);
        }

        private void consume() {
            p++;
        }

        private void skipWhitespaceAndComments() {
            while (true) {
                // whitespace
                while (Character.isWhitespace(LA(1))) consume();

                // // line comment
                if (LA(1) == '/' && LA(2) == '/') {
                    while (LA(1) != '\n' && LA(1) != '\r' && LA(1) != '\0') consume();
                    continue;
                }

                // { ... } comment
                if (LA(1) == '{') {
                    consume();
                    while (LA(1) != '}' && LA(1) != '\0') consume();
                    if (LA(1) == '}') consume();
                    continue;
                }

                break;
            }
        }

        Token nextToken() {
            skipWhitespaceAndComments();

            char c = LA(1);
            if (c == '\0') {
                return new Token(TokenType.EOF, "");
            }

            // identifier / keyword
            if (Character.isLetter(c) || c == '_') {
                int start = p;
                while (Character.isLetterOrDigit(LA(1)) || LA(1) == '_') consume();
                String text = input.substring(start, p);
                String lower = text.toLowerCase();
                switch (lower) {
                    case "program":  return new Token(TokenType.PROGRAM, text);
                    case "var":      return new Token(TokenType.VAR, text);
                    case "begin":    return new Token(TokenType.BEGIN, text);
                    case "end":      return new Token(TokenType.END, text);
                    case "integer":  return new Token(TokenType.INTEGER, text);
                    case "readln":   return new Token(TokenType.READLN, text);
                    case "writeln":  return new Token(TokenType.WRITELN, text);
                    default:         return new Token(TokenType.ID, text);
                }
            }

            // number
            if (Character.isDigit(c)) {
                int start = p;
                while (Character.isDigit(LA(1))) consume();
                String text = input.substring(start, p);
                int value = Integer.parseInt(text);
                return new Token(TokenType.NUMBER, text, value);
            }

            // symbols
            switch (c) {
                case ';': consume(); return new Token(TokenType.SEMI, ";");
                case ':':
                    if (LA(2) == '=') {
                        consume(); consume();
                        return new Token(TokenType.ASSIGN, ":=");
                    }
                    consume(); return new Token(TokenType.COLON, ":");
                case ',': consume(); return new Token(TokenType.COMMA, ",");
                case '.': consume(); return new Token(TokenType.DOT, ".");
                case '(': consume(); return new Token(TokenType.LPAREN, "(");
                case ')': consume(); return new Token(TokenType.RPAREN, ")");
                case '+': consume(); return new Token(TokenType.PLUS, "+");
                case '-': consume(); return new Token(TokenType.MINUS, "-");
                case '*': consume(); return new Token(TokenType.STAR, "*");
                case '/': consume(); return new Token(TokenType.SLASH, "/");
            }

            throw new RuntimeException("Unexpected character '" + c + "' at position " + p);
        }
    }

    // ===== TAC Representation =====

    static class TACInstr {
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
                case "read":
                    return "read " + result;
                case "print":
                    return "print " + arg1;
                default:
                    return "// ? " + op + " " + arg1 + " " + arg2 + " " + result;
            }
        }
    }

    // ===== Parser + TAC Generator =====

    static class CompilerCore {
        private final Lexer lexer;
        private Token lookahead;

        // symbol table: store declared variables (no types needed for now)
        private final Set<String> variables = new LinkedHashSet<>();

        // generated TAC
        private final List<TACInstr> tac = new ArrayList<>();

        // temp counter
        private int tempCount = 0;

        CompilerCore(String source) {
            this.lexer = new Lexer(source);
            this.lookahead = lexer.nextToken();
        }

        List<TACInstr> compile() {
            parseProgram();
            return tac;
        }

        // ---- utilities ----

        private void consume(TokenType expected) {
            if (lookahead.type == expected) {
                lookahead = lexer.nextToken();
            } else {
                error("Expected " + expected + " but found " + lookahead);
            }
        }

        private boolean match(TokenType t) {
            if (lookahead.type == t) {
                lookahead = lexer.nextToken();
                return true;
            }
            return false;
        }

        private void error(String msg) {
            throw new RuntimeException("Parse error: " + msg + " at token " + lookahead);
        }

        private String newTemp() {
            tempCount++;
            return "t" + tempCount;
        }

        private void ensureDeclared(String name) {
            if (!variables.contains(name)) {
                error("Undeclared variable: " + name);
            }
        }

        private String expectId() {
            if (lookahead.type != TokenType.ID) {
                error("Identifier expected");
            }
            String name = lookahead.text;
            consume(TokenType.ID);
            return name;
        }

        private void emit(String op, String a1, String a2, String res) {
            tac.add(new TACInstr(op, a1, a2, res));
        }

        // ---- grammar ----

        // program ::= 'program' ID ';' 'var' varDecls 'begin' stmtList 'end' '.'
        private void parseProgram() {
            consume(TokenType.PROGRAM);
            if (lookahead.type != TokenType.ID) {
                error("Program name expected");
            }
            consume(TokenType.ID);
            consume(TokenType.SEMI);

            parseVarSection();

            consume(TokenType.BEGIN);
            parseStmtList();
            consume(TokenType.END);
            consume(TokenType.DOT);

            if (lookahead.type != TokenType.EOF) {
                error("Extra input after program end");
            }
        }

        // varSection ::= 'var' (idList ':' 'integer' ';')*
        private void parseVarSection() {
            consume(TokenType.VAR);
            while (lookahead.type == TokenType.ID) {
                parseVarDecl();
            }
        }

        private void parseVarDecl() {
            List<String> names = new ArrayList<>();
            names.add(expectId());
            while (match(TokenType.COMMA)) {
                names.add(expectId());
            }
            consume(TokenType.COLON);
            if (lookahead.type != TokenType.INTEGER) {
                error("Only 'integer' type is supported");
            }
            consume(TokenType.INTEGER);
            consume(TokenType.SEMI);

            for (String n : names) {
                if (variables.contains(n)) {
                    error("Duplicate variable: " + n);
                }
                variables.add(n);
            }
        }

        // stmtList ::= statement (';' statement)*
        private void parseStmtList() {
            parseStatement();
            while (match(TokenType.SEMI)) {
                if (lookahead.type == TokenType.END) {
                    // allow trailing semicolon before END
                    break;
                }
                parseStatement();
            }
        }

        // statement ::= assignment | readStmt | writeStmt
        private void parseStatement() {
            switch (lookahead.type) {
                case ID:
                    parseAssignment();
                    break;
                case READLN:
                    parseReadStmt();
                    break;
                case WRITELN:
                    parseWriteStmt();
                    break;
                default:
                    error("Statement expected");
            }
        }

        // assignment ::= ID ':=' expr
        private void parseAssignment() {
            String name = expectId();
            ensureDeclared(name);
            consume(TokenType.ASSIGN);
            String src = parseExpr();          // src is a var, number, or temp
            // name = src
            emit("assign", src, null, name);
        }

        // readStmt ::= 'readln' '(' ID ')'
        private void parseReadStmt() {
            consume(TokenType.READLN);
            consume(TokenType.LPAREN);
            String name = expectId();
            ensureDeclared(name);
            consume(TokenType.RPAREN);
            // read name
            emit("read", null, null, name);
        }

        // writeStmt ::= 'writeln' '(' expr ')'
        private void parseWriteStmt() {
            consume(TokenType.WRITELN);
            consume(TokenType.LPAREN);
            String v = parseExpr();
            consume(TokenType.RPAREN);
            // print v
            emit("print", v, null, null);
        }

        // expr ::= term ( ('+' | '-') term)*
        //          1 term
        //          followed by 0 or more + | - term
        private String parseExpr() {
            String left = parseTerm();
            while (lookahead.type == TokenType.PLUS || lookahead.type == TokenType.MINUS) {
                TokenType op = lookahead.type;
                consume(op);
                String right = parseTerm();
                String t = newTemp();
                if (op == TokenType.PLUS) {
                    emit("+", left, right, t);
                } else {
                    emit("-", left, right, t);
                }
                left = t;
            }
            return left;
        }

        // term ::= factor (('*' | '/') factor)*
        //          1 factor
        //          followed by 0 or more * | / factor
        private String parseTerm() {
            String left = parseFactor();
            while (lookahead.type == TokenType.STAR || lookahead.type == TokenType.SLASH) {
                TokenType op = lookahead.type;
                consume(op);
                String right = parseFactor();
                String t = newTemp();
                if (op == TokenType.STAR) {
                    emit("*", left, right, t);
                } else {
                    emit("/", left, right, t);
                }
                left = t;
            }
            return left;
        }

        // factor ::= ID | NUMBER | '(' expr ')'
        private String parseFactor() {
            switch (lookahead.type) {
                case ID: {
                    String name = lookahead.text;
                    consume(TokenType.ID);
                    ensureDeclared(name);
                    return name;
                }
                case NUMBER: {
                    int v = lookahead.intValue;
                    consume(TokenType.NUMBER);
                    String t = newTemp();
                    emit("assign", String.valueOf(v), null, t); // t = literal
                    return t;
                }
                case LPAREN:
                    consume(TokenType.LPAREN);
                    String inner = parseExpr();
                    consume(TokenType.RPAREN);
                    return inner;
                default:
                    error("Expression expected");
                    return null; // unreachable
            }
        }
    }

    // ===== Driver =====

    public static void main(String[] args) throws Exception {
        // Read entire Mini-Pascal program from stdin
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        String source = sb.toString();

        CompilerCore compiler = new CompilerCore(source);
        List<TACInstr> tac = compiler.compile();

        // Print TAC
        for (TACInstr i : tac) {
            System.out.println(i);
        }
    }
}
