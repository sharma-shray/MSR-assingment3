package freenet.support;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HTMLEncoderTest {
    @Test
    public void testEncode() {
        assertEquals("S", HTMLEncoder.encode("S"));
    }

    @Test
    public void testEncodeToBuffer() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        HTMLEncoder.encodeToBuffer("S", new StringBuilder());
    }

    @Test
    public void testEncodeToBuffer2() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        HTMLEncoder.encodeToBuffer("", new StringBuilder(1));
    }

    @Test
    public void testEncodeXML() {
        assertEquals("S", HTMLEncoder.encodeXML("S"));
    }
}

