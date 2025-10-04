package edu.und.csci465.minipascal;


import org.junit.jupiter.api.Test;

class ParserTest {

    @Test
    void parse() {
        parseMiniPascalFile("src/test/resources/sample.txt");
        parseMiniPascalFile("src/test/resources/sample2.txt");
        parseMiniPascalFile("src/test/resources/sample3.txt");
        parseMiniPascalFile("src/test/resources/sample4.txt");
        parseMiniPascalFile("src/test/resources/sample5.txt");
        parseMiniPascalFile("src/test/resources/sample6-cleanPascalProgram.txt");
        parseMiniPascalFile("src/test/resources/sample7-pascalProgramWithLexicalError.txt");
    }

    private static void parseMiniPascalFile(String fileName) {
        InputOutputModule ioModule = new InputOutputModule();
        ioModule.readFile(fileName);

        System.out.println("");
        System.out.println("Printing original file: " + fileName);
        System.out.println("--------------------------------------------");
        Output output = ioModule.getOutput();
        output.print();
        System.out.println("--------------------------------------------");

        GetSymbol getsym = new GetSymbol();
        getsym.setInputOutputModule(ioModule);
        getsym.process();

        System.out.println("");
        Parser parser = new Parser();
        parser.setGetSymbol(getsym);
        System.out.println("Printing the Token Stream:");
        System.out.println("--------------------------------------------");
        parser.process();
        System.out.println("--------------------------------------------");
    }
}