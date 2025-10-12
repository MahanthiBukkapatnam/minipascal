package edu.und.csci465.minipascal.lexer;

import edu.und.csci465.minipascal.lexer.Input;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputTest {

    @Test
    public void readFileTest() {
        List<String> strings = Input.readFile("src/test/resources/basic.pas");
        assertEquals(3, strings.size());
        assertEquals("Hello", strings.get(0));
        assertEquals("Mini", strings.get(1));
        assertEquals("Pascal", strings.get(2));
    }

    @Test
    public void readFileAndKeepEolTest() {
        String fileContent = Input.readFileAndKeepEol("src/test/resources/basic.pas");
        System.out.println("[" + fileContent + "]");
        assertEquals("Hello\r\nMini\r\nPascal\r\n", fileContent);
    }

}
