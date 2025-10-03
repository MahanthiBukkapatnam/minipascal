package edu.und.csci465.minipascal;

public class Parser {

    private GetSymbol getSymbol;

    public void setGetSymbol(GetSymbol getSymbol) {
        this.getSymbol = getSymbol;
    }

    public GetSymbol getGetSymbol() {
        return getSymbol;
    }

    public void process() {
        Token token = getSymbol.getNextToken();
        while( null != token ) {
            //System.out.println(token.toString());
            if(token.lexeme.equals(" ")) {
            }
            else if(token.lexeme.equals("NUMBER")) {
                System.out.printf("%20s %20s\n", token.type, token.value);
            }
            else {
                System.out.printf("%20s %20s\n", token.type, token.lexeme);
            }

            token = getSymbol.getNextToken();
        }
    }
}
