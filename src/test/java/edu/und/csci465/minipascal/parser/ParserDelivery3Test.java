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
        parseMiniPascalFile("src/test/resources/delivery3/1boolean/booleanExpression.pas");
    }

    @Nested
    class EvaluatingBooleanExpressionTests {
        @Test
        void parseBooleanExpression1() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/boolean1.pas");
            assertEquals("true\n", parser.runInterpreter() );
        }

        @Test
        void parseBooleanExpression2() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/boolean2.pas");
            assertEquals("false\n", parser.runInterpreter() );
        }

        @Test
        void parseBooleanExpression3() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/boolean3.pas");
            assertEquals("true\n", parser.runInterpreter() );
        }

        @Test
        void parseBooleanExpression4() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/boolean4.pas");
            assertEquals("true\n", parser.runInterpreter() );
        }

        @Test
        void parseBooleanExpression5() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/boolean5.pas");
            assertEquals("false\n", parser.runInterpreter() );
        }

        @Test
        void parseBooleanExpression6() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/boolean6.pas");
            assertEquals("false\n", parser.runInterpreter() );
        }

        @Test
        void parseBooleanExpressionWithAnd() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/booleanExprAnd.pas");
            assertEquals("false\n", parser.runInterpreter() );
        }

        @Test
        void parseBooleanExpressionWithOr() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/booleanExprOr.pas");
            assertEquals("true\n", parser.runInterpreter() );
        }

        @Test
        void parseBooleanExpressionWithAndPlusOr() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/booleanExprAndPlusOr.pas");
            assertEquals("true\n", parser.runInterpreter() );
        }
    }

    @Nested
    class EvaluatingIfStatementsTests {
        @Test
        void parseIf1() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/if/if1.pas");
        }

        @Test
        void parseIf2() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/if/if2.pas");
        }

        @Test
        void parseIf31() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/if/if3-1.pas");
        }

        @Test
        void parseIf32() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/if/if3-2.pas");
        }
    }

    @Nested
    class EvaluatingWhileStatementsTests {
        @Test
        void parseWhile1() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/while/while1.pas");
        }

        @Test
        void parseWhile2() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/while/while2.pas");
        }
    }

    @Nested
    class EvaluatingForStatementsTests {
        @Test
        void parseFor1() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/for/for1.pas");
        }

        @Test
        void parseFor2() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/for/for2.pas");
        }
    }

    @Nested
    class RegressionTests {
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