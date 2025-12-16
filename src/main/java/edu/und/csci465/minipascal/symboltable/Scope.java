package edu.und.csci465.minipascal.symboltable;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    public Scope next;
    public int returnType, frameSize, level;

    private HashMap<String, Symbol> locals = new HashMap<String, Symbol>();   // to locally declared objects

    public Scope(int type, int level) {
        this.returnType = type;
        this.level = level;
        this.frameSize = 0;
    }

    public HashMap<String, Symbol> getLocals() {
        return locals;
    }

    public void printLocals() {
        for (Map.Entry<String, Symbol> symbol_entry : this.locals.entrySet()) {
            String name = symbol_entry.getKey();
            Variable var = (Variable) symbol_entry.getValue();
            for (int i=0; i<var.level; i++) {
                System.out.print("  ");
            }
            System.out.println(name + ": "+ Integer.toString(var.adr));
        }
    }

    public void checkUniqueSymbolName(String name) {
        for (String local_name : this.locals.keySet()) {
            if (local_name.equals(name)) {
                printSemanticError("name '" + name + "' declared twice");
            }
        }
    }

    public void addSymbol(Function fn) {
        this.checkUniqueSymbolName(fn.name);
        this.locals.put(fn.name, fn);
    }

    public void addSymbol(Procedure pc) {
        this.checkUniqueSymbolName(pc.name);
        this.locals.put(pc.name, pc);
    }

    public void addSymbol(Variable var) {
        this.checkUniqueSymbolName(var.name);
        this.locals.put(var.name, var);
        var.adr = ++this.frameSize;
    }

    public Symbol findSymbol(String name) {
        for (Map.Entry<String, Symbol> symbol_entry : this.locals.entrySet()) {
            String symbol_name = symbol_entry.getKey();
            Symbol symbol_value = symbol_entry.getValue();
            if (name.equals(symbol_name)) {
                return symbol_value;
            }
        }
        return null;
    }

    public void printSemanticError(String str) {
        System.out.println(str);
    }
}
