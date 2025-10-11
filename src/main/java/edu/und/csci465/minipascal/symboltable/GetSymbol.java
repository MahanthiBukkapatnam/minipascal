package edu.und.csci465.minipascal.symboltable;

import edu.und.csci465.minipascal.chatgpt.MiniPascalLexer;
import edu.und.csci465.minipascal.lexer.InputOutputModule;
import edu.und.csci465.minipascal.lexer.Position;

import java.util.*;

public class GetSymbol {

    private InputOutputModule inputOutputModule;

    private int currentTokenIndex = 0;
    private List<Token> tokens = new ArrayList<>();

    public void reset() {
        tokens = new ArrayList<>();
    }

    public void setInputOutputModule(InputOutputModule inputOutputModule) {
        this.inputOutputModule = inputOutputModule;
    }

    public InputOutputModule getInputOutputModule() {
        return inputOutputModule;
    }

    public Token getNextToken() {
        if(currentTokenIndex > tokens.size()-1) {
            return null;
        }
        Token token = tokens.get(currentTokenIndex);
        currentTokenIndex++;
        return token;
    }

    public void process() {
        reset();
        char ch = inputOutputModule.currentChar();
        while( ch != 0 ) {
            Position startPosition = getPosition();

            //Skip Spaces
            while(isWhitespace(ch) && isWhitespace(inputOutputModule.peek())) {
                inputOutputModule.next();
            }
            //Skip Single Line Comment
            if(isSlash(ch) && isSlash(inputOutputModule.peek()) ) {
                inputOutputModule.next();
                inputOutputModule.next();
                ch = inputOutputModule.currentChar();
                while(ch!='\n' || ch == 0) {
                    inputOutputModule.next();
                    ch = inputOutputModule.currentChar();
                }
                if(ch==0) {
                    return;
                }
            }
            //Skip Brace Comment
            if(isLeftBrace(ch)) {
                inputOutputModule.next();
                ch = inputOutputModule.currentChar();

                while(ch !='}') {
                    inputOutputModule.next();
                    ch = inputOutputModule.currentChar();
                    if(ch==0) {
                        throw new RuntimeException("Unfinished Brace Comment at: " + startPosition.toString());
                    }
                }
                if(ch == '}') {
                    inputOutputModule.next();
                    ch = inputOutputModule.currentChar();
                }
            }
            //Skip Block Line Comment
            if(ch == '(' && inputOutputModule.peek() == '*' ) {
                inputOutputModule.next();
                inputOutputModule.next();

                Position braceStartPosition = getPosition();
                inputOutputModule.next();
                ch = inputOutputModule.currentChar();

                while(ch !='*' && inputOutputModule.peek() != ')') {
                    inputOutputModule.next();
                    ch = inputOutputModule.currentChar();
                    if(ch==0) {
                        throw new RuntimeException("Unfinished Block Comment at: " + startPosition.toString());
                    }
                }
                if(ch == '*' && inputOutputModule.peek() == ')') {
                    inputOutputModule.next();
                    inputOutputModule.next();
                    ch = inputOutputModule.currentChar();
                }
            }

            //{
//                if(isDigit(ch) ) {
//                    processForDigit(ch);
//                }
//                else if(isLetter(ch) ) {
//                    processForWord(ch);
//                }
//                else if(isOperator(ch,inputOutputModule.peek()) ) {
//                    String operatorName = PascalOperatorCharacter.getOperatorName(ch, inputOutputModule.peek());
//                    tokens.add( new Token(operatorName, null, getPosition()));
//                }
//                else if(isQuote(ch)) {
//                    processForQuote(ch);
//                }
//                else {
//                    tokens.add(new Token("" + ch, TokenType.ILLEGAL, getPosition()));
//                }
            //}
            int currentCharValue = ch;
            System.out.println("Current Char: [" + ch + "], [" + currentCharValue +"] " + " Position = " + getPosition() );
            inputOutputModule.next();
            ch = inputOutputModule.currentChar();
        }

        tokens.add( new Token("", TokenType.EOFSYM, getPosition()));
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

    boolean isSlash(char ch) {
        return ch == '/';
    }
    boolean isLeftBrace(char ch) {
        return ch == '{';
    }
    boolean isRightBrace(char ch) {
        return ch == '}';
    }

    void processForDigit(char ch) {
        int totalValue = ch - '0';
        while(isDigit(inputOutputModule.peek())) {
            int value = ch - '0';
            totalValue = totalValue * 10 + (inputOutputModule.peek()-'0');
            ch = inputOutputModule.currentChar();
        }
        int value = ch - '0';
        totalValue += value;
        tokens.add( new Token("--", TokenType.NUMBER, totalValue, getPosition() ));
    }

    void processForWord(char ch) {
        String word = "";
        while(isLetter(inputOutputModule.peek()) || isDigit(inputOutputModule.peek())) {
            word = word + ch;
            ch = inputOutputModule.currentChar();
        }
        word = word + ch;

        if(PascalKeywords.isKeyword(word)) {
            tokens.add(new Token(word, TokenType.lookUp(word), getPosition()));
        }
        else {
            tokens.add(new Token(word, TokenType.IDENTIFIER, getPosition()));
        }
    }

    void processForQuote(char ch) {
        char ch1 = ch;
        char ch2 = inputOutputModule.peek();
        //System.out.printf("[%c %c]\n", ch1, ch2);

        String word = "";
        ch1 = inputOutputModule.currentChar();
        while( !isQuote(ch1) ) {
            word = word + ch1;
            //System.out.println(word);

            //advance to next char
            ch1 = inputOutputModule.currentChar();
        }

        if(word.length()==1) {
            tokens.add(new Token(word, TokenType.LITCHAR, getPosition()));
        }
        else {
            tokens.add(new Token(word, TokenType.QUOTESTRING, getPosition()));
        }
    }

    public Position getPosition() {
        return inputOutputModule.getPosition();
    }
}
