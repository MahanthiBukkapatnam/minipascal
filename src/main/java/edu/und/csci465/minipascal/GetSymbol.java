package edu.und.csci465.minipascal;

import java.util.ArrayList;
import java.util.List;

public class GetSymbol {

    private InputOutputModule inputOutputModule;

    private int currentTokenIndex = 0;
    private List<Token> lexemes = new ArrayList<>();

    public void setInputOutputModule(InputOutputModule inputOutputModule) {
        this.inputOutputModule = inputOutputModule;
    }

    public InputOutputModule getInputOutputModule() {
        return inputOutputModule;
    }

    public Token getNextToken() {
        if(currentTokenIndex > lexemes.size()-1) {
            return null;
        }
        Token token = lexemes.get(currentTokenIndex);
        currentTokenIndex++;
        return token;
    }

    public void process() {
        char ch = inputOutputModule.nextChar();
        while( ch != 0 ) {
            lexemes.add( new Token("" + ch));
            //System.out.println(ch);
            ch = inputOutputModule.nextChar();
        }
    }

}
