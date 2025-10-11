package edu.und.csci465.minipascal.lexer;

public class Position {

    private int lineNumber;
    private int columnNumber;

    public Position(int lineNumber, int columnNumber) {
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    @Override
    public String toString() {
        return "Position{" +
                "lineNumber=" + (lineNumber+1) +
                ", columnNumber=" + (columnNumber+1) +
                '}';
    }
}
