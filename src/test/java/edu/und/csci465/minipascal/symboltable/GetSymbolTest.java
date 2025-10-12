package edu.und.csci465.minipascal.symboltable;


import edu.und.csci465.minipascal.lexer.InputOutputModule;
import edu.und.csci465.minipascal.util.FileUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GetSymbolTest {

    GetSymbol getsym = new GetSymbol();


    @Nested
    class SpacesTests {
        @Test
        void spaces1Test() {
            processFile("src/test/resources/pascalPrograms/0spaces/spaces1.pas");
        }
        @Test
        void spaces2Test() {
            processFile("src/test/resources/pascalPrograms/0spaces/spaces2.pas");
        }
        @Test
        void spaces3Test() {
            processFile("src/test/resources/pascalPrograms/0spaces/spaces3.pas");
        }
        @Test
        void spaces4Test() {
            processFile("src/test/resources/pascalPrograms/0spaces/spaces4.pas");
        }
        @Test
        void spaces5Test() {
            processFile("src/test/resources/pascalPrograms/0spaces/spaces5.pas");
        }
    }

    //I am visually inspecting the correctness of the output
    @Nested
    class CommentsTests {
        @Test
        void readFileWithSingleLineComment() {
            List<Token> tokens = processFile("src/test/resources/pascalPrograms/1comments/line/comments1.pas");
            assertEquals(7, tokens.size());

            assertEquals("Line", tokens.get(0).lexeme);
            assertEquals("1", tokens.get(1).lexeme);
            assertEquals("Line", tokens.get(2).lexeme);
            assertEquals("2", tokens.get(3).lexeme);
            assertEquals("Line", tokens.get(4).lexeme);
            assertEquals("3", tokens.get(5).lexeme);
            assertEquals("EOFSYM", tokens.get(6).type.name());
        }

        @Test
        void readFileWithSingleLineComment2() {
            List<Token> tokens = processFile("src/test/resources/pascalPrograms/1comments/line/comments2.pas");
            assertEquals(7, tokens.size());

            assertEquals("Line", tokens.get(0).lexeme);
            assertEquals("1", tokens.get(1).lexeme);
            assertEquals("Line", tokens.get(2).lexeme);
            assertEquals("2", tokens.get(3).lexeme);
            assertEquals("Line", tokens.get(4).lexeme);
            assertEquals("3", tokens.get(5).lexeme);
            assertEquals("EOFSYM", tokens.get(6).type.name());
        }
    }

    @Nested
    class BraceCommentsTests {
        @Test
        void braceCommentTest1() {
            List<Token> tokens = processFile("src/test/resources/pascalPrograms/1comments/brace/braceComment1.pas");
            assertEquals(7, tokens.size());

            assertEquals("Line", tokens.get(0).lexeme);
            assertEquals("1", tokens.get(1).lexeme);
            assertEquals("Line", tokens.get(2).lexeme);
            assertEquals("2", tokens.get(3).lexeme);
            assertEquals("Line", tokens.get(4).lexeme);
            assertEquals("3", tokens.get(5).lexeme);
            assertEquals("EOFSYM", tokens.get(6).type.name());
        }
        @Test
        void braceCommentTest2() {
            List<Token> tokens = processFile("src/test/resources/pascalPrograms/1comments/brace/braceComment2.pas");
            assertEquals(7, tokens.size());

            assertEquals("Line", tokens.get(0).lexeme);
            assertEquals("1", tokens.get(1).lexeme);
            assertEquals("Line", tokens.get(2).lexeme);
            assertEquals("2", tokens.get(3).lexeme);
            assertEquals("Line", tokens.get(4).lexeme);
            assertEquals("3", tokens.get(5).lexeme);
            assertEquals("EOFSYM", tokens.get(6).type.name());
        }
        @Test
        void braceCommentTest3() {
            Exception ex = assertThrows(RuntimeException.class, () -> processFile("src/test/resources/pascalPrograms/1comments/brace/braceComment3.pas") );
            assertEquals("Unfinished Brace Comment at: Position{lineNumber=2, columnNumber=8}", ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }

    @Nested
    class BlockCommentsTests {
        @Test
        void braceCommentTest1() {
            processFile("src/test/resources/pascalPrograms/1comments/block/blockComment1.pas");
        }

        @Test
        void braceCommentTest2() {
            processFile("src/test/resources/pascalPrograms/1comments/block/blockComment2.pas");
        }

        @Test
        void braceCommentTest3() {
            Exception ex = assertThrows(RuntimeException.class, () -> processFile("src/test/resources/pascalPrograms/1comments/block/blockComment3.pas") );
            assertEquals("Unfinished Block Comment at: Position{lineNumber=2, columnNumber=8}", ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }

    @Nested
    class QuoteTests {
        @Test
        void test1() {
            processFile("src/test/resources/pascalPrograms/2quote/withQuoteString.pas");
        }

        @Test
        void test2() {
            processFile("src/test/resources/pascalPrograms/2quote/withSingleQuote.pas");
        }

        @Test
        void test3() {
            processFile("src/test/resources/pascalPrograms/2quote/withEmptyQuote.pas");
        }

        @Test
        void test4() {
            Exception ex = assertThrows(RuntimeException.class, ()-> {
                processFile("src/test/resources/pascalPrograms/2quote/withUnterminatedQuote.pas");
            });
            System.out.println(ex.getMessage());
        }

    }


    @Nested
    class invalidIdentifierTests {
        @Test
        void invalidIdentifier1Test() {
            Exception ex = assertThrows( RuntimeException.class, () -> processFile("src/test/resources/pascalPrograms/3invalidIdentifier/invalidIdentifier1.pas") );
            assertEquals("Invalid number: letters/underscore immediately after digitsPosition{lineNumber=1, columnNumber=2}", ex.getMessage());
            System.out.println(ex.getMessage());
        }

        @Test
        void invalidIdentifier2Test() {
            List<Token> tokens = processFile("src/test/resources/pascalPrograms/3invalidIdentifier/invalidIdentifier2.pas");
            assertEquals(3, tokens.size());
            assertEquals(TokenType.ILLEGAL, tokens.get(0).getType());
            assertEquals("$", tokens.get(0).lexeme);
            assertEquals(TokenType.THENSYM, tokens.get(1).getType());
            assertEquals(TokenType.EOFSYM, tokens.get(2).getType());
        }

        @Test
        void invalidIdentifier3Test() {
            List<Token> tokens = processFile("src/test/resources/pascalPrograms/3invalidIdentifier/invalidIdentifier3.pas");
            assertEquals(3, tokens.size());
            assertEquals(TokenType.ILLEGAL, tokens.get(0).getType());
            assertEquals("_", tokens.get(0).lexeme);
            assertEquals(TokenType.THENSYM, tokens.get(1).getType());
            assertEquals(TokenType.EOFSYM, tokens.get(2).getType());
        }
        @Test
        void invalidIdentifier4Test() {
            List<Token> tokens = processFile("src/test/resources/pascalPrograms/3invalidIdentifier/invalidIdentifier4.pas");
            assertEquals(4, tokens.size());

            assertEquals(TokenType.IDENTIFIER, tokens.get(0).getType());
            assertEquals("abcdefghijklmnopqrstuvwxyz12345", tokens.get(0).lexeme);

            assertEquals(TokenType.ILLEGAL, tokens.get(1).getType());
            assertEquals("#", tokens.get(1).lexeme);

            assertEquals(TokenType.NUMBER, tokens.get(2).getType());
            assertEquals("12345", tokens.get(2).lexeme);

            assertEquals(TokenType.EOFSYM, tokens.get(3).getType());
        }

        @Test
        void invalidIdentifier5Test() {
            Exception ex = assertThrows( RuntimeException.class, () -> processFile("src/test/resources/pascalPrograms/3invalidIdentifier/invalidIdentifier5.pas") );
            assertEquals("Invalid number: letters/underscore immediately after digitsPosition{lineNumber=1, columnNumber=6}", ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }

    @Nested
    class OperatorTests {
        @Test
        void testLessThan() {
            processFile("src/test/resources/pascalPrograms/4operator/operatorLessThan.pas");
        }
        @Test
        void testGreaterThan() {
            processFile("src/test/resources/pascalPrograms/4operator/operatorGreaterThan.pas");
        }
        @Test
        void testListAll() {
            processFile("src/test/resources/pascalPrograms/4operator/operatorsListAll.pas");
        }
    }

    @Nested
    class PascalProgramInvalidTests {
        @Test
        void invalidFileTest() {
            Exception ex = assertThrows(RuntimeException.class, () -> processFile("src/test/resources/pascalPrograms/invalid/invalid.pas") );
            System.out.println(ex.getMessage());
        }
    }

    @Nested
    class PascalProgramValidTests {

        @Test
        void allKeywordsTest() {
            processFile("src/test/resources/pascalPrograms/valid/keywords.pas");
        }

        @Test
        void validFile1Test() {
            processFile("src/test/resources/pascalPrograms/valid/valid.pas");
        }
    }

    List<Token> processFile(String fileName) {
        FileUtil.printFile(fileName);

        getsym.setIoModule(getInputOutputModule(fileName));
        getsym.process();

        System.out.println("Token Stream:");
        System.out.println("-----------------------------------------------");
        Token token = getsym.getNextToken();
        while( token !=null ) {
            System.out.printf("%20s, %20s\n", token.type, token.getLexeme());
            token = getsym.getNextToken();
        }
        System.out.println("-----------------------------------------------");
        return getsym.getTokens();
    }

    private static InputOutputModule getInputOutputModule(String fileName) {
        InputOutputModule ioModule = new InputOutputModule();
        ioModule.readFile(fileName);
        return ioModule;
    }
}