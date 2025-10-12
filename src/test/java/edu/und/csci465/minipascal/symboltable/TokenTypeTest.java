package edu.und.csci465.minipascal.symboltable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenTypeTest {

    @Test
    void printAllTokenTypes() {
        for(TokenType tokenType : TokenType.values() ) {
            System.out.println(tokenType);
        }
    }

    @Test
    void tokenTypeTest() {
        assertEquals("ANDSYM", TokenType.ANDSYM.toString());
        assertEquals("VARSYM", TokenType.VARSYM.toString());
    }

    @Test
    void checkForKeyword() {
        assertTrue( TokenType.isKeyword("BEGIN") );
        assertTrue( TokenType.isKeyword("begin") );
        assertFalse( TokenType.isKeyword("begin1") );
    }

    @Test
    void checkForOperator() {
        assertTrue( TokenType.is2CharOperator('<','>') );
        assertTrue( TokenType.is2CharOperator(':','=') );
        assertTrue( TokenType.is2CharOperator('<','=') );
        assertTrue( TokenType.is2CharOperator('>','=') );

        assertTrue( TokenType.is1CharOperator('<') );
        assertTrue( TokenType.is1CharOperator('>') );
    }

}
