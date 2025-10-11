package edu.und.csci465.minipascal.lexer;

public class InputOutputModule {

    String fileContent;
    private int currentCharIndex = 0;
    private int currentLineNumber = 0;
    private int currentColumnNumber = 0;
    private char prevChar = (char)-1;


    public void readFile(String fileName) {
        fileContent = Input.readFileAndKeepEol(fileName);
    }

    public String getFileContent() {
        return this.fileContent;
    }


    public char currentChar() {
        if(currentCharIndex > (fileContent.length()-1) ) {
            return 0;
        }
        return fileContent.charAt(currentCharIndex);
    }

    public char peek() {
        int peekIndex = currentCharIndex+1;
        if(peekIndex > (fileContent.length()-1) ) {
            return 0;
        }
        return fileContent.charAt(peekIndex);
    }

    public void next() {
        prevChar = currentChar();
        currentCharIndex++;
        currentColumnNumber++;

        // The unit tests helped me to code this logic correctly!!
        if(prevChar=='\n') {
            currentLineNumber++;
            currentColumnNumber=0;
        }
    }

    public Position getPosition() {
        return new Position(currentLineNumber, currentColumnNumber);
    }

}
