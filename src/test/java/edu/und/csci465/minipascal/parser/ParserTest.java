package edu.und.csci465.minipascal.parser;


import edu.und.csci465.minipascal.lexer.InputOutputModule;
import edu.und.csci465.minipascal.symboltable.GetSymbol;
import edu.und.csci465.minipascal.util.FileUtil;
import org.junit.jupiter.api.Test;

class ParserTest {

    @Test
    void parse() {
        parseMiniPascalFile("src/test/resources/pascalPrograms/0spaces/spaces1.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/0spaces/spaces2.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/0spaces/spaces3.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/0spaces/spaces4.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/0spaces/spaces5.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/1comments/line/comments1.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/1comments/line/comments2.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/1comments/brace/braceComment1.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/1comments/brace/braceComment2.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/1comments/brace/braceComment3.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/1comments/block/blockComment1.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/1comments/block/blockComment2.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/1comments/block/blockComment3.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/2quote/withQuoteString.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/2quote/withSingleQuote.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/2quote/withEmptyQuote.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/2quote/withUnterminatedQuote.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/3invalidIdentifier/invalidIdentifier1.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/3invalidIdentifier/invalidIdentifier2.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/3invalidIdentifier/invalidIdentifier3.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/3invalidIdentifier/invalidIdentifier4.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/3invalidIdentifier/invalidIdentifier5.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/4operator/operatorLessThan.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/4operator/operatorGreaterThan.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/4operator/operatorsListAll.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/invalid/invalid.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/valid/keywords.pas");
        parseMiniPascalFile("src/test/resources/pascalPrograms/valid/valid.pas");
        parseMiniPascalFile("src/test/resources/sample6-cleanPascalProgram.pas");
        parseMiniPascalFile("src/test/resources/sample7-pascalProgramWithLexicalError.pas");
    }

    @Test
    void parse2() {
        parseMiniPascalFile("src/test/resources/delivery2/expressions/mixed/expr2.pas");
    }

    void parseMiniPascalFile(String fileName) {
        try {
            FileUtil.printFile(fileName);

            InputOutputModule ioModule = new InputOutputModule();
            ioModule.readFile(fileName);
            GetSymbol getsym = new GetSymbol();
            getsym.setIoModule(ioModule);
            getsym.process();

            Parser parser = new Parser();
            parser.setGetSymbol(getsym);
            System.out.println("Printing the Token Stream:");
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