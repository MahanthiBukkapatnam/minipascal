package edu.und.csci465.minipascal.chatgpt;

import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * ChatGPT Prompt:  "Write a Lexer in Java for Mini Pascal"
 *
 * MiniPascal Lexer (Tokenizer)
 * - Single file, no dependencies
 * - Line/column tracking
 * - Supports Pascal-style literals, comments, and operators
 */
public class MiniPascalLexer {

    // ===== Token Types =====
    public enum TokenType {
        // Single-character punctuation
        PLUS, MINUS, STAR, SLASH,
        LPAREN, RPAREN, LBRACKET, RBRACKET,
        COMMA, SEMICOLON, COLON,
        DOT, RANGE, // . and ..
        // Relational & assignment
        EQUAL, LT, GT, LE, GE, NEQ, ASSIGN, // = < > <= >= <> :=
        // Literals & identifiers
        IDENT, INT, REAL, CHAR, STRING,
        // Keywords
        PROGRAM, VAR, BEGIN, END, IF, THEN, ELSE, WHILE, DO,
        PROCEDURE, FUNCTION, ARRAY, OF, INTEGER, CHAR_KW,
        READ, READLN, WRITE, WRITELN,
        AND, OR, NOT, DIV, MOD, CHR, ORD,
        // End of file / errors
        EOF
    }

    // ===== Token =====
    public static final class Token {
        public final TokenType type;
        public final String lexeme;   // exact text (for literals/idents/operators)
        public final int line;
        public final int column;

        public Token(TokenType type, String lexeme, int line, int column) {
            this.type = type;
            this.lexeme = lexeme;
            this.line = line;
            this.column = column;
        }

        @Override public String toString() {
            return String.format("%-12s  %-12s  (line %d, col %d)", type, printable(lexeme), line, column);
        }

        private String printable(String s) {
            if (s == null) return "";
            return s.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
        }
    }

    // ===== Lexer State =====
    private final String src;
    private int index = 0;
    private int line = 1;
    private int col = 1;

    // For error messages
    public static class LexicalException extends RuntimeException {
        public final int line, column;
        public LexicalException(String msg, int line, int column) {
            super("Lexical error at line " + line + ", col " + column + ": " + msg);
            this.line = line;
            this.column = column;
        }
    }

    // Keywords map (lowercase)
    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();
    static {
        // core
        KEYWORDS.put("program", TokenType.PROGRAM);
        KEYWORDS.put("var", TokenType.VAR);
        KEYWORDS.put("begin", TokenType.BEGIN);
        KEYWORDS.put("end", TokenType.END);
        KEYWORDS.put("if", TokenType.IF);
        KEYWORDS.put("then", TokenType.THEN);
        KEYWORDS.put("else", TokenType.ELSE);
        KEYWORDS.put("while", TokenType.WHILE);
        KEYWORDS.put("do", TokenType.DO);
        KEYWORDS.put("procedure", TokenType.PROCEDURE);
        KEYWORDS.put("function", TokenType.FUNCTION);
        KEYWORDS.put("array", TokenType.ARRAY);
        KEYWORDS.put("of", TokenType.OF);
        KEYWORDS.put("integer", TokenType.INTEGER);
        KEYWORDS.put("char", TokenType.CHAR_KW);
        // I/O
        KEYWORDS.put("read", TokenType.READ);
        KEYWORDS.put("readln", TokenType.READLN);
        KEYWORDS.put("write", TokenType.WRITE);
        KEYWORDS.put("writeln", TokenType.WRITELN);
        // ops
        KEYWORDS.put("and", TokenType.AND);
        KEYWORDS.put("or", TokenType.OR);
        KEYWORDS.put("not", TokenType.NOT);
        KEYWORDS.put("div", TokenType.DIV);
        KEYWORDS.put("mod", TokenType.MOD);
        // functions
        KEYWORDS.put("chr", TokenType.CHR);
        KEYWORDS.put("ord", TokenType.ORD);
    }

    public MiniPascalLexer(String src) {
        this.src = Objects.requireNonNull(src);
    }

    // ===== Public API =====
    public List<Token> tokenizeAll() {
        List<Token> tokens = new ArrayList<>();
        Token t;
        do {
            t = nextToken();
            tokens.add(t);
        } while (t.type != TokenType.EOF);
        return tokens;
    }

    public Token nextToken() {
        skipWhitespaceAndComments();
        int startLine = line, startCol = col;

        if (isAtEnd()) return new Token(TokenType.EOF, "", startLine, startCol);

        char c = peek();

        // Identifiers / Keywords
        if (isAlpha(c)) {
            String ident = readIdentifier();
            TokenType kw = KEYWORDS.get(ident.toLowerCase());
            if (kw != null) return new Token(kw, ident, startLine, startCol);
            return new Token(TokenType.IDENT, ident, startLine, startCol);
        }

        // Numbers (integer or real)
        if (Character.isDigit(c)) {
            return readNumber(startLine, startCol);
        }

        // Strings / chars: Pascal uses single quotes. '' inside string = escaped quote.
        if (c == '\'') {
            return readQuotedLiteral(startLine, startCol);
        }

        // Operators / punctuation with lookahead
        switch (c) {
            case '+': advance(); return new Token(TokenType.PLUS, "+", startLine, startCol);
            case '-': advance(); return new Token(TokenType.MINUS, "-", startLine, startCol);
            case '*': advance(); return new Token(TokenType.STAR, "*", startLine, startCol);
            case '/': {
                // We already handled comments; '/' here is division
                advance(); return new Token(TokenType.SLASH, "/", startLine, startCol);
            }
            case '(': {
                advance();
                return new Token(TokenType.LPAREN, "(", startLine, startCol);
            }
            case ')': advance(); return new Token(TokenType.RPAREN, ")", startLine, startCol);
            case '[': advance(); return new Token(TokenType.LBRACKET, "[", startLine, startCol);
            case ']': advance(); return new Token(TokenType.RBRACKET, "]", startLine, startCol);
            case ',': advance(); return new Token(TokenType.COMMA, ",", startLine, startCol);
            case ';': advance(); return new Token(TokenType.SEMICOLON, ";", startLine, startCol);
            case ':': {
                advance();
                if (match('=')) return new Token(TokenType.ASSIGN, ":=", startLine, startCol);
                return new Token(TokenType.COLON, ":", startLine, startCol);
            }
            case '.': {
                advance();
                if (match('.')) return new Token(TokenType.RANGE, "..", startLine, startCol);
                return new Token(TokenType.DOT, ".", startLine, startCol);
            }
            case '=': advance(); return new Token(TokenType.EQUAL, "=", startLine, startCol);
            case '<': {
                advance();
                if (match('=')) return new Token(TokenType.LE, "<=", startLine, startCol);
                if (match('>')) return new Token(TokenType.NEQ, "<>", startLine, startCol);
                return new Token(TokenType.LT, "<", startLine, startCol);
            }
            case '>': {
                advance();
                if (match('=')) return new Token(TokenType.GE, ">=", startLine, startCol);
                return new Token(TokenType.GT, ">", startLine, startCol);
            }
            default:
                // Unknown character
                String ch = String.valueOf(c);
                advance();
                throw new LexicalException("Unexpected character: '" + ch + "'", startLine, startCol);
        }
    }

    // ===== Helpers: identifiers =====
    private String readIdentifier() {
        StringBuilder sb = new StringBuilder();
        while (!isAtEnd() && isAlphaNum(peek())) {
            sb.append(advance());
        }
        return sb.toString();
    }

    private boolean isAlpha(char c) {
        return Character.isLetter(c) || c == '_';
    }

    private boolean isAlphaNum(char c) {
        return isAlpha(c) || Character.isDigit(c);
    }

    // ===== Helpers: numbers =====
    private Token readNumber(int startLine, int startCol) {
        StringBuilder sb = new StringBuilder();
        // integer part
        while (!isAtEnd() && Character.isDigit(peek())) sb.append(advance());

        boolean isReal = false;

        // fractional part .digits
        if (!isAtEnd() && peek() == '.' && nextCharIsDigit()) {
            isReal = true;
            sb.append(advance()); // dot
            if (isAtEnd() || !Character.isDigit(peek())) {
                throw new LexicalException("Malformed real literal (expected digits after '.')", line, col);
            }
            while (!isAtEnd() && Character.isDigit(peek())) sb.append(advance());
        }

        // exponent part e[+/-]?digits
        if (!isAtEnd() && (peek() == 'e' || peek() == 'E')) {
            isReal = true;
            sb.append(advance());
            if (!isAtEnd() && (peek() == '+' || peek() == '-')) sb.append(advance());
            if (isAtEnd() || !Character.isDigit(peek())) {
                throw new LexicalException("Malformed exponent (expected digits after 'e')", line, col);
            }
            while (!isAtEnd() && Character.isDigit(peek())) sb.append(advance());
        }

        String lex = sb.toString();
        return new Token(isReal ? TokenType.REAL : TokenType.INT, lex, startLine, startCol);
    }

    private boolean nextCharIsDigit() {
        if (index + 1 >= src.length()) return false;
        char n = src.charAt(index + 1);
        return Character.isDigit(n);
    }

    // ===== Helpers: strings & chars (single quotes) =====
    private Token readQuotedLiteral(int startLine, int startCol) {
        // Pascal strings and chars both use single quotes. A doubled single quote ('') is an escaped quote.
        StringBuilder sb = new StringBuilder();
        advance(); // consume starting '

        while (!isAtEnd()) {
            char c = advance();
            if (c == '\'') {
                if (match('\'')) {
                    // doubled quote -> literal quote char
                    sb.append('\'');
                    continue;
                } else {
                    // end of literal
                    String lex = sb.toString();
                    // Distinguish CHAR vs STRING by length 1
                    if (lex.length() == 1) {
                        return new Token(TokenType.CHAR, lex, startLine, startCol);
                    } else {
                        return new Token(TokenType.STRING, lex, startLine, startCol);
                    }
                }
            }
            if (c == '\n' || c == '\r') {
                throw new LexicalException("Unterminated string/char literal before end of line", startLine, startCol);
            }
            sb.append(c);
        }
        throw new LexicalException("Unterminated string/char literal at EOF", startLine, startCol);
    }

    // ===== Skipping whitespace and comments =====
    private void skipWhitespaceAndComments() {
        boolean again;
        do {
            again = false;
            // whitespace
            while (!isAtEnd() && Character.isWhitespace(peek())) {
                advance();
            }
            if (isAtEnd()) return;

            // line comments //
            if (peek() == '/' && peekNext() == '/') {
                while (!isAtEnd() && peek() != '\n' && peek() != '\r') advance();
                again = true;
                continue;
            }
            // block comments { ... }
            if (peek() == '{') {
                consumeBraceComment();
                again = true;
                continue;
            }
            // block comments (* ... *)
            if (peek() == '(' && peekNext() == '*') {
                consumeParenStarComment();
                again = true;
            }
        } while (again);
    }

    private void consumeBraceComment() {
        int startL = line, startC = col;
        advance(); // consume '{'
        while (!isAtEnd()) {
            char c = advance();
            if (c == '}') return;
        }
        throw new LexicalException("Unterminated { ... } comment", startL, startC);
    }

    private void consumeParenStarComment() {
        int startL = line, startC = col;
        advance(); // '('
        advance(); // '*'
        while (!isAtEnd()) {
            char c = advance();
            if (c == '*' && match(')')) return;
        }
        throw new LexicalException("Unterminated (* ... *) comment", startL, startC);
    }

    // ===== Character navigation with line/column tracking =====
    private boolean isAtEnd() {
        return index >= src.length();
    }

    private char peek() {
        return src.charAt(index);
    }

    private char peekNext() {
        if (index + 1 >= src.length()) return '\0';
        return src.charAt(index + 1);
    }

    private boolean match(char expected) {
        if (isAtEnd() || src.charAt(index) != expected) return false;
        advance();
        return true;
    }

    private char advance() {
        char c = src.charAt(index++);
        if (c == '\r') {
            // Handle CR, CRLF, or lone CR
            if (!isAtEnd() && src.charAt(index) == '\n') {
                index++; // consume LF of CRLF
                line++; col = 1;
                return '\n'; // normalize internally
            } else {
                line++; col = 1;
                return '\n';
            }
        } else if (c == '\n') {
            line++; col = 1;
        } else {
            col++;
        }
        return c;
    }

    // ===== Demo main =====
    public static void main(String[] args) throws Exception {
//        if (args.length == 0) {
//            System.err.println("Usage: java MiniPascalLexer <path-to-source.pas>");
//            System.exit(1);
//        }
        //String path = args[0];
        String path = "src/test/resources/pascalPrograms/valid/sample2.txt";

        // Read bytes to preserve original EOLs exactly
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String source = new String(bytes, StandardCharsets.UTF_8);

        MiniPascalLexer lexer = new MiniPascalLexer(source);
        try {
            for (Token t : lexer.tokenizeAll()) {
                System.out.println(t);
            }
        } catch (LexicalException e) {
            System.err.println(e.getMessage());
            System.exit(2);
        }
    }
}
