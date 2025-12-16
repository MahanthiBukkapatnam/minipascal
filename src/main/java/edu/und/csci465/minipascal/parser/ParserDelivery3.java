package edu.und.csci465.minipascal.parser;

import edu.und.csci465.minipascal.lexer.Position;
import edu.und.csci465.minipascal.symboltable.*;

import java.util.*;

public class ParserDelivery3 implements IParser {

    private GetSymbol getSymbol;
    private Token lookahead;

    // symbol table: store declared variables (no types needed for now)
    private SymbolTable symbolTable = new SymbolTable();
//    private final Set<String> variables = new LinkedHashSet<>();
//    private final Map<String, VariableType> variableTypeMap = new HashMap<>();
    private int parameterIndex = 0;

    private int tempCount = 0;
    private int locatorCount = 0;
    private int promptCount=0;

    // generated TAC
    private List<TACInstr> tac = new ArrayList<>();
    private List<TACInstr> variablesTac = new ArrayList<>();


    public void setGetSymbol(GetSymbol getSymbol) {
        this.getSymbol = getSymbol;
    }

    public GetSymbol getGetSymbol() {
        return getSymbol;
    }


    boolean lookAheadIs(TokenType tokenType) {
        return lookahead.getType() == tokenType;
    }

    public void process() {
        try {
            lookahead = getSymbol.getNextToken();
            symbolTable.openScope(0);
            parseProgram();
            ThreeAddressCode threeAddressCode = new ThreeAddressCode(tac, symbolTable);
            threeAddressCode.print();
            symbolTable.closeScope();
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public List<TACInstr> getTac() {
        return tac;
    }

    public String runInterpreter() {
        TACInterpreter interpreter = new TACInterpreter(tac);
        try {
            return interpreter.run();
        }
        catch (Exception ex) {
            return interpreter.evaluateBooleanExpr();
        }
    }

    private void parseProgram() {
        consume(TokenType.PROGRAMSYM);
        if (lookahead.getType() != TokenType.IDENTIFIER) {
            error("Program name expected");
        }
        consume(TokenType.IDENTIFIER);
        consume(TokenType.SEMICOLON);

        // zero or more procedure declarations
        parseProcedureSection();

        if (lookahead.getType() == TokenType.VARSYM) {
            variablesTac = parseVarSection();
        }

        this.tac.addAll(variablesTac);
        parseBlock();
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
        throw new RuntimeException("Parse error: [" + msg + "] at [Line:" + (position.getLineNumber()+1) + ",Column:" + (position.getColumnNumber()+1) + "]" );
    }

    private String newTemp() {
        tempCount++;
        return "t" + tempCount;
    }

    private String newLocator() {
        locatorCount++;
        return "L" + locatorCount;
    }

    private String newPrompt() {
        promptCount++;
        return "prompt" + promptCount;
    }

    private void emit(String op, String a1, String a2, String res) {
        tac.add(new TACInstr(op, a1, a2, res));
    }

    private void ensureDeclared(String name) {
        if(!symbolTable.isSymbolDeclared(name)) {
            error("Undeclared variable: " + name);
        }
    }

    private void ensureDeclared(String name, VariableType variableType) {
        if(!symbolTable.isSymbolDeclared(name)) {
            error("Undeclared variable: " + name);
        }
        Symbol nameSymbol = symbolTable.findSymbol(name);
        if(nameSymbol.getVariableType() != variableType) {
            error("Type Mismatch: Expected=" + variableType.name() + ", but it is declared as " + nameSymbol.getVariableType().name());
        }
    }

    void parseProcedureSection() {
        while (lookahead.getType() == TokenType.PROCEDURESYM) {
            parseProcedureDecl();
        }
    }


    // procedureDecl  -> PROCEDURE id formalParams? ';' varSection? compoundStmt ';'
    private void parseProcedureDecl() {
        consume(TokenType.PROCEDURESYM);
        String procName = "";
        if(lookahead.getType()==TokenType.IDENTIFIER) {
            procName = lookahead.getLexeme();
        }
        consume(TokenType.IDENTIFIER);

        parameterIndex=0;
        // New scope for params + locals
        Procedure procedure = symbolTable.addProcedure(procName);
        symbolTable.openScope(0);       //?

        // Optional formal parameter list: (a: integer; b: char)
        if (lookahead.getType() == TokenType.LPAREN) {
            parseFormalParams(procedure);     // declares params in current scope
        }

        consume(TokenType.SEMICOLON);

        // Optional local var section inside the procedure
        if (lookahead.getType() == TokenType.VARSYM) {
            variablesTac = parseVarSection();       // reuse your existing var parser
        }
        this.tac.addAll(variablesTac);

        emit("PROC", procName, null, null);
        emit("BEGINPROC", procName, null, null );
        // Body: begin ... end
        parseBlock();
        emit("ENDPROC", procName, null, null );

        // Procedure declaration ends with ';'
        consume(TokenType.SEMICOLON);

        symbolTable.closeScope();
    }

    /**
     * formalParams -> '(' paramGroup (';' paramGroup)* ')'
     * paramGroup   -> id (',' id)* ':' type
     */
    private void parseFormalParams(Procedure procedure) {
        consume(TokenType.LPAREN);

        // Empty param list not allowed in standard Pascal, but handle gracefully:
        if (lookahead.getType() != TokenType.RPAREN) {
            parseParamGroup(procedure);
            while (lookahead.getType() == TokenType.SEMICOLON) {
                consume(TokenType.SEMICOLON);
                parseParamGroup(procedure);
            }
        }

        consume(TokenType.RPAREN);
    }

    private void parseParamGroup(Procedure procedure) {
        // Collect identifiers: a, b, c
        List<String> names = new ArrayList<>();
        String id = expectId();
        names.add(id);
        procedure.addParameter(new Variable(id,VariableType.INTEGER));  //This should be dynamic
        while (match(TokenType.COMMA)) {
            id = expectId();
            names.add(id);
            procedure.addParameter(new Variable(id,VariableType.INTEGER));  //This should be dynamic, not harded coded as INTEGER
        }

        consume(TokenType.COLON);

        Expr arrayExprEnd = new Expr();             //This is Code duplication
        VariableType variableType = null;
        if(lookahead.getType() == TokenType.INTEGERSYM) {
            consume(TokenType.INTEGERSYM);
            variableType = VariableType.INTEGER;
        }
        else if(lookahead.getType() == TokenType.CHARSYM) {
            consume(TokenType.CHARSYM);
            variableType = VariableType.CHAR;
        }
        else if(lookahead.getType() == TokenType.BOOLEANSYM) {
            consume(TokenType.BOOLEANSYM);
            variableType = VariableType.BOOLEAN;
        }
        else if(lookahead.getType() == TokenType.ARRAYSYM) {
            consume(TokenType.ARRAYSYM);
            consume(TokenType.LBRACK);
            consume(TokenType.NUMBER);
            consume(TokenType.PERIOD);
            consume(TokenType.PERIOD);
            arrayExprEnd = parseExpr(arrayExprEnd);
            //consume(TokenType.NUMBER);
            consume(TokenType.RBRACK);
            consume(TokenType.OFSYM);
            if( lookahead.getType() == TokenType.INTEGERSYM) {
                consume(TokenType.INTEGERSYM);
                variableType = VariableType.INTEGER_ARRAY;
            }
            else if( lookahead.getType() == TokenType.CHARSYM) {
                consume(TokenType.CHARSYM);
                variableType = VariableType.CHAR_ARRAY;
            }
            else if( lookahead.getType() == TokenType.BOOLEANSYM) {
                consume(TokenType.BOOLEANSYM);
                variableType = VariableType.BOOLEAN_ARRAY;
            }
        }

        // Declare each as a parameter in the current scope
        for (String name : names) {
            if(symbolTable.isSymbolDeclared(name)) {
                error("Duplicate variable: " + name);
            }
            symbolTable.addVariable(name,variableType);
            if(variableType == VariableType.INTEGER_ARRAY) {
                try {
                    Integer endVal = Integer.parseInt(arrayExprEnd.getConstValue().toString());
                    endVal = endVal.intValue() + 1;
                    emit("declare", id, variableType.name(), endVal.toString());
                }
                catch(Exception ex) {
                    throw new RuntimeException("Array End Index not defined correctly");
                }
            }
            else {
                emit("declare", name, variableType.name(), null);
                emit("declareParam", name, variableType.name(), ""+parameterIndex);
            }
            parameterIndex++;
        }
    }

    // varSection ::= 'var' (idList ':' 'integer' ';')*
    private List<TACInstr> parseVarSection() {
        this.variablesTac = new ArrayList<>();
        consume(TokenType.VARSYM);
        while (lookahead.getType() == TokenType.IDENTIFIER) {
            List<TACInstr> tempList = parseVarDecl();
            this.variablesTac.addAll(tempList);
        }
        return this.variablesTac;
    }

    private List<TACInstr> parseVarDecl() {
        List<TACInstr> tempList = new ArrayList<>();
        List<String> names = new ArrayList<>();
        String id = expectId();
        names.add(id);
        Expr arrayExprEnd = new Expr();

        while (match(TokenType.COMMA)) {
            names.add(expectId());
        }

        consume(TokenType.COLON);

        VariableType variableType = null;
        if(lookahead.getType() == TokenType.INTEGERSYM) {
            consume(TokenType.INTEGERSYM);
            variableType = VariableType.INTEGER;
        }
        else if(lookahead.getType() == TokenType.CHARSYM) {
            consume(TokenType.CHARSYM);
            variableType = VariableType.CHAR;
        }
        else if(lookahead.getType() == TokenType.BOOLEANSYM) {
            consume(TokenType.BOOLEANSYM);
            variableType = VariableType.BOOLEAN;
        }
        else if(lookahead.getType() == TokenType.ARRAYSYM) {
            consume(TokenType.ARRAYSYM);
            consume(TokenType.LBRACK);
            consume(TokenType.NUMBER);
            consume(TokenType.PERIOD);
            consume(TokenType.PERIOD);
            arrayExprEnd = parseExpr(arrayExprEnd);
            //consume(TokenType.NUMBER);
            consume(TokenType.RBRACK);
            consume(TokenType.OFSYM);
            if( lookahead.getType() == TokenType.INTEGERSYM) {
                consume(TokenType.INTEGERSYM);
                variableType = VariableType.INTEGER_ARRAY;
            }
            else if( lookahead.getType() == TokenType.CHARSYM) {
                consume(TokenType.CHARSYM);
                variableType = VariableType.CHAR_ARRAY;
            }
            else if( lookahead.getType() == TokenType.BOOLEANSYM) {
                consume(TokenType.BOOLEANSYM);
                variableType = VariableType.BOOLEAN_ARRAY;
            }
        }

        consume(TokenType.SEMICOLON);

        for (String name : names) {
            if(symbolTable.isSymbolDeclared(name)) {
                error("Duplicate variable: " + name);
            }
            symbolTable.addVariable(name,variableType);
            if(variableType == VariableType.INTEGER_ARRAY) {
                try {
                    Integer endVal = Integer.parseInt(arrayExprEnd.getConstValue().toString());
                    endVal = endVal.intValue() + 1;
                    emit("declare", id, variableType.name(), endVal.toString());
                    tempList.add(new TACInstr("declare", id, variableType.name(), endVal.toString()) );
                }
                catch(Exception ex) {
                    throw new RuntimeException("Array End Index not defined correctly");
                }
            }
            else {
                //emit("declare", name, variableType.name(), null);
                tempList.add(new TACInstr("declare", name, variableType.name(), null) );
            }
        }
        return tempList;
    }

    private String expectId() {
        if (lookahead.getType() != TokenType.IDENTIFIER) {
            error("Identifier expected");
        }
        String name = lookahead.getLexeme();
        consume(TokenType.IDENTIFIER);
        return name;
    }


    void parseBlock() {
        consume(TokenType.BEGINSYM);
        parseStmtList();
        consume(TokenType.ENDSYM);
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
            case "READSYM":
                parseReadStmt();
                break;
            case "READLNSYM":
                parseReadlnStmt();
                break;
            case "WRITESYM":
                parseWriteStmt();
                break;
            case "WRITELNSYM":
                parseWritelnStmt();
                break;
            case "WHILESYM":
                parseWhileStmt();
                break;
            case "FORSYM":
                parseForStmt();
                break;
            case "IFSYM":
                parseIfStmt();
                break;
            default:
                error("Statement expected");
        }
    }

    // assignment ::= ID ':=' expr
    private void parseAssignment() {
        String name = expectId();  //This consumes the identifier.

        //if it is a procedure name, invoke the proc
        if( symbolTable.findSymbol(name).getVariableType() == VariableType.PROCEDURE) {
            parseProcedureCall(name);
            return;
        }
        ensureDeclared(name);

        Expr arrayIndex = new Expr();
        if(lookahead.getType() == TokenType.LBRACK ) {   //For Array Indexes
            consume(TokenType.LBRACK);
            if(lookahead.getType() == TokenType.NUMBER ) {
                //consume(TokenType.NUMBER);                          //How do you process the index?
                arrayIndex = parseExpr(arrayIndex);
            }
            else if(lookahead.getType() == TokenType.IDENTIFIER ) {
                //consume(TokenType.IDENTIFIER);                      //How do you process the index?
                arrayIndex = parseExpr(arrayIndex);
            }
            consume(TokenType.RBRACK);
        }

        consume(TokenType.ASSIGN);

        Expr expr = new Expr();
        if( symbolTable.findSymbol(name).getVariableType() == VariableType.BOOLEAN) {
            expr.setExpectedType(VariableType.BOOLEAN);
            Expr src = parseBoolExpr(expr);
            emit("declare", name, "BOOLEAN", null);
            emit("assign", src.getExpressionValue(), null, name);
        }
        else if( symbolTable.findSymbol(name).getVariableType() == VariableType.INTEGER) {
            expr.setExpectedType(VariableType.INTEGER);
            Expr exprRight = parseExpr(expr);          // src is a var, number, or temp
            emit("declare", name, "INTEGER", null);
            emit("assign", exprRight.getExpressionValue(), null, name);
        }
        else if( symbolTable.findSymbol(name).getVariableType() == VariableType.CHAR) {
            expr.setExpectedType(VariableType.CHAR);
            Expr exprRight = parseExpr(expr);          // src is a var, number, or temp
            emit("declare", name, "CHAR", null);
            emit("assign", exprRight.getExpressionValue(), null, name);
        }
        else if( symbolTable.findSymbol(name).getVariableType() == VariableType.INTEGER_ARRAY) {
            expr.setExpectedType(VariableType.INTEGER);
            Expr exprRight = parseExpr(expr);          // src is a var, number, or temp
            emit("assignArray", exprRight.getExpressionValue(), arrayIndex.getExpressionValue(), name);
        }
    }

    private void parseProcedureCall(String procedureName) {
        List<String> args = Collections.emptyList();

        if (lookahead.getType() == TokenType.LPAREN) {
            args = parseActualParams();
        }

        // Example TAC convention:
        // PARAM a1
        // PARAM a2
        // CALL procName, n
        for (String a : args) {
            emit("PARAM", a, null, null);
        }
        emit("callProc", procedureName, String.valueOf(args.size()), null);
    }


    private List<String> parseActualParams() {
        List<String> args = new ArrayList<>();
        consume(TokenType.LPAREN);
        // handle empty () if your dialect allows it
        if (lookahead.getType() != TokenType.RPAREN) {
            Expr expr = new Expr();
            expr = parseExpr(expr);
            args.add(expr.getVariableName());              // parse first expression

            while (lookahead.getType() == TokenType.COMMA) {
                consume(TokenType.COMMA);
                Expr expr2 = new Expr();
                expr2 = parseExpr(expr2);
                args.add(expr2.getVariableName());
            }
        }

        consume(TokenType.RPAREN);
        return args;
    }


    // readStmt ::= 'read' '(' ID ')'
    private void parseReadStmt() {
        consume(TokenType.READSYM);
        consume(TokenType.LPAREN);
        String name = expectId();
        ensureDeclared(name);
        consume(TokenType.RPAREN);
        // read name
        if(symbolTable.findSymbol(name).getVariableType()==VariableType.CHAR) {
            emit("read-char", null, null, name);
        }
        else if(symbolTable.findSymbol(name).getVariableType()==VariableType.INTEGER ) {
            emit("read-integer", null, null, name);
        }
    }

    // readStmt ::= 'readln' '(' ID ')'
    private void parseReadlnStmt() {
        consume(TokenType.READLNSYM);
        consume(TokenType.LPAREN);
        String name = expectId();
        ensureDeclared(name);
        consume(TokenType.RPAREN);
        // read name
        if(symbolTable.findSymbol(name).getVariableType()==VariableType.CHAR) {
            emit("readln-char", null, null, name);
        }
        else if(symbolTable.findSymbol(name).getVariableType()==VariableType.INTEGER ) {
            emit("readln-integer", null, null, name);
        }
    }

    // writeStmt ::= 'writeln' '(' expr ')'
    private void parseWriteStmt() {
        consume(TokenType.WRITESYM);
        consume(TokenType.LPAREN);

        Expr expr = new Expr();
        expr = parseExpr(expr);
        consume(TokenType.RPAREN);

        if(!expr.isConst()) {
            String name = expr.getExpressionValue();
            emit("declare", name, symbolTable.findSymbol(name).getVariableType().name(), null);
            if(symbolTable.findSymbol(name).getVariableType()==VariableType.INTEGER ) {
                emit("write-integer", null, null, name);
            }
            else if(symbolTable.findSymbol(name).getVariableType()==VariableType.CHAR) {
                emit("write-char", null, null, name);
            }
            else if(symbolTable.findSymbol(name).getVariableType()==VariableType.BOOLEAN) {
                emit("write-boolean", null, null, name);
            }
            else if(expr.getVariableType()==VariableType.INTEGER) {
                emit("write-integer", null, null, expr.getVariableName());
            }
        }
        else {
            // print name
            String prompt = newPrompt();
            emit("declarePrompt", prompt, expr.getExpressionValue(), null);
            emit("write", prompt, null, null);
        }
    }


    private void parseWritelnStmt() {
        consume(TokenType.WRITELNSYM);
        if(lookahead.getType()==TokenType.SEMICOLON) {
            emit("writeln", "", null, null);
            return;
        }
        consume(TokenType.LPAREN);

        Expr expr = new Expr();

        while (lookahead.getType() != TokenType.RPAREN ) {
            if(lookahead.getType()==TokenType.COMMA) {
                consume(TokenType.COMMA);
                continue;
            }
            expr.setExpectedType(VariableType.INTEGER);
            expr = parseExpr(expr);

            if(!expr.isConst()) {
                String name = expr.getExpressionValue();
                //emit("declare", name, symbolTable.findSymbol(name).getVariableType().name(), null);
                if(symbolTable.findSymbol(name).getVariableType()==VariableType.INTEGER ) {
                    emit("write-integer", null, null, name);
                }
                else if(symbolTable.findSymbol(name).getVariableType()==VariableType.CHAR) {
                    emit("write-char", null, null, name);
                }
                else if(symbolTable.findSymbol(name).getVariableType()==VariableType.BOOLEAN) {
                    emit("write-boolean", null, null, name);
                }
                else if(expr.getVariableType()==VariableType.INTEGER) {
                    emit("write-integer", null, null, expr.getVariableName());
                }
            }
            else {
                // print name
                String prompt = newPrompt();
                emit("declarePrompt", prompt, expr.getExpressionValue(), null);
                emit("write", prompt, null, null);
            }
        }
        emit("writeln", "", null, null);
        consume(TokenType.RPAREN);
    }

    // expr ::= term ( ('+' | '-') term)*
    //          1 term
    //          followed by 0 or more + | - term
    private Expr parseExpr(Expr expr) {

        Expr left = parseTerm(expr);
        while (lookahead.getType() == TokenType.PLUS
                || lookahead.getType() == TokenType.MINUS) {
            TokenType op = lookahead.getType();
            consume(op);

            Expr right = parseTerm(expr);
            String t = newTemp();
            emit("declare", t, "INTEGER", null);

            checkForArithmeticSemanticError(expr.getExpectedType(), right.getVariableType());
            if (op == TokenType.PLUS) {
                emit("+", left.getExpressionValue(), right.getExpressionValue(), t);
            } else {
                emit("-", left.getExpressionValue(), right.getExpressionValue(), t);
            }

            Expr exprRight = new Expr();
            exprRight.setExpectedType(VariableType.INTEGER);
            exprRight.setVariableName(t);
            left = exprRight;
        }
        return left;
    }

    // term ::= factor (('*' | '/') factor)*
    //          1 factor
    //          followed by 0 or more * | / factor
    private Expr parseTerm(Expr expr) {

        expr.setWantLValue(true);
        Expr left = parseFactor(expr);

        while (lookahead.getType() == TokenType.TIMES || lookahead.getType() == TokenType.DIVSYM) {
            TokenType op = lookahead.getType();
            consume(op);
            expr.setWantLValue(false);
            Expr right = parseFactor(expr);
            String t = newTemp();
            emit("declare", t, "INTEGER", null);

            checkForArithmeticSemanticError(expr.getExpectedType(), right.getVariableType());
            if (op == TokenType.TIMES) {
                emit("*", left.getExpressionValue(), right.getExpressionValue(), t);
            } else {
                emit("/", left.getExpressionValue(), right.getExpressionValue(), t);
            }
            Expr exprRight = new Expr();
            exprRight.setExpectedType(VariableType.INTEGER);
            exprRight.setVariableName(t);
            exprRight.setVariableType(VariableType.INTEGER);
            left = exprRight;
        }
        return left;
    }

    // factor ::= ID | NUMBER | '(' expr ')'
    private Expr parseFactor(Expr expr) {

        String typeName = lookahead.getType().name();

        switch (typeName) {
            case "IDENTIFIER": {
                String name = lookahead.getLexeme();
                consume(TokenType.IDENTIFIER);
                ensureDeclared(name);

                Expr arrayIndex = new Expr();
                if(lookahead.getType() == TokenType.LBRACK ) {
                    consume(TokenType.LBRACK);
                    if(lookahead.getType() == TokenType.NUMBER ) {
                        arrayIndex = parseExpr(arrayIndex);
                    }
                    else if(lookahead.getType() == TokenType.IDENTIFIER ) {
                        arrayIndex = parseExpr(arrayIndex);
                    }
                    consume(TokenType.RBRACK);
                }

                if(symbolTable.findSymbol(name).getVariableType()==VariableType.INTEGER_ARRAY) {
                    String temp = newTemp();
                    emit("declare", temp, "INTEGER", null);
                    emit("computeArrayElem", name, arrayIndex.getVariableName(),temp);
                    Expr tempExpr = new Expr();
                    tempExpr.setVariableName(temp);
                    tempExpr.setVariableType(VariableType.INTEGER);
                    return tempExpr;
                }
                Expr newExpr = new Expr();
                newExpr.setVariableName(name);
                newExpr.setVariableType(symbolTable.findSymbol(name).getVariableType());
                return newExpr;
            }
            case "NUMBER": {
                int v = lookahead.getValue();
                consume(TokenType.NUMBER);
                String temp = newTemp();
                emit("declare", temp, "INTEGER", null);
                emit("assign", String.valueOf(v), null, temp); // t = literal

                Expr newExpr = new Expr();
                newExpr.setConst(v, VariableType.INTEGER);
                newExpr.setVariableName(temp);
                newExpr.setVariableType(VariableType.INTEGER);
                return newExpr;
            }
            case "LPAREN":
                consume(TokenType.LPAREN);
                Expr innerExpr = parseExpr(expr);
                consume(TokenType.RPAREN);
                return innerExpr;
            case "QUOTESTRING":
                String name = lookahead.getLexeme();
                consume(TokenType.QUOTESTRING);
                expr.setVariableName(name);
                expr.setVariableType(VariableType.QUOTE_STRING);
                expr.setConst(name,VariableType.QUOTE_STRING);
                return expr;
            case "TRUESYM":
                consume(TokenType.TRUESYM);

                String t = newTemp();
                emit("assign", "true", null, t); // t = literal

                Expr newExpr = new Expr();
                newExpr.setConst(Boolean.TRUE, VariableType.BOOLEAN);
                return newExpr;
            case "FALSESYM":                                //Semantic Error??
                consume(TokenType.TRUESYM);

                String t1 = newTemp();
                emit("assign", "false", null, t1);

                Expr exprFalse = new Expr();
                exprFalse.setConst(Boolean.FALSE, VariableType.BOOLEAN);
                return exprFalse;
            case "LITCHAR":
                String litChar = lookahead.getLexeme();
                String t2 = newTemp();
                emit("declare", t2, "CHAR", null);
                emit("assign", litChar, null, t2);
                consume(TokenType.LITCHAR);

                Expr exprChar = new Expr();
                exprChar.setConst(litChar.charAt(0), VariableType.CHAR);
                return exprChar;
            default:
                error("Expression expected");
                return null; // unreachable
        }
    }

    private void checkForLogicalSemanticError(VariableType expectedType,VariableType lookaheadType) {
        if( VariableType.resultOfLogical(expectedType, lookaheadType) == VariableType.ERROR) {
            error("Semantic Error: Incompatible Types [" + expectedType.name() + ", " + lookaheadType.name() + "]. Cannot perform Logical Operation");
        }
    }

    private void checkForRelationalSemanticError(VariableType expectedType,VariableType lookaheadType) {
        if( VariableType.resultOfRelational(expectedType, lookaheadType) == VariableType.ERROR) {
            error("Semantic Error: Incompatible Types [" + expectedType.name() + ", " + lookaheadType.name() + "]. Cannot perform Relational Operation");
        }
    }

    private void checkForArithmeticSemanticError(VariableType expectedType,VariableType lookaheadType) {
        if( VariableType.resultOfArithmetic(expectedType, lookaheadType) == VariableType.ERROR) {
            error("Semantic Error: Incompatible Types [" + expectedType.name() + ", " + lookaheadType.name() + "]. Cannot perform Arithmetic Operation");
        }
    }

    private Expr parseBoolExpr(Expr expr) {
        Expr prevExpr = parseBoolNot(expr);
        //System.out.println(left);

        while (lookahead.getType() == TokenType.ORSYM || lookahead.getType() == TokenType.ANDSYM) {
            String operator = lookahead.getLexeme();
            consume(lookahead.getType());
            Expr nextExpr = parseBoolNot(expr);

            checkForLogicalSemanticError(prevExpr.getVariableType(),nextExpr.getVariableType());
            String t = newTemp();
            emit("declare", t, "BOOLEAN", null);
            emit(operator, prevExpr.getExpressionValue(), nextExpr.getExpressionValue(), t);

            Expr tempExpr = new Expr();
            tempExpr.setVariableName(t);
            tempExpr.setVariableType(VariableType.BOOLEAN);

            prevExpr = tempExpr;
        }
        return prevExpr;
    }

    Expr parseBoolNot(Expr expr) {
        if (lookahead.getType() == TokenType.NOTSYM) {
            consume(TokenType.NOTSYM);
        }
        Expr left = parseBoolAtom(expr);
        return left;
    }

    Expr parseBoolAtom(Expr expr) {
        if (lookAheadIs(TokenType.TRUESYM) ) {
            consume(TokenType.TRUESYM);
            String t = newTemp();
            emit("declare", t, "BOOLEAN", null);
            emit("assign", "true", null, t); // t = literal
            Expr expr1 = new Expr();
            expr1.setExpectedType(VariableType.BOOLEAN);
            expr1.setConst(Boolean.TRUE, VariableType.BOOLEAN);
            return expr1;
        }
        if (lookAheadIs(TokenType.FALSESYM) ) {
            consume(TokenType.FALSESYM);
            String t = newTemp();
            emit("declare", t, "BOOLEAN", null);
            emit("assign", "false", null, t); // t = literal
            Expr expr2 = new Expr();
            expr2.setExpectedType(VariableType.BOOLEAN);
            expr2.setConst(Boolean.FALSE, VariableType.BOOLEAN);
            return expr2;
        }

        if (lookAheadIs(TokenType.LPAREN)) {
            consume(TokenType.LPAREN);
            Expr left = parseBoolExpr(expr);
            consume(TokenType.RPAREN);
            return left;
        }

        // relational or "expr <> 0"
        Expr left = parseBooleanFactor(expr);

        if (lookahead.getType().isRelationalOperator()) {
            String operator = lookahead.getLexeme();
            consume(lookahead.getType());
            Expr right = parseBooleanFactor(expr);

            checkForRelationalSemanticError(left.getExpectedType(), right.getVariableType());

            String t = newTemp();
            emit("declare", t, "BOOLEAN", null);
            emit(operator, left.getExpressionValue(), right.getExpressionValue(), t);

            Expr expr3 = new Expr();
            expr3.setVariableType(VariableType.BOOLEAN);
            expr3.setVariableName(t);
            return expr3;
        } else {
            // boolean context: treat expression as (expr <> 0)
            //TODO
        }
        return left;
    }

    private Expr parseBooleanFactor(Expr expr) {
        String typeName = lookahead.getType().name();
        switch (typeName) {
            case "IDENTIFIER": {
                String name = lookahead.getLexeme();
                consume(TokenType.IDENTIFIER);
                ensureDeclared(name);

                Expr arrayIndex = new Expr();
                if(lookahead.getType() == TokenType.LBRACK ) {
                    consume(TokenType.LBRACK);
                    if(lookahead.getType() == TokenType.NUMBER ) {
                        arrayIndex = parseExpr(arrayIndex);
                    }
                    else if(lookahead.getType() == TokenType.IDENTIFIER ) {
                        arrayIndex = parseExpr(arrayIndex);
                    }
                    consume(TokenType.RBRACK);
                }

                if(symbolTable.findSymbol(name).getVariableType()==VariableType.INTEGER_ARRAY) {
                    String temp = newTemp();
                    emit("declare", temp, "INTEGER", null);
                    emit("computeArrayElem", name, arrayIndex.getVariableName(),temp);
                    Expr tempExpr = new Expr();
                    tempExpr.setVariableName(temp);
                    tempExpr.setVariableType(VariableType.INTEGER);
                    tempExpr.setExpectedType(VariableType.INTEGER);
                    return tempExpr;
                }
                Expr newExpr = new Expr();
                newExpr.setVariableName(name);
                newExpr.setVariableType(symbolTable.findSymbol(name).getVariableType());
                newExpr.setExpectedType(symbolTable.findSymbol(name).getVariableType());
                return newExpr;
            }
            case "NUMBER": {
                int v = lookahead.getValue();
                consume(TokenType.NUMBER);
                String tempName = newTemp();
                emit("declare", tempName, "INTEGER", null);
                emit("assign", String.valueOf(v), null, tempName); // tempName = literal

                Expr newExpr = new Expr();
                newExpr.setVariableType(VariableType.INTEGER);
                newExpr.setVariableName(tempName);
                newExpr.setExpectedType(VariableType.INTEGER);
                return newExpr;
            }
            case "TRUESYM":
                consume(TokenType.TRUESYM);

                String t = newTemp();
                emit("assign", "true", null, t); // t = literal

                Expr exprTrue = new Expr();
                exprTrue.setConst(Boolean.TRUE, VariableType.BOOLEAN);
                exprTrue.setExpectedType(VariableType.BOOLEAN);
                return exprTrue;
            case "FALSESYM":
                consume(TokenType.TRUESYM);
                String t1 = newTemp();
                emit("assign", "false", null, t1);

                Expr exprFalse = new Expr();
                exprFalse.setConst(Boolean.FALSE, VariableType.BOOLEAN);
                exprFalse.setExpectedType(VariableType.BOOLEAN);
                return exprFalse;
            case "LITCHAR":
                String litChar = lookahead.getLexeme();
                String t2 = newTemp();
                emit("declare", t2, "CHAR", null);
                emit("assign", litChar, null, t2);
                consume(TokenType.LITCHAR);

                Expr exprChar = new Expr();
                exprChar.setConst(litChar.toString(), VariableType.CHAR);
                exprChar.setExpectedType(VariableType.CHAR);
                return exprChar;
            case "LPAREN":
                consume(TokenType.LPAREN);
                Expr inner = parseBoolExpr(expr);
                consume(TokenType.RPAREN);
                return inner;
            default:
                error("Boolean Expression expected");
                return null; // unreachable
        }
    }

    void parseIfStmt() {
        consume(TokenType.IFSYM);
        Expr expr = new Expr();
        expr.setExpectedType(VariableType.BOOLEAN);
        Expr conditionalExpr = parseBoolExpr(expr);
        consume(TokenType.THENSYM);

        String elseLocator = newLocator();
        String endOfIfAndElseLocator = newLocator();
        emit("IfZ", conditionalExpr.getExpressionValue(), elseLocator,"");

        if(lookahead.getType() == TokenType.BEGINSYM ) {
            parseBlock();
        }
        else {
            parseStatement();
        }
        emit("GOTO", endOfIfAndElseLocator, "","");

        emit("Label", elseLocator, "","");
        if(lookahead.getType() == TokenType.ELSESYM ) {
            consume(TokenType.ELSESYM);
            if(lookahead.getType() == TokenType.BEGINSYM ) {
                parseBlock();
            }
            else {
                parseStatement();
            }
        }
        emit("Label", endOfIfAndElseLocator, "","");
    }

    void parseWhileStmt() {
        consume(TokenType.WHILESYM);

        String startLocator = newLocator();
        emit("Label", startLocator, "","");

        Expr expr = new Expr();
        expr.setExpectedType(VariableType.BOOLEAN);
        Expr conditionalExpr = parseBoolExpr(expr);

        String endLocator = newLocator();
        emit("IfZ", conditionalExpr.getExpressionValue(), endLocator,"");

        consume(TokenType.DOSYM);
        if(lookahead.getType() == TokenType.BEGINSYM ) {
            parseBlock();
        }
        else {
            parseStatement();
        }
        emit("GOTO", startLocator, "","");
        emit("Label", endLocator, "","");
    }

    void parseForStmt() {
        consume(TokenType.FORSYM);
        //consume(TokenType.IDENTIFIER);
        Expr forIndexIdentifier = new Expr();
        forIndexIdentifier = parseFactor(forIndexIdentifier);

        consume(TokenType.ASSIGN);

        Expr exprStart = new Expr();
        exprStart.setExpectedType(VariableType.INTEGER);
        Expr startValue = parseFactor(exprStart);

        emit("assign", startValue.getExpressionValue(), null, forIndexIdentifier.getVariableName());

        String startLocator = newLocator();
        emit("Label", startLocator, "","");

        consume(TokenType.TOSYM);
        Expr exprEnd = new Expr();
        exprEnd.setExpectedType(VariableType.INTEGER);
        Expr endValue = parseFactor(exprEnd);
        consume(TokenType.DOSYM);

        String endLocator = newLocator();
        String t = newTemp();
        emit("declare", t, "BOOLEAN", null);
        emit("LESSEQUAL", forIndexIdentifier.getVariableName(), endValue.getExpressionValue(), t);
        emit("IfZ", t, endLocator,"");

        if(lookahead.getType() == TokenType.BEGINSYM ) {
            parseBlock();
        }
        else {
            parseStatement();
        }

        String incVar = newTemp();
        emit("declare", incVar, "INTEGER", null);
        emit("assign", "1", null, incVar);

        emit("+", forIndexIdentifier.getVariableName(), incVar, forIndexIdentifier.getVariableName());

        emit("GOTO", startLocator, "","");
        emit("Label", endLocator, "","");
    }

}

