package edu.und.csci465.minipascal.symboltable;

public class Symbol {
    String name;
    VariableType variableType; // For functions this is the return type

    public Symbol(String name, VariableType variableType) {
        this.name = name;
        this.variableType = variableType;
    }

    public String getName() {
        return name;
    }

    public VariableType getVariableType() {
        return variableType;
    }
}
