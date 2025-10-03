package edu.und.csci465.minipascal;

import java.util.ArrayList;
import java.util.List;

public class InputOutputModule {

    int currentLineIndex = 0;
    int currentColIndex = 0;

    private List<String> lines = new ArrayList<>();
    private Output output = new Output();

    public void readFile(String fileName) {
        lines = Input.readFile(fileName);
        process();
    }

    void process() {
        output.clear();
        for (String line : lines) {
            output.addLine(line);
        }
    }

    public Output getOutput() {
        return output;
    }

    public char nextChar() {
        try {
            String currentLine = lines.get(currentLineIndex);
            char toReturn = currentLine.charAt(currentColIndex);
            moveToNextCharLocation(currentLine);
            return toReturn;
        }
        catch(Exception ex) {
        }

        return 0;
    }

    void moveToNextCharLocation(String currentLine) {
        currentColIndex++;
        if(currentColIndex > currentLine.length()-1 ) {
            //move to next line;
            currentLineIndex++;
            currentColIndex=0;
        }
    }
}
