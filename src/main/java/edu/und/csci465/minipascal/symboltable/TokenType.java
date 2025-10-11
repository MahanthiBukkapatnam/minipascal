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

    TokenType() {
    }

    public static TokenType lookUp(String word) {
        return keyWords.get(word);
    }
}
