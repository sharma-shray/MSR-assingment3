package freenet.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HTMLDecoderTest {
    @Test
    public void testDecode() {
        assertEquals("S", HTMLDecoder.decode("S"));
        assertEquals("S", HTMLDecoder.decode("S"));
        assertEquals("", HTMLDecoder.decode(""));
        assertEquals("S", HTMLDecoder.decode("S"));
    }

    @Test
    public void testDecode2() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        HTMLDecoder.decode("S");
    }

    @Test
    public void testIsLetterOrDigit() {
        assertTrue(HTMLDecoder.isLetterOrDigit('A'));
        assertFalse(HTMLDecoder.isLetterOrDigit('\u0000'));
        assertTrue(HTMLDecoder.isLetterOrDigit('a'));
        assertTrue(HTMLDecoder.isLetterOrDigit('0'));
        assertFalse(HTMLDecoder.isLetterOrDigit('?'));
        assertFalse(HTMLDecoder.isLetterOrDigit('\\'));
    }

    @Test
    public void testIsHexDigit() {
        assertTrue(HTMLDecoder.isHexDigit('A'));
        assertFalse(HTMLDecoder.isHexDigit('\u0000'));
        assertTrue(HTMLDecoder.isHexDigit('a'));
        assertFalse(HTMLDecoder.isHexDigit('Z'));
        assertFalse(HTMLDecoder.isHexDigit('z'));
        assertTrue(HTMLDecoder.isHexDigit('0'));
    }

    @Test
    public void testIsLetter() {
        assertTrue(HTMLDecoder.isLetter('A'));
        assertFalse(HTMLDecoder.isLetter('\u0000'));
        assertTrue(HTMLDecoder.isLetter('a'));
        assertFalse(HTMLDecoder.isLetter('\\'));
    }

    @Test
    public void testIsHexLetter() {
        assertTrue(HTMLDecoder.isHexLetter('A'));
        assertFalse(HTMLDecoder.isHexLetter('\u0000'));
        assertTrue(HTMLDecoder.isHexLetter('a'));
        assertFalse(HTMLDecoder.isHexLetter('Z'));
        assertFalse(HTMLDecoder.isHexLetter('z'));
    }

    @Test
    public void testIsDigit() {
        assertFalse(HTMLDecoder.isDigit('A'));
        assertFalse(HTMLDecoder.isDigit('\u0000'));
        assertTrue(HTMLDecoder.isDigit('0'));
    }

    @Test
    public void testCompact() {
        assertEquals("S", HTMLDecoder.compact("S"));
        assertEquals("S", HTMLDecoder.compact("S"));
        assertEquals("S", HTMLDecoder.compact("S"));
        assertEquals("S", HTMLDecoder.compact("S"));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(HTMLDecoder.isWhitespace(' '));
        assertFalse(HTMLDecoder.isWhitespace('\u0000'));
        assertTrue(HTMLDecoder.isWhitespace('\n'));
        assertTrue(HTMLDecoder.isWhitespace(' '));
        assertFalse(HTMLDecoder.isWhitespace('\u0000'));
        assertTrue(HTMLDecoder.isWhitespace('\n'));
        assertTrue(HTMLDecoder.isWhitespace(' '));
        assertFalse(HTMLDecoder.isWhitespace('\u0000'));
        assertTrue(HTMLDecoder.isWhitespace('\n'));
        assertTrue(HTMLDecoder.isWhitespace(' '));
        assertFalse(HTMLDecoder.isWhitespace('\u0000'));
        assertTrue(HTMLDecoder.isWhitespace('\n'));
    }
}

