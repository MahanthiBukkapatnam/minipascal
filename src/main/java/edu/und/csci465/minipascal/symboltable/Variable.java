package edu.und.csci465.minipascal.symboltable;

public class Variable extends Symbol {
    public int adr;
    public int level;

    public Variable(String name, VariableType variableType) {
        super(name, variableType);
    }

    public int getAdr() {
        return adr;
    }

    public int getLevel() {
        return level;
    }
}
