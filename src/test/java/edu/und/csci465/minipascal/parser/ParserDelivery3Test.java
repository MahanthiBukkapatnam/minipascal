package edu.und.csci465.minipascal.parser;


import edu.und.csci465.minipascal.lexer.InputOutputModule;
import edu.und.csci465.minipascal.symboltable.GetSymbol;
import edu.und.csci465.minipascal.util.FileUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserDelivery3Test {

    private IParser parser;

    IParser makeParserObject() {
        return new ParserDelivery3();
    }

    @Test
    void parse() {
        parseMiniPascalFile("src/test/resources/delivery3/full/sample1.pas");
    }

    @Test
    void parseBooleanExpression() {
        parseMiniPascalFile("src/test/resources/delivery3/boolean/booleanExpression.pas");
    }

    @Nested
    class EvaluatingBooleanExpressionTests {
        @Test
        void parseBooleanExpression1() {
            parseMiniPascalFile("src/test/resources/delivery3/boolean/boolean1.pas");
            assertEquals("true\n", parser.runInterpreter() );
        }

        @Test
        void parseBooleanExpression2() {
            parseMiniPascalFile("src/test/resources/delivery3/boolean/boolean2.pas");
            assertEquals("false\n", parser.runInterpreter() );
        }

        @Test
        void parseBooleanExpression3() {
            parseMiniPascalFile("src/test/resources/delivery3/boolean/boolean3.pas");
            assertEquals("true\n", parser.runInterpreter() );
        }

        @Test
        void parseBooleanExpression4() {
            parseMiniPascalFile("src/test/resources/delivery3/boolean/boolean4.pas");
            assertEquals("true\n", parser.runInterpreter() );
        }

        @Test
        void parseBooleanExpression5() {
            parseMiniPascalFile("src/test/resources/delivery3/boolean/boolean5.pas");
            assertEquals("false\n", parser.runInterpreter() );
        }

        @Test
        void parseBooleanExpression6() {
            parseMiniPascalFile("src/test/resources/delivery3/boolean/boolean6.pas");
            assertEquals("false\n", parser.runInterpreter() );
        }

        @Test
        void parseBooleanExpressionWithAnd() {
            parseMiniPascalFile("src/test/resources/delivery3/boolean/booleanExprAnd.pas");
            assertEquals("false\n", parser.runInterpreter() );
        }

        @Test
        void parseBooleanExpressionWithOr() {
            parseMiniPascalFile("src/test/resources/delivery3/boolean/booleanExprOr.pas");
            assertEquals("true\n", parser.runInterpreter() );
        }

        @Test
        void parseBooleanExpressionWithAndPlusOr() {
            parseMiniPascalFile("src/test/resources/delivery3/boolean/booleanExprAndPlusOr.pas");
            assertEquals("true\n", parser.runInterpreter() );
        }
    }

    @Nested
    class RegressionTests {
        @Test
        void parse() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/expr1.pas");
        }

        @Test
        void parse2() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/expr2.pas");
            assertEquals("500\n", parser.runInterpreter() );
        }

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

}