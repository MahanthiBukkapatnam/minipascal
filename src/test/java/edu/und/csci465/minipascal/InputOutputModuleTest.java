package edu.und.csci465.minipascal;


import org.junit.jupiter.api.Test;

class InputOutputModuleTest {

    @Test
    void readFile() {
        InputOutputModule ioModule = new InputOutputModule();
        ioModule.readFile("src/test/resources/sample.txt");
        ioModule.process();
        Output output = ioModule.getOutput();
        output.print();
    }
}