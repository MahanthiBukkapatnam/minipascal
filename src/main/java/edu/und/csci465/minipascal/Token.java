package edu.und.csci465.minipascal;

public class Token {

    String lexeme;
    String type;
    int value;
    Position position;

    public Token(String lexeme, String type, int value, Position position) {
        this.lexeme= lexeme;
        this.type = type;
        this.value = value;
        this.position = position;
    }

    public Token(String lexeme, String type, Position position) {
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
}
