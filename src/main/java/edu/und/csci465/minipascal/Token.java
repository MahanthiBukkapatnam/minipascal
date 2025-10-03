package edu.und.csci465.minipascal;

public class Token {

    String lexeme;
    String type;
    int value;

    public Token(String lexeme) {
        this.lexeme= lexeme;
    }

    public Token(String lexeme, String type, int value) {
        this.lexeme= lexeme;
        this.type = type;
        this.value = value;
    }

    public Token(String lexeme, String type) {
        this.lexeme= lexeme;
        this.type = type;
        this.value = value;
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
}
