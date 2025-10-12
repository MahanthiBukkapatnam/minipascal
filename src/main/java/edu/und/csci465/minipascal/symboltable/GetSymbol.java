package edu.und.csci465.minipascal.symboltable;

import edu.und.csci465.minipascal.lexer.InputOutputModule;
import edu.und.csci465.minipascal.lexer.Position;

import java.util.*;

public class GetSymbol {

    private InputOutputModule ioModule;

    private int currentTokenIndex = 0;
    private List<Token> tokens = new ArrayList<>();

    public void reset() {
        tokens = new ArrayList<>();
        currentTokenIndex = 0;
    }

    public void setIoModule(InputOutputModule ioModule) {
        this.ioModule = ioModule;
    }

    public InputOutputModule getIoModule() {
        return ioModule;
    }

    public List<Token> getTokens() {
        return tokens;
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
        char ch = ioModule.currentChar();

        while( ch != 0 ) {
            skipWhiteSpaces();
            skipSingleLineComment();
            skipBraceComment();
            skipBlockComment();
            ch = ioModule.currentChar();
            Position startPosition = getPosition();

            if(isQuote(ch)) {
                processForQuote();
            }
            // Identifiers / Keywords
            else if (isAlpha(ch)) {
                processForIdentifier(ch);
            }
            // Numbers (integer or real)
            else if (Character.isDigit(ch)) {
                processForDigit2();
            }
            else if(isOperator(ch,ioModule.peek()) ) {
                if(TokenType.is2CharOperator(ch,ioModule.peek())) {
                    TokenType tokenType = TokenType.getOperator(ch,ioModule.peek());
                    tokens.add( new Token(tokenType.name(), tokenType, startPosition));
                    ioModule.next();
                }
                else if(TokenType.is1CharOperator(ch)) {
                    TokenType tokenType = TokenType.getOperator(ch);
                    tokens.add( new Token(tokenType.name(), tokenType, startPosition));
                }
            }
            else if(!isWhitespace(ch)){
                tokens.add(new Token("" + ch, TokenType.ILLEGAL, startPosition));
            }

            int currentCharValue = ch;
            //System.out.println("Current Char: [" + ch + "], [" + currentCharValue +"] " + " Position = " + getPosition() );
            //System.out.print(ch);

            ioModule.next();
            ch = ioModule.currentChar();
        }

        tokens.add( new Token("", TokenType.EOFSYM, getPosition()));
        System.out.println();
    }

    private void skipBlockComment() {
        Position startPosition = getPosition();

        char ch = ioModule.currentChar();
        if(ch == '(' && ioModule.peek() == '*' ) {
            ioModule.next();
            ioModule.next();

            Position braceStartPosition = getPosition();
            ioModule.next();
            ch = ioModule.currentChar();

            while(ch !='*' && ioModule.peek() != ')') {
                ioModule.next();
                ch = ioModule.currentChar();
                if(ch==0) {
                    throw new RuntimeException("Unfinished Block Comment at: " + startPosition.toString());
                }
            }
            if(ch == '*' && ioModule.peek() == ')') {
                ioModule.next();
                ioModule.next();
                ch = ioModule.currentChar();
            }
        }
    }
    private void skipBraceComment() {
        Position startPosition = getPosition();

        char ch = ioModule.currentChar();
        if(isLeftBrace(ch)) {
            ioModule.next();
            ch = ioModule.currentChar();

            while( !isRightBrace(ch) ) {
                ioModule.next();
                ch = ioModule.currentChar();
                if(ch==0) {
                    throw new RuntimeException("Unfinished Brace Comment at: " + startPosition.toString());
                }
            }
            if( isRightBrace(ch) ) {
                ioModule.next();
                ch = ioModule.currentChar();
            }
        }
    }

    private void skipSingleLineComment() {
        char ch = ioModule.currentChar();
        if(isSlash(ch) && isSlash(ioModule.peek()) ) {
            ioModule.next();
            ioModule.next();
            ch = ioModule.currentChar();
            while(ch !='\n' || ch == 0) {
                ioModule.next();
                ch = ioModule.currentChar();
            }
        }
    }

    void skipWhiteSpaces() {
        char ch = ioModule.currentChar();
        while(isWhitespace(ch) && isWhitespace(ioModule.peek())) {
            ioModule.next();
            ch = ioModule.currentChar();
        }
    }

    boolean isOperator(char ch, char ch2) {
        return TokenType.isOperator(ch,ch2);
    }

    boolean isAlpha(char ch) {
        return Character.isLetter(ch);
    }

    boolean isDigit(char ch) {
        return Character.isDigit(ch);
    }

    private boolean isAlphaNum(char c) {
        return isAlpha(c) || Character.isDigit(c);
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
        Position startPosition = getPosition();
        int totalValue = ch - '0';
        StringBuilder sb = new StringBuilder();
        while(isDigit(ioModule.peek())) {
            sb.append(ch);
            int value = ch - '0';
            totalValue = totalValue * 10 + (ioModule.peek()-'0');

            ioModule.next();
            ch = ioModule.currentChar();
        }
        int value = ch - '0';
        sb.append(ch);
        totalValue += value;
        tokens.add( new Token(sb.toString(), TokenType.NUMBER, totalValue,startPosition));
    }

    void processForDigit2() {
        Position startPosition = ioModule.getPosition();
        char ch = ioModule.currentChar();
        StringBuilder sb = new StringBuilder();

        // integer part
        while ( ch!=0 && Character.isDigit(ch)) {
            sb.append(ch);
            ioModule.next();
            ch = ioModule.currentChar();
        }

        boolean isReal = false;

        // fractional part: '.' followed by a digit (avoid consuming '..')
        if (ch!=0 && ch == '.' && Character.isDigit(ioModule.peek())) {
            isReal = true;
            sb.append(ch);                 // consume '.'
            ioModule.next();
            ch = ioModule.currentChar();
            if (ch!=0 || !Character.isDigit(ch)) {
                Position afterDotPosition = ioModule.getPosition();
                throw new RuntimeException("Malformed real: digits required after '.' at " + afterDotPosition);
            }

            while ( ch!=0 && Character.isDigit(ch)) {
                sb.append(ch);
                ioModule.next();
                ch = ioModule.currentChar();
            }
        }

        // exponent part: e[+/-]?digits
        if (ch!=0 &&  (ch == 'e' && ch == 'E') ) {
            isReal = true;
            sb.append(ch);                 // 'e' or 'E'
            ioModule.next();
            ch = ioModule.currentChar();

            if (ch!=0 && (ch == '+' || ch == '-')) {
                sb.append(ch);
                ioModule.next();
                ch = ioModule.currentChar();
            }

            if (ch!=0 || !Character.isDigit(ch)) {
                Position afterExpoPosition = ioModule.getPosition();
                throw new RuntimeException("Malformed exponent: digits required after 'e' " + afterExpoPosition);
            }

            while ( ch!=0 && Character.isDigit(ch)) {
                sb.append(ch);
                ioModule.next();
                ch = ioModule.currentChar();
            }
        }

        // If a letter/underscore follows immediately, it's not a valid number (e.g., 123abc)
        if (ch!=0 && (Character.isLetter(ch) )) {
            Position insidePosition = ioModule.getPosition();
            throw new RuntimeException("Invalid number: letters/underscore immediately after digits" + insidePosition);
        }

        String lexeme = sb.toString();
        tokens.add( new Token(lexeme, isReal ? TokenType.REAL : TokenType.NUMBER, startPosition) );
    }


    void processForIdentifier(char ch) {
        Position startPosition = getPosition();
        char c = ch;
        StringBuilder sb = new StringBuilder();
        while(isAlphaNum(ioModule.peek()) ) {
            sb.append(c);
            ioModule.next();
            c = ioModule.currentChar();
        }
        sb.append(c);

        String identifier = sb.toString();
        if(TokenType.isKeyword(identifier)) {
            tokens.add(new Token(identifier, TokenType.lookUp(sb.toString()), startPosition));
        }
        else {
            if(identifier.length()>0 && isDigit( identifier.charAt(0)) ) {
                tokens.add(new Token(identifier, TokenType.ILLEGAL, startPosition));
            }
            else {
                tokens.add(new Token(identifier, TokenType.IDENTIFIER, startPosition));
            }
        }
    }

    void processForQuote() {
        Position startPosition = getPosition();
        ioModule.next();
        char ch = ioModule.currentChar();
        StringBuffer word = new StringBuffer();
        while( !isQuote(ch) ) {
            if(ch==0) {
                throw new RuntimeException("Unfinished Quote starting at " + startPosition);
            }
            word.append(ch);
            //advance to next char
            ioModule.next();
            ch = ioModule.currentChar();
        }

        if(word.length()==1) {
            tokens.add(new Token(word.toString(), TokenType.LITCHAR, getPosition()));
        }
        else {
            tokens.add(new Token(word.toString(), TokenType.QUOTESTRING, getPosition()));
        }
    }

    public Position getPosition() {
        return ioModule.getPosition();
    }
}
