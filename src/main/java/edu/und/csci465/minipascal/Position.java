package edu.und.csci465.minipascal;

public class Position {

    private int lineNumber;
    private int columnNumber;

    private int message;

    public Position(int lineNumber, int columnNumber) {
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public Position(int lineNumber, int columnNumber, int message) {
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.message = message;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getLineNumber() {
        return lineNumber+1;
    }

    public int getColumnNumber() {
        return columnNumber;
    }
}
