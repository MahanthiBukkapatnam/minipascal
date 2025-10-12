package edu.und.csci465.minipascal.symboltable;

import java.util.HashMap;
import java.util.Map;

public enum TokenType {
    ANDSYM,
    ARRAYSYM,
    BEGINSYM,
    CHARSYM,
    CHRSYM,
    DIVSYM,
    DOSYM,
    ELSESYM,
    ENDSYM,
    IFSYM,
    INTEGERSYM,
    MODSYM,
    NOTSYM,
    OFSYM,
    ORSYM,
    ORD,
    PROCEDURESYM,
    PROGRAMSYM,
    READSYM,
    READLNSYM,
    THENSYM,
    VARSYM,
    WHILESYM,
    WRITESYM,
    WRITELNSYM,

    PLUS,
    MINUS,
    TIMES,
    SLASH,

    LESSTHAN,
    LESSEQUAL,
    NOTEQUAL,
    GREATER,
    GREATEREQUAL,
    EQUAL,

    ASSIGN,
    COLON,
    SEMICOLON,
    COMMA,
    LPAREN,
    LBRACK,
    RPAREN,
    RBRACK,
    PERIOD,

    // Literals & identifiers
    IDENTIFIER, // (*letter (letter|digit)*	  *)
    NUMBER,		// (* digit (digit)*				*)
    REAL,		// (* digit (digit)*				*)
    QUOTESTRING,// (* such as ‘here’, ‘s a string’		*)
    LITCHAR,	// (* quoted strings of length=1: e.g.,     ‘a’, ‘b’ *)
    EOFSYM,		// (* returned by getsym at end of file		*)
    ILLEGAL;	// (* for lexical errors: e.g., #id		*)

    private static Map<String, TokenType> keyWords = new HashMap<>();

    static {
        keyWords.put("and",      TokenType.ANDSYM);
        keyWords.put("array",    TokenType.ARRAYSYM);
        keyWords.put("begin",    TokenType.BEGINSYM);
        keyWords.put("char",     TokenType.CHARSYM);
        keyWords.put("chr",      TokenType.CHRSYM);
        keyWords.put("div",      TokenType.DIVSYM);
        keyWords.put("do",       TokenType.DOSYM);
        keyWords.put("else",     TokenType.ELSESYM);
        keyWords.put("end",      TokenType.ENDSYM);
        keyWords.put("if",       TokenType.IFSYM);
        keyWords.put("integer",  TokenType.INTEGERSYM);
        keyWords.put("mod",      TokenType.MODSYM);
        keyWords.put("not",      TokenType.NOTSYM);
        keyWords.put("of",       TokenType.OFSYM);
        keyWords.put("or",       TokenType.ORSYM);
        keyWords.put("ord",      TokenType.ORD);
        keyWords.put("procedure",TokenType.PROCEDURESYM);
        keyWords.put("program",  TokenType.PROGRAMSYM);
        keyWords.put("read",     TokenType.READSYM);
        keyWords.put("readln",   TokenType.READLNSYM);
        keyWords.put("then",     TokenType.THENSYM);
        keyWords.put("var",      TokenType.VARSYM);
        keyWords.put("while",    TokenType.WHILESYM);
        keyWords.put("write",    TokenType.WRITESYM);
        keyWords.put("writeln",  TokenType.WRITELNSYM);
    }

    static Map<String, TokenType> operatorCharacters = new HashMap<>();

    static {
        operatorCharacters.put(".",TokenType.PERIOD);

        operatorCharacters.put("+",TokenType.PLUS);
        operatorCharacters.put("-",TokenType.MINUS);
        operatorCharacters.put("*",TokenType.TIMES);
        operatorCharacters.put("/",TokenType.DIVSYM);

        operatorCharacters.put("=",TokenType.EQUAL);
        operatorCharacters.put("<",TokenType.LESSTHAN);
        operatorCharacters.put(">",TokenType.GREATER);
        operatorCharacters.put("(",TokenType.LPAREN);
        operatorCharacters.put(")",TokenType.RPAREN);
        operatorCharacters.put("[",TokenType.LBRACK);
        operatorCharacters.put("]",TokenType.RBRACK);

        operatorCharacters.put(":",TokenType.COLON);
        operatorCharacters.put(";",TokenType.SEMICOLON);
        operatorCharacters.put(",",TokenType.COMMA);
    }

    TokenType() {
    }

    public static TokenType lookUp(String word) {
        return keyWords.get(word);
    }
    public static boolean isKeyword(String word) {
        if( keyWords.keySet().contains(word.toLowerCase()) ) {
            return true;
        }
        return false;
    }

    public static TokenType getOperator(char ch1) {
        return operatorCharacters.get(""+ch1);
    }

    public static TokenType getOperator(char ch1, char ch2) {
        if(isLessThanOrEqual(ch1,ch2)) {
            return TokenType.LESSEQUAL;
        }
        if(isNotEqual(ch1,ch2)) {
            return TokenType.NOTEQUAL;
        }
        if(isGreaterThanOrEqual(ch1,ch2)) {
            return TokenType.GREATEREQUAL;
        }
        if(isAssignment(ch1,ch2)) {
            return TokenType.ASSIGN;
        }
        return null;
    }

    public static boolean is1CharOperator(char ch) {
        return operatorCharacters.keySet().contains(""+ch);
    }

    public static boolean is2CharOperator(char ch1, char ch2) {
        if(isLessThanOrEqual(ch1,ch2)) {
            return true;
        }
        if(isNotEqual(ch1,ch2)) {
            return true;
        }
        if(isGreaterThanOrEqual(ch1,ch2)) {
            return true;
        }
        if(isAssignment(ch1,ch2)) {
            return true;
        }
        return false;
    }

    public static boolean isOperator(char ch1, char ch2) {
        if(is2CharOperator(ch1, ch2) ) {
            return true;
        }
        return is1CharOperator(ch1);
    }

    public static boolean isLessThanOrEqual(char ch1, char ch2) {
        return ch1 == '<' && ch2 == '=';
    }
    public static boolean isNotEqual(char ch1, char ch2) {
        return ch1 == '<' && ch2 == '>';
    }
    public static boolean isGreaterThanOrEqual(char ch1, char ch2) {
        return ch1 == '>' && ch2 == '=';
    }
    public static boolean isAssignment(char ch1, char ch2) {
        return ch1 == ':' && ch2 == '=';
    }

}
