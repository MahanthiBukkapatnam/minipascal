package edu.und.csci465.minipascal.parser;


import edu.und.csci465.minipascal.lexer.InputOutputModule;
import edu.und.csci465.minipascal.symboltable.GetSymbol;
import edu.und.csci465.minipascal.util.FileUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserDelivery2Test {

    private IParser parser;

    IParser makeParserObject() {
        return new ParserDelivery2();
    }

    @Nested
    class InvalidCases {
        @Test
        void parseInvalid1() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/invalid/invalid1.pas");
        }

        @Test
        void parseInvalid2() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/invalid/invalid2.pas");
        }
    }

    @Test
    void parse() {
        parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/expr1.pas");
    }

    @Test
    void parse2() {
        parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/expr2.pas");
        assertEquals("500\n", parser.runInterpreter() );
    }

    @Test
    void parseAllConstants() {
        parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/allConstants.pas");
        System.out.println( "Output of Interpreter:");
        System.out.println( parser.runInterpreter() );
        assertEquals(
                "23\n" +
                "35\n" +
                "18\n" +
                "11\n" +
                "4\n" +
                "18\n" +
                "5\n" +
                "25\n", parser.runInterpreter() );
    }

    @Test
    void parseBasicMixed() {
        parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/basicMixed.pas");
        System.out.println( "Output of Interpreter:");
        System.out.println( parser.runInterpreter() );
        assertEquals(
                "0\n" +
                        "2\n" +
                        "5\n" +
                        "7\n" +
                        "-1\n" +
                        "-5\n" +
                        "3\n" +
                        "1\n" +
                        "-3\n" +
                        "1\n" +
                        "0\n" +
                        "0\n", parser.runInterpreter() );
    }

    @Test
    void parseAll4Operators() {
        parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/all4Operators.pas");

        System.out.println( "Output of Interpreter:");
        System.out.println( parser.runInterpreter() );

        assertEquals("-9\n" +
                "-1\n" +
                "2\n" +
                "-12\n" +
                "3\n" +
                "5\n" +
                "2\n" +
                "12\n", parser.runInterpreter() );
    }

    @Test
    void parseParanthesis() {
        parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/parenthesis.pas");
        System.out.println( "Output of Interpreter:");
        System.out.println( parser.runInterpreter() );
        assertEquals("9\n" +
                "5\n" +
                "-3\n" +
                "-1\n" +
                "1\n" +
                "0\n" +
                "2\n" +
                "-9\n" +
                "0\n" +
                "1\n" +
                "-3\n" +
                "-7\n" +
                "2\n" +
                "12\n", parser.runInterpreter() );
    }

    void parseMiniPascalFile(String fileName) {
        try {
            FileUtil.printFile(fileName);

            InputOutputModule ioModule = new InputOutputModule();
            ioModule.readFile(fileName);
            GetSymbol getsym = new GetSymbol();
            getsym.setIoModule(ioModule);
            getsym.process();

            parser = makeParserObject();
            parser.setGetSymbol(getsym);
            System.out.println("Printing the Intermediate Code:");
            System.out.println("--------------------------------------------");
            parser.process();
            System.out.println("--------------------------------------------");
            System.out.println();
            System.out.println();
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    void additionExpressionsTest() {
        String prefix = "src/test/resources/delivery2/expressions/addition/expr";
        for( int i=1; i<=10; i++) {
            parseMiniPascalFile(prefix + i + ".pas");
            System.out.println( "Output of Interpreter:");
            System.out.println( parser.runInterpreter() );
        }
    }

    @Test
    void multiplicationExpressionsTest() {
        String prefix = "src/test/resources/delivery2/expressions/multiplication/expr";
        for( int i=1; i<=10; i++) {
            parseMiniPascalFile(prefix + i + ".pas");
            System.out.println( "Output of Interpreter:");
            System.out.println( parser.runInterpreter() );
        }
    }

    @Test
    void mixedExpressionsTest() {
        String prefix = "src/test/resources/delivery2/expressions/mixed/expr";
        for( int i=1; i<=10; i++) {
            parseMiniPascalFile(prefix + i + ".pas");
            System.out.println( "Output of Interpreter:");
            System.out.println( parser.runInterpreter() );
        }
    }

}