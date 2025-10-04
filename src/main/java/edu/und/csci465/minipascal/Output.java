package edu.und.csci465.minipascal;

import java.util.ArrayList;
import java.util.List;

public class Output {

    private List<String> lines = new ArrayList<>();

    public void clear() {
        lines.clear();
    }

    public void addLine(String line) {
        lines.add(line);
    }

    public void print() {
        int lineNumber=1;
        for (String line : lines) {
            System.out.printf("%03d: %s\n", lineNumber,line);
            lineNumber++;
        }
    }
}
