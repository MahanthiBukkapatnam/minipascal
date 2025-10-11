package edu.und.csci465.minipascal.parser;

import edu.und.csci465.minipascal.symboltable.GetSymbol;
import edu.und.csci465.minipascal.symboltable.Token;

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
            if(token.getLexeme().equals(" ")) {
            }
            else if(token.getType() !=null && token.getType().equals("ILLEGAL")) {
                System.out.printf("%20s %20s at [%3d,%3d]\n", token.getType(), token.getLexeme(), token.getPosition().getLineNumber(), token.getPosition().getColumnNumber());
            }
            else if(token.getType() !=null && token.getType().equals("NUMBER")) {
                System.out.printf("%20s %20s\n", token.getType(), token.getValue());
            }
            else {
                System.out.printf("%20s %20s\n", token.getType(), token.getLexeme());
            }

            token = getSymbol.getNextToken();
        }
    }
}
