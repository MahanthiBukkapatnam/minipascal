package edu.und.csci465.minipascal.symboltable;

import edu.und.csci465.minipascal.lexer.Position;

public class Token {

    String lexeme;
    TokenType type;
    int value;
    Position position;

    public Token(String lexeme, TokenType type, int value, Position position) {
        this.lexeme= lexeme;
        this.type = type;
        this.value = value;
        this.position = position;
    }

    public Token(String lexeme, TokenType type, Position position) {
        this.lexeme= lexeme;
        this.type = type;
        this.value = value;
        this.position = position;
    }

    @Override
    public String toString() {
        if(type!=null) {
            if(type.equals("NUMBER")) {
                return "Token{" +
                        ", type='" + type + '\'' +
                        ", value=" + value +
                        '}';
            }
            return "Token{" +
                    "lexeme='" + lexeme + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }

        return "Token{" +
                "lexeme='" + lexeme + '\'' +
                '}';
    }

    public String getLexeme() {
        return lexeme;
    }

    public TokenType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public Position getPosition() {
        return position;
    }
}
