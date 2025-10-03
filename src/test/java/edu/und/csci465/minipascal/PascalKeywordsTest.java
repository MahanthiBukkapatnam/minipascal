package edu.und.csci465.minipascal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PascalKeywordsTest {

    @Test
    void checkForKeyWord() {
        assertTrue( PascalKeywords.isKeyword("BEGIN") );
        assertTrue( PascalKeywords.isKeyword("begin") );
        assertFalse( PascalKeywords.isKeyword("begin1") );
    }
}
