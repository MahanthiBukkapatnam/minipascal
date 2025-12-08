package edu.und.csci465.minipascal.parser;

import edu.und.csci465.minipascal.symboltable.GetSymbol;

import java.util.List;

public interface IParser {

    void setGetSymbol(GetSymbol getSymbol);

    List<TACInstr> getTac();

    void process();

    String runInterpreter();

}
