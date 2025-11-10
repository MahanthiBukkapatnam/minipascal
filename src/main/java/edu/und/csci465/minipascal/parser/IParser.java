package edu.und.csci465.minipascal.parser;

import edu.und.csci465.minipascal.symboltable.GetSymbol;

public interface IParser {

    void setGetSymbol(GetSymbol getSymbol);

    void process();

    String runInterpreter();
}
