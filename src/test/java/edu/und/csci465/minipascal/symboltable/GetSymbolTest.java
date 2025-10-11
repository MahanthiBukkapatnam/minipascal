package edu.und.csci465.minipascal.symboltable;


import edu.und.csci465.minipascal.lexer.InputOutputModule;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GetSymbolTest {

    GetSymbol getsym = new GetSymbol();

    @Nested
    class SpacesTests {
        @Test
        void readFile() {
            processFile("src/test/resources/spaces/comments1.txt");
            processFile("src/test/resources/spaces/spaces2.txt");
            processFile("src/test/resources/spaces/spaces3.txt");
            processFile("src/test/resources/spaces/spaces4.txt");
            processFile("src/test/resources/spaces/spaces5.txt");
        }
    }

    //I am visually inspecting the correctness of the output
    @Nested
    class CommentsTests {
        @Test
        void readFileWithSingleLineComment() {
            processFile("src/test/resources/comments/comments1.txt");
        }
        @Test
        void readFileWithSingleLineComment2() {
            processFile("src/test/resources/comments/comments2.txt");
        }
    }

    @Nested
    class BraceCommentsTests {
        @Test
        void braceCommentTest1() {
            processFile("src/test/resources/comments/brace/braceComment1.txt");
        }
        @Test
        void braceCommentTest2() {
            processFile("src/test/resources/comments/brace/braceComment2.txt");
        }
        @Test
        void braceCommentTest3() {
            Exception ex = assertThrows(RuntimeException.class, () -> processFile("src/test/resources/comments/brace/braceComment3.txt") );
            assertEquals("Unfinished Brace Comment at: Position{lineNumber=2, columnNumber=8}", ex.getMessage());
        }
    }

    @Nested
    class BlockCommentsTests {
        @Test
        void braceCommentTest1() {
            processFile("src/test/resources/comments/block/blockComment1.txt");
        }

        @Test
        void braceCommentTest2() {
            processFile("src/test/resources/comments/block/blockComment2.txt");
        }

        @Test
        void braceCommentTest3() {
            Exception ex = assertThrows(RuntimeException.class, () -> processFile("src/test/resources/comments/block/blockComment3.txt") );
            assertEquals("Unfinished Block Comment at: Position{lineNumber=2, columnNumber=8}", ex.getMessage());
        }
    }

    private void processFile(String fileName) {
        System.out.println();
        System.out.println("-----------------------------------------------");
        getsym.setInputOutputModule(getInputOutputModule(fileName));
        System.out.println(fileName);
        getsym.process();
    }

    private static InputOutputModule getInputOutputModule(String fileName) {
        InputOutputModule ioModule = new InputOutputModule();
        ioModule.readFile(fileName);
        return ioModule;
    }
}