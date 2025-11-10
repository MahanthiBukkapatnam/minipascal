package edu.und.csci465.minipascal.chatgpt;

import edu.und.csci465.minipascal.lexer.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

class MiniPascalToTACTest {

    List<MiniPascalToTAC.TACInstr> compileSource(String source) {
        MiniPascalToTAC.CompilerCore compiler = new MiniPascalToTAC.CompilerCore(source);
        return compiler.compile();
    }

    void printTAC(List<MiniPascalToTAC.TACInstr> tac) {
        for (MiniPascalToTAC.TACInstr i : tac) {
            System.out.println(i);
        }
    }

    @Test
    void sample1Test() {
        String source = Input.readFileAndKeepEol("src/test/resources/delivery2/sample1.pas");
        List<MiniPascalToTAC.TACInstr> tac = compileSource(source);
        printTAC(tac);
    }

    @Test
    void sample2Test() {
        String source = Input.readFileAndKeepEol("src/test/resources/delivery2/sample2.pas");
        List<MiniPascalToTAC.TACInstr> tac = compileSource(source);
        printTAC(tac);
    }

    @Test
    void additionExpressionsTest() {
        String prefix = "src/test/resources/delivery2/expressions/addition/expr";
        for( int i=1; i<=10; i++) {
            String fileName = prefix + i + ".pas";
            compileAndGenerateTAC(fileName);
        }
    }

    @Test
    void multiplicationExpressionsTest() {
        String prefix = "src/test/resources/delivery2/expressions/multiplication/expr";
        for( int i=1; i<=10; i++) {
            String fileName = prefix + i + ".pas";
            compileAndGenerateTAC(fileName);
        }
    }

    @Test
    void mixedExpressionsTest() {
        String prefix = "src/test/resources/delivery2/expressions/mixed/expr";
        for( int i=1; i<=10; i++) {
            String fileName = prefix + i + ".pas";
            compileAndGenerateTAC(fileName);
        }
    }

    private void compileAndGenerateTAC(String fileName) {
        System.out.println("------------------------------------------------");
        System.out.println("Translating Source: " + fileName);
        System.out.println("-----------------------");

        String source = Input.readFileAndKeepEol(fileName);
        List<MiniPascalToTAC.TACInstr> tac = compileSource(source);
        printTAC(tac);
        System.out.println("------------------------------------------------");
    }

    @Test
    void parse2() {
        compileAndGenerateTAC("src/test/resources/delivery2/expressions/mixed/expr2.pas");
    }

}