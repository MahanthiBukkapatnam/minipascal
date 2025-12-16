package edu.und.csci465.minipascal.symboltable;

import java.util.ArrayList;

public class Procedure extends Symbol {
    public ArrayList<Variable> parameters = new ArrayList<Variable>();

    public Procedure(String name, VariableType variableType) {
        super(name, variableType);
    }

    public void addParameter(Variable var) {
        this.parameters.add(var);
    }
}
