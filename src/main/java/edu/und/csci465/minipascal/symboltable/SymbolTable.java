package edu.und.csci465.minipascal.symboltable;

import java.util.*;

public class SymbolTable {
    int currentLevel;	// nesting level of current scope
    Variable undefinedVar;	// object node for erroneous symbols
    public Scope topScope;	// topmost procedure scope

    //MB:  private Parser parser;

    public SymbolTable() {
        //this.parser = parser;
        this.topScope = null; // new Scope();
        this.currentLevel = 0;
        undefinedVar = new Variable("undef", VariableType.UNDEFINED);
        undefinedVar.adr = 0;
        undefinedVar.level = 0;
    }

    public void printTable() {
        System.out.println("Table contents: ");
        Scope scope = this.topScope;
        while (scope != null) {
            System.out.println(scope);
            scope.printLocals();
            scope = scope.next;
        }
        System.out.println();
        System.out.println();
    }

    // open a new scope and make it the current scope (topScope)
    public void openScope(int type) {
        Scope new_scope = new Scope(type, this.currentLevel++);
        new_scope.next = topScope;
        this.topScope = new_scope;
    }

    // close the current scope
    public void closeScope() {
        this.topScope = topScope.next;
        this.currentLevel--;
    }

    // TODO: should the store location come from the parser?
    public Function addFunction(String name, VariableType variableType, int label) {
        Function function = new Function(name, variableType, label);
        this.topScope.addSymbol(function);
        return function;
    }

    public Procedure addProcedure(String name) {
        Procedure procedure = new Procedure(name, VariableType.PROCEDURE);
        this.topScope.addSymbol(procedure);
        return procedure;
    }

    public Variable addVariable(String name, VariableType variableType) {
        Variable new_variable = new Variable(name, variableType);
        this.topScope.addSymbol(new_variable);
        return new_variable;
    }

    public boolean isSymbolDeclared(String name) {
        if(this.findSymbol(name)==undefinedVar) {
            return false;
        }
        return true;
    }

    // search the name in all open scopes and return its object node
    public Symbol findSymbol(String name) {
        Scope scope = this.topScope;
        Symbol symbol;
        while (scope != null) {
            symbol = scope.findSymbol(name);
            if (symbol != null) {
                return symbol;
            }
            scope = scope.next;
        }

        //printSemanticError("'" + name + "' is undeclared");
        return undefinedVar;
    }

    public void printSemanticError(String str) {
        System.out.println(str);
    }

}






