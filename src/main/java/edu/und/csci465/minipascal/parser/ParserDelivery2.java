package edu.und.csci465.minipascal.parser;

import edu.und.csci465.minipascal.lexer.Position;
import edu.und.csci465.minipascal.symboltable.GetSymbol;
import edu.und.csci465.minipascal.symboltable.Token;
import edu.und.csci465.minipascal.symboltable.TokenType;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ParserDelivery2 implements IParser {

    private GetSymbol getSymbol;
    private Token lookahead;

    // symbol table: store declared variables (no types needed for now)
    private final Set<String> variables = new LinkedHashSet<>();

    private int tempCount = 0;

    // generated TAC
    private final List<TACInstr> tac = new ArrayList<>();


    public void setGetSymbol(GetSymbol getSymbol) {
        this.getSymbol = getSymbol;
    }

    public GetSymbol getGetSymbol() {
        return getSymbol;
    }

    @Override
    public List<TACInstr> getTac() {
        return tac;
    }

    public void process() {
        try {
            lookahead = getSymbol.getNextToken();
            parseProgram();

            ThreeAddressCode threeAddressCode = new ThreeAddressCode(tac, variables);
            threeAddressCode.print();
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String runInterpreter() {
        TACInterpreter interpreter = new TACInterpreter(tac);
        return interpreter.run();
    }

    private void parseProgram() {
        consume(TokenType.PROGRAMSYM);
        if (lookahead.getType() != TokenType.IDENTIFIER) {
            error("Program name expected");
        }
        consume(TokenType.IDENTIFIER);
        consume(TokenType.SEMICOLON);

        parseVarSection();

        consume(TokenType.BEGINSYM);
        parseStmtList();
        consume(TokenType.ENDSYM);
        consume(TokenType.PERIOD);

        if (lookahead.getType() != TokenType.EOFSYM) {
            error("Extra input after program end");
        }
    }

    private void consume(TokenType expected) {
        if (lookahead.getType() == expected) {
            lookahead = getSymbol.getNextToken();
        } else {
            error("Expected " + expected + " but found " + lookahead);
        }
    }

    private boolean match(TokenType t) {
        if (lookahead.getType() == t) {
            lookahead = getSymbol.getNextToken();
            return true;
        }
        return false;
    }

    private void error(String msg) {
        Position position = lookahead.getPosition();
        throw new RuntimeException("Parse error: " + msg + " at [Line:" + position.getLineNumber() + ",Column:" + position.getColumnNumber() + "]" );
    }

    private String newTemp() {
        tempCount++;
        return "t" + tempCount;
    }

    private void emit(String op, String a1, String a2, String res) {
        tac.add(new TACInstr(op, a1, a2, res));
    }

    private void ensureDeclared(String name) {
        if (!variables.contains(name)) {
            error("Undeclared variable: " + name);
        }
    }

    // varSection ::= 'var' (idList ':' 'integer' ';')*
    private void parseVarSection() {
        consume(TokenType.VARSYM);
        while (lookahead.getType() == TokenType.IDENTIFIER) {
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
        if (lookahead.getType() != TokenType.INTEGERSYM) {
            error("Only 'integer' type is supported");
        }
        consume(TokenType.INTEGERSYM);
        consume(TokenType.SEMICOLON);

        for (String n : names) {
            if (variables.contains(n)) {
                error("Duplicate variable: " + n);
            }
            variables.add(n);
        }
    }

    private String expectId() {
        if (lookahead.getType() != TokenType.IDENTIFIER) {
            error("Identifier expected");
        }
        String name = lookahead.getLexeme();
        consume(TokenType.IDENTIFIER);
        return name;
    }


    // stmtList ::= statement (';' statement)*
    private void parseStmtList() {
        parseStatement();
        while (match(TokenType.SEMICOLON)) {
            if (lookahead.getType() == TokenType.ENDSYM) {
                // allow trailing semicolon before END
                break;
            }
            parseStatement();
        }
    }

    // statement ::= assignment | readStmt | writeStmt
    private void parseStatement() {
        String name = lookahead.getType().name();
        switch (name) {
            case "IDENTIFIER":
                parseAssignment();
                break;
            case "READLNSYM":
                parseReadStmt();
                break;
            case "WRITELNSYM":
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
        consume(TokenType.READLNSYM);
        consume(TokenType.LPAREN);
        String name = expectId();
        ensureDeclared(name);
        consume(TokenType.RPAREN);
        // read name
        emit("read", null, null, name);
    }

    // writeStmt ::= 'writeln' '(' expr ')'
    private void parseWriteStmt() {
        consume(TokenType.WRITELNSYM);
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
        while (lookahead.getType() == TokenType.PLUS
                || lookahead.getType() == TokenType.MINUS) {
            TokenType op = lookahead.getType();
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
        while (lookahead.getType() == TokenType.TIMES || lookahead.getType() == TokenType.DIVSYM) {
            TokenType op = lookahead.getType();
            consume(op);
            String right = parseFactor();
            String t = newTemp();
            if (op == TokenType.TIMES) {
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
        String typeName = lookahead.getType().name();
        switch (typeName) {
            case "IDENTIFIER": {
                String name = lookahead.getLexeme();
                consume(TokenType.IDENTIFIER);
                ensureDeclared(name);
                return name;
            }
            case "NUMBER": {
                int v = lookahead.getValue();
                consume(TokenType.NUMBER);
                String t = newTemp();
                emit("assign", String.valueOf(v), null, t); // t = literal
                return t;
            }
            case "LPAREN":
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
