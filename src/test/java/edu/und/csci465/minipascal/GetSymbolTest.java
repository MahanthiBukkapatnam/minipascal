package edu.und.csci465.minipascal;


import org.junit.jupiter.api.Test;

class GetSymbolTest {
    @Test
    void readFile() {
        InputOutputModule ioModule = new InputOutputModule();
        ioModule.readFile("src/test/resources/sample.txt");
        ioModule.process();

        GetSymbol getsym = new GetSymbol();
        getsym.setInputOutputModule(ioModule);
        getsym.process();
    }
}