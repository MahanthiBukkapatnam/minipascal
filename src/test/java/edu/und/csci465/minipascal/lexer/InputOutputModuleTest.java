package edu.und.csci465.minipascal.lexer;


import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputOutputModuleTest {

    @Test
    void readFile() {
        InputOutputModule ioModule = new InputOutputModule();
        ioModule.readFile("src/test/resources/sample.txt");
        System.out.println(ioModule.getFileContent());

        printCharsFromInput(ioModule);
    }

    @Nested
    class SpacesTests {
        @Test
        void readFileWithSpaces() {
            InputOutputModule ioModule = new InputOutputModule();
            ioModule.readFile("src/test/resources/spaces/spaces5.txt");
            System.out.println(ioModule.getFileContent());

            printCharsFromInput(ioModule);
        }
    }

    @Test
    void onNewObjectCheckCurrentLineAndColumn() {
        InputOutputModule ioModule = new InputOutputModule();
        assertEquals(0,ioModule.getPosition().getLineNumber());
        assertEquals(0,ioModule.getPosition().getColumnNumber());
    }

    @Test
    void withEmptyStringAsContent() {
        InputOutputModule ioModule = new InputOutputModule();
        ioModule.fileContent = "";
        assertEquals(0,ioModule.currentChar());
        assertEquals(0,ioModule.peek());
    }

    @Test
    void withNewLineAsContent() {
        InputOutputModule ioModule = new InputOutputModule();
        ioModule.fileContent = "\n";
        assertEquals(10,ioModule.currentChar());
        assertEquals(0,ioModule.peek());
    }

    @Test
    void withNewLineAsContent2() {
        InputOutputModule ioModule = new InputOutputModule();
        ioModule.fileContent = "\r\n";
        assertEquals(13,ioModule.currentChar());
        assertEquals(10,ioModule.peek());
        ioModule.next();
        assertEquals(10,ioModule.currentChar());
        assertEquals(0,ioModule.peek());
    }

    @Test
    void withOneWord() {
        InputOutputModule ioModule = new InputOutputModule();
        ioModule.fileContent = "Hello\r\n";

        printCharsFromInput(ioModule);
    }

    @Test
    void with2Lines() {
        InputOutputModule ioModule = new InputOutputModule();
        ioModule.fileContent = "Hello\r\nworld";

        printCharsFromInput(ioModule);
    }

    @Test
    void with3Lines() {
        InputOutputModule ioModule = new InputOutputModule();
        ioModule.fileContent = "Hello\r\nworld\r\nmini";
        printCharsFromInput(ioModule);
    }

    private static void printCharsFromInput(InputOutputModule ioModule) {
        while( ioModule.currentChar() != 0 ) {
            int ch = ioModule.currentChar();
            int peekCh = ioModule.peek();
            System.out.println( "["+ ioModule.currentChar() + "]," + " [" + ch + "], peek=[" + peekCh + "], position=" + ioModule.getPosition().toString());
            ioModule.next();
        }

        System.out.println( "[ EOF ]," + " [" + 0 + "]," + " position=" + ioModule.getPosition().toString());
    }

}