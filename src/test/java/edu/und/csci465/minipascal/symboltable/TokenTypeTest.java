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
}
