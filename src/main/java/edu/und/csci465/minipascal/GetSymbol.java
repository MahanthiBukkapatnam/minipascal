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
                else if(isLetter(ch) ) {
                    processForWord(ch);
                }
                else if(isOperator(ch,inputOutputModule.peek()) ) {
                    String operatorName = PascalOperatorCharacter.getOperatorName(ch, inputOutputModule.peek());
                    lexemes.add( new Token(operatorName, "OPERATOR", getPosition()));
                }
                else if(isQuote(ch)) {
                    processForQuote(ch);
                }
                else {
                    lexemes.add(new Token("" + ch, "ILLEGAL", getPosition()));
                }
                //System.out.println(ch);
            }
            ch = inputOutputModule.nextChar();
        }

        lexemes.add( new Token("", "EOF", getPosition()));
    }

    boolean isOperator(char ch, char ch2) {
        return PascalOperatorCharacter.isKeyword(ch,ch2);
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

    boolean isQuote(char ch) {
        return ch == '\'';
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
        lexemes.add( new Token("--", "NUMBER", totalValue, getPosition() ));
    }

    void processForWord(char ch) {
        String word = "";
        while(isLetter(inputOutputModule.peek()) || isDigit(inputOutputModule.peek())) {
            word = word + ch;
            ch = inputOutputModule.nextChar();
        }
        word = word + ch;

        if(PascalKeywords.isKeyword(word)) {
            lexemes.add(new Token(word, "KEYWORD", getPosition()));
        }
        else {
            lexemes.add(new Token(word, "IDENTIFIER", getPosition()));
        }
    }

    void processForQuote(char ch) {
        char ch1 = ch;
        char ch2 = inputOutputModule.peek();
        //System.out.printf("[%c %c]\n", ch1, ch2);

        String word = "";
        ch1 = inputOutputModule.nextChar();
        while( !isQuote(ch1) ) {
            word = word + ch1;
            //System.out.println(word);

            //advance to next char
            ch1 = inputOutputModule.nextChar();
        }

        if(word.length()==1) {
            lexemes.add(new Token(word, "LIT_QUOTE", getPosition()));
        }
        else {
            lexemes.add(new Token(word, "QUOTE", getPosition()));
        }
    }

    public Position getPosition() {
        return inputOutputModule.getPosition();
    }
}
