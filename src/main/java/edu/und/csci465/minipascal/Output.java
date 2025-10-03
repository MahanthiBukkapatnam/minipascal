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
        for (String line : lines) {
            System.out.println(line);
        }
    }
}
