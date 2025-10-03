package edu.und.csci465.minipascal;


import org.junit.jupiter.api.Test;

class ParserTest {
    @Test
    void readFile() {
        InputOutputModule ioModule = new InputOutputModule();
        ioModule.readFile("src/test/resources/sample4.txt");

        GetSymbol getsym = new GetSymbol();
        getsym.setInputOutputModule(ioModule);
        getsym.process();

        Parser parser = new Parser();
        parser.setGetSymbol(getsym);
        parser.process();
    }
}