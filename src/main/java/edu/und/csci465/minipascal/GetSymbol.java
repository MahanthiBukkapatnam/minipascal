package edu.und.csci465.minipascal;

import java.util.*;

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
            if( isWhitespace(ch) &&  isWhitespace(inputOutputModule.peek()) ) { //Space
            }
            else {
                if(isDigit(ch) ) {
                    processForDigit(ch);
                }
                if(isLetter(ch) ) {
                    processForWord(ch);
                }
                else {
                    lexemes.add(new Token("" + ch));
                }
                //System.out.println(ch);
            }
            ch = inputOutputModule.nextChar();
        }
    }

    boolean isLetter(char ch) {
        return Character.isLetter(ch);
    }

    boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\t' || ch == 10 || ch == 13;
    }

    void processForDigit(char ch) {
        int totalValue = ch - '0';
        while(isDigit(inputOutputModule.peek())) {
            int value = ch - '0';
            totalValue = totalValue * 10 + (inputOutputModule.peek()-'0');
            ch = inputOutputModule.nextChar();
        }
        int value = ch - '0';
        totalValue += value;
        lexemes.add( new Token("--", "NUMBER", totalValue ));
    }

    void processForWord(char ch) {
        String word = "";
        while(isLetter(inputOutputModule.peek())) {
            word = word + ch;
            ch = inputOutputModule.nextChar();
        }
        word = word + ch;

        if(PascalKeywords.isKeyword(word)) {
            lexemes.add(new Token(word, "KEYWORD"));
        }
        else {
            lexemes.add(new Token(word, "IDENTIFIER"));
        }
    }

}
