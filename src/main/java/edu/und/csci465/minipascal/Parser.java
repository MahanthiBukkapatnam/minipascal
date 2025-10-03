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
            System.out.println(token.toString());
            token = getSymbol.getNextToken();
        }
    }
}
