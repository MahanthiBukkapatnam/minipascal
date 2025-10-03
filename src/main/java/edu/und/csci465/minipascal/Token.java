package edu.und.csci465.minipascal;

public class Token {

    String lexeme;

    public Token(String lexeme) {
        this.lexeme= lexeme;
    }

    @Override
    public String toString() {
        return "Token{" +
                "lexeme='" + lexeme + '\'' +
                '}';
    }
}
