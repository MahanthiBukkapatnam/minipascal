package edu.und.csci465.minipascal.symboltable;

import java.util.ArrayList;

public class Function extends Symbol {
    public int label;
    public ArrayList<Variable> parameters = new ArrayList<Variable>();

    public Function(String name, VariableType variableType, int label) {
        super(name, variableType);
        this.label = label;
    }

    public void addParameter(Variable var) {
        this.parameters.add(var);
    }
}
