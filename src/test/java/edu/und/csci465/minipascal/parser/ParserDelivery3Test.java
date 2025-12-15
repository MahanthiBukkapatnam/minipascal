package edu.und.csci465.minipascal.parser;


import edu.und.csci465.minipascal.lexer.InputOutputModule;
import edu.und.csci465.minipascal.symboltable.GetSymbol;
import edu.und.csci465.minipascal.util.FileUtil;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ParserDelivery3Test {

    private IParser parser;

    IParser makeParserObject() {
        return new ParserDelivery3();
    }

    @Test
    void parse() {
        parseMiniPascalFile("src/test/resources/delivery3/full/sample1.pas");
        generateTargetCode();
    }

    @Test
    void parseBooleanExpression() {
        parseMiniPascalFile("src/test/resources/delivery3/1boolean/booleanExpression.pas");
        generateTargetCode();
    }

    @Nested
    class EvaluatingBooleanExpressionTests {
        @Test
        void parseBooleanExpression1() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/boolean1.pas");
//            assertEquals("true\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseBooleanExpression2() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/boolean2.pas");
//            assertEquals("false\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseBooleanExpression3() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/boolean3.pas");
//            assertEquals("true\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseBooleanExpression4() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/boolean4.pas");
//            assertEquals("true\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseBooleanExpression5() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/boolean5.pas");
            //assertEquals("false\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseBooleanExpression6() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/boolean6.pas");
            //assertEquals("false\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseBooleanExpressionWithAnd() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/booleanExprAnd.pas");
            //assertEquals("false\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseBooleanExpressionWithOr() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/booleanExprOr.pas");
            //assertEquals("true\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseBooleanExpressionWithAndPlusOr() {
            parseMiniPascalFile("src/test/resources/delivery3/1boolean/booleanExprAndPlusOr.pas");
            //assertEquals("true\n", parser.runInterpreter() );
            generateTargetCode();
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class IfStatementsTests {

        @Order(1)
        @Test
        void parseIf1False() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/if/if1-false.pas");
            generateTargetCode();
        }
        @Order(2)
        @Test
        void parseIf1True() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/if/if1-true.pas");
            generateTargetCode();
        }

        @Order(3)
        @Test
        void parseIf2False() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/if/if2-false.pas");
            generateTargetCode();
        }
        @Order(4)
        @Test
        void parseIf2True() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/if/if2-true.pas");
            generateTargetCode();
        }

        @Order(5)
        @Test
        void parseIf3False() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/if/if3-false.pas");
            generateTargetCode();
        }

        @Order(6)
        @Test
        void parseIf3True() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/if/if3-true.pas");
            generateTargetCode();
        }
    }

    private void generateTargetCode() {
        MasmGenerator masmGenerator = new MasmGenerator(parser.getTac());
//        System.out.println("Printing Target Code:");
//        System.out.println("--------------------------------------------");
        System.out.println(masmGenerator.generateMasmCode());
//        System.out.println("--------------------------------------------");
    }

    @Nested
    class WhileStatementsTests {
        @Test
        void parseWhile1() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/while/while1.pas");
            generateTargetCode();
        }

        @Test
        void parseWhile2() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/while/while2.pas");
            generateTargetCode();
        }

        @Test
        void parseWhileFactorial() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/while/while-factorial.pas");
            generateTargetCode();
        }
    }

    @Nested
    class ForStatementsTests {
        @Test
        void parseFor1() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/for/for1.pas");
            generateTargetCode();
        }

        @Test
        void parseFor2() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/for/for2.pas");
            generateTargetCode();
        }

        @Test
        void parseFor3() {
            parseMiniPascalFile("src/test/resources/delivery3/2statement/for/for3.pas");
            generateTargetCode();
        }
    }

    @Nested
    class ReadWriteStatementsTests {
        @Test
        void parseReadWrite1() {
            parseMiniPascalFile("src/test/resources/delivery3/3-read-write/readWrite1.pas");
            generateTargetCode();
        }
    }

    @Nested
    class Delivery3DemoTests {
        @Test
        void sample1() {
            parseMiniPascalFile("src/test/resources/delivery3/full/sample1.pas");
            generateTargetCode();
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
        void parseExpr1() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/expr1.pas");
            //assertEquals("30\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseExpr2() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/expr2.pas");
            //assertEquals("500\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseExpr3() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/expr3.pas");
            //assertEquals("600\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseExpr4() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/expr4.pas");
            //assertEquals("-300\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseExpr5() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/expr5.pas");
            //assertEquals("3\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseExpr6() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/expr6.pas");
            //assertEquals("0\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseExpr7() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/expr7.pas");
            //assertEquals("4\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseExpr8() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/expr8.pas");
            //assertEquals("4\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseExpr9() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/expr9.pas");
//            assertEquals(
//                    "2\n" +
//                            "14\n" +
//                            "11\n" +
//                            "67\n" +
//                            "-23\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseExpr10() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/expr10.pas");
//            assertEquals(
//                    "3\n" +
//                            "-1\n" +
//                            "-6\n" +
//                            "-6\n" +
//                            "600\n", parser.runInterpreter() );
//            System.out.println(parser.runInterpreter());
            generateTargetCode();
        }

        @Test
        void parseAllConstants() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/allConstants.pas");
//            System.out.println( "Output of Interpreter:");
//            System.out.println( parser.runInterpreter() );
//            assertEquals(
//                    "23\n" +
//                            "35\n" +
//                            "18\n" +
//                            "11\n" +
//                            "4\n" +
//                            "18\n" +
//                            "5\n" +
//                            "25\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseBasicMixed() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/basicMixed.pas");
//            System.out.println( "Output of Interpreter:");
//            System.out.println( parser.runInterpreter() );
//            assertEquals(
//                    "0\n" +
//                            "2\n" +
//                            "5\n" +
//                            "7\n" +
//                            "-1\n" +
//                            "-5\n" +
//                            "3\n" +
//                            "1\n" +
//                            "-3\n" +
//                            "1\n" +
//                            "0\n" +
//                            "0\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseAll4Operators() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/all4Operators.pas");

//            System.out.println( "Output of Interpreter:");
//            System.out.println( parser.runInterpreter() );
//
//            assertEquals(
//                    "-9\n" +
//                    "-1\n" +
//                    "2\n" +
//                    "-12\n" +
//                    "3\n" +
//                    "5\n" +
//                    "2\n" +
//                    "12\n", parser.runInterpreter() );
            generateTargetCode();
        }

        @Test
        void parseParanthesis() {
            parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/parenthesis.pas");
//            System.out.println( "Output of Interpreter:");
//            System.out.println( parser.runInterpreter() );
//            assertEquals(
//                    "9\n" +
//                    "5\n" +
//                    "-3\n" +
//                    "-1\n" +
//                    "1\n" +
//                    "0\n" +
//                    "2\n" +
//                    "-9\n" +
//                    "0\n" +
//                    "1\n" +
//                    "-3\n" +
//                    "-7\n" +
//                    "2\n" +
//                    "12\n", parser.runInterpreter() );
            generateTargetCode();
        }
    }

    @Nested
    class SemanticTypeCheckingTests {
        @Nested
        class InvalidArithmeticCases {
            @Test
            void semanticError1() {
                parseMiniPascalFile("src/test/resources/delivery2/expressions/invalid/semantic/arithmetic/mixingIntAndBoolean.pas");
            }

            @Test
            void semanticError2() {
                parseMiniPascalFile("src/test/resources/delivery2/expressions/invalid/semantic/arithmetic/mixingIntAndBooleanCase2.pas");
            }

            @Test
            void semanticError3() {
                parseMiniPascalFile("src/test/resources/delivery2/expressions/invalid/semantic/arithmetic/mixingIntAndQuote.pas");
            }
        }

        @Nested
        class InvalidRelationalCases {
            @Test
            void semanticError1() {
                parseMiniPascalFile("src/test/resources/delivery2/expressions/invalid/semantic/relational/comparingIntAndChar.pas");
            }
            @Test
            void semanticError2() {
                parseMiniPascalFile("src/test/resources/delivery2/expressions/invalid/semantic/relational/comparingIntAndBoolean.pas");
            }
            @Test
            void semanticError3() {
                parseMiniPascalFile("src/test/resources/delivery2/expressions/invalid/semantic/relational/comparingIntAndQuote.pas");
            }

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