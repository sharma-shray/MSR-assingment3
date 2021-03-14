/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package freenet.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import freenet.node.FSParseException;
import freenet.support.io.LineReader;
import freenet.support.io.Readers;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test case for {@link freenet.support.SimpleFieldSet} class.
 *
 * @author Alberto Bacchelli &lt;sback@freenetproject.org&gt;
 */
public class SimpleFieldSetTest extends TestCase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final char KEY_VALUE_SEPARATOR = '=';

    /* A double string array used across all tests
     * it must not be changed in order to perform tests
     * correctly */
    private static final String[][] SAMPLE_STRING_PAIRS = {
            //directSubset
            {"foo", "bar"},
            {"foo.bar", "foobar"},
            {"foo.bar.foo", "foobar"},
            {"foo.bar.boo.far", "foobar"},
            {"foo2", "foobar.fooboo.foofar.foofoo"},
            {"foo3", KEY_VALUE_SEPARATOR + "bar"}};

    private static final String SAMPLE_END_MARKER = "END";

    @Test
    public void testConstructor() throws IOException {
        SimpleFieldSet actualSimpleFieldSet = new SimpleFieldSet(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true));
        assertNull(actualSimpleFieldSet.getHeader());
        assertTrue(actualSimpleFieldSet.isEmpty());
        assertEquals("AAAAAAAAAAAAAAAAAAAAAAAA", actualSimpleFieldSet.getEndMarker());
    }

    @Test
    public void testConstructor10() throws IOException {
        thrown.expect(EOFException.class);
        new SimpleFieldSet(new BufferedReader(new StringReader("")), true, true);
    }

    @Test
    public void testConstructor11() throws IOException {
        SimpleFieldSet actualSimpleFieldSet = new SimpleFieldSet(new BufferedReader(new StringReader("foo")), true, true,
                true, true);
        assertTrue(actualSimpleFieldSet.isEmpty());
        assertEquals("foo", actualSimpleFieldSet.getEndMarker());
    }

    @Test
    public void testConstructor12() throws IOException {
        thrown.expect(EOFException.class);
        new SimpleFieldSet(new BufferedReader(new StringReader("")), true, true, true, true);
    }

    @Test
    public void testConstructor13() throws IOException {
        SimpleFieldSet actualSimpleFieldSet = new SimpleFieldSet("Not all who wander are lost", true, true, true);
        assertTrue(actualSimpleFieldSet.isEmpty());
        assertEquals("Not all who wander are lost", actualSimpleFieldSet.getEndMarker());
    }

    @Test
    public void testConstructor14() throws IOException {
        SimpleFieldSet actualSimpleFieldSet = new SimpleFieldSet("Content", true, true, true);
        assertTrue(actualSimpleFieldSet.isEmpty());
        assertEquals("Content", actualSimpleFieldSet.getEndMarker());
    }

    @Test
    public void testConstructor15() throws IOException {
        thrown.expect(EOFException.class);
        new SimpleFieldSet("", true, true, true);
    }

    @Test
    public void testConstructor16() {
        assertTrue((new SimpleFieldSet(true)).isEmpty());
    }

    @Test
    public void testConstructor17() {
        assertTrue((new SimpleFieldSet(true, true)).isEmpty());
    }

    @Test
    public void testConstructor18() throws IOException {
        SimpleFieldSet actualSimpleFieldSet = new SimpleFieldSet(new String[]{"foo", "foo", "foo"}, true, true, true);
        assertTrue(actualSimpleFieldSet.isEmpty());
        assertEquals("foo", actualSimpleFieldSet.getEndMarker());
    }

    @Test
    public void testConstructor19() throws IOException {
        thrown.expect(EOFException.class);
        new SimpleFieldSet(new String[]{null, "foo", "foo"}, true, true, true);
    }

    @Test
    public void testConstructor2() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65});
        byteArrayInputStream.mark(0);
        SimpleFieldSet actualSimpleFieldSet = new SimpleFieldSet(
                SimpleFieldSet.readFrom(byteArrayInputStream, true, true, true, true));
        assertNull(actualSimpleFieldSet.getHeader());
        assertTrue(actualSimpleFieldSet.isEmpty());
        assertEquals("AAAAAAAAAAAAAAAAAAAAAAAA", actualSimpleFieldSet.getEndMarker());
    }

    @Test
    public void testConstructor20() throws IOException {
        SimpleFieldSet actualSimpleFieldSet = new SimpleFieldSet(new String[]{"", "foo", "foo"}, true, true, true);
        assertTrue(actualSimpleFieldSet.isEmpty());
        assertEquals("foo", actualSimpleFieldSet.getEndMarker());
    }

    @Test
    public void testConstructor21() throws IOException {
        SimpleFieldSet actualSimpleFieldSet = new SimpleFieldSet(new String[]{"Not all who wander are lost"}, true, true,
                true);
        assertTrue(actualSimpleFieldSet.isEmpty());
        assertEquals("Not all who wander are lost", actualSimpleFieldSet.getEndMarker());
    }

    @Test
    public void testConstructor3() throws IOException {
        SimpleFieldSet actualSimpleFieldSet = new SimpleFieldSet(Readers.fromStringArray(new String[]{"foo", "foo", "foo"}),
                3, 2, true, true, true);
        assertTrue(actualSimpleFieldSet.isEmpty());
        assertEquals("foo", actualSimpleFieldSet.getEndMarker());
    }

    @Test
    public void testConstructor4() throws IOException {
        thrown.expect(EOFException.class);
        new SimpleFieldSet(Readers.fromStringArray(new String[]{null, "foo", "foo"}), 3, 2, true, true, true);
    }

    @Test
    public void testConstructor5() throws IOException {
        SimpleFieldSet actualSimpleFieldSet = new SimpleFieldSet(Readers.fromStringArray(new String[]{"", "foo", "foo"}), 3,
                2, true, true, true);
        assertTrue(actualSimpleFieldSet.isEmpty());
        assertEquals("foo", actualSimpleFieldSet.getEndMarker());
    }

    @Test
    public void testConstructor6() throws IOException {
        SimpleFieldSet actualSimpleFieldSet = new SimpleFieldSet(Readers.fromStringArray(new String[]{"foo", "foo", "foo"}),
                3, 2, true, true, true, true);
        assertTrue(actualSimpleFieldSet.isEmpty());
        assertEquals("foo", actualSimpleFieldSet.getEndMarker());
    }

    @Test
    public void testConstructor7() throws IOException {
        thrown.expect(EOFException.class);
        new SimpleFieldSet(Readers.fromStringArray(new String[]{null, "foo", "foo"}), 3, 2, true, true, true, true);
    }

    @Test
    public void testConstructor8() throws IOException {
        SimpleFieldSet actualSimpleFieldSet = new SimpleFieldSet(Readers.fromStringArray(new String[]{"", "foo", "foo"}), 3,
                2, true, true, true, true);
        assertTrue(actualSimpleFieldSet.isEmpty());
        assertEquals("foo", actualSimpleFieldSet.getEndMarker());
    }

    @Test
    public void testConstructor9() throws IOException {
        SimpleFieldSet actualSimpleFieldSet = new SimpleFieldSet(new BufferedReader(new StringReader("foo")), true, true);
        assertTrue(actualSimpleFieldSet.isEmpty());
        assertEquals("foo", actualSimpleFieldSet.getEndMarker());
    }

    @Test
    public void testKeyIteratorConstructor() throws IOException {
        SimpleFieldSet.KeyIterator actualKeyIterator = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).new KeyIterator("Prefix");
        assertEquals("Prefix", actualKeyIterator.prefix);
        assertNull(actualKeyIterator.subsetIterator);
    }

    @Test
    public void testKeyIteratorConstructor2() throws IOException {
        SimpleFieldSet.KeyIterator actualKeyIterator = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).new KeyIterator(null);
        assertNull(actualKeyIterator.prefix);
        assertNull(actualKeyIterator.subsetIterator);
    }

    @Test
    public void testKeyIteratorHasNext() throws IOException {
        assertFalse((SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).new KeyIterator("Prefix")).hasNext());
    }

    @Test
    public void testPutSingle() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.putSingle("Key", "value");
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPutSingle2() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.putSingle("Key", "value");
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPutSingle3() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.putSingle("Key", "value");
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPutSingle4() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).putSingle("Key", null);
    }

    /**
     * Tests putSingle(String,String) method
     * trying to store a key with two paired
     * multi_level_chars (i.e. "..").
     */
    public void testSimpleFieldSetPutSingle_StringString_WithTwoPairedMultiLevelChars() {
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        String methodKey = "foo..bar.";
        String methodValue = "foobar";
        methodSFS.putSingle(methodKey, methodValue);
        assertEquals(methodSFS.subset("foo").subset("").subset("bar").get(""), methodValue);
        assertEquals(methodSFS.get(methodKey), methodValue);
    }

    /**
     * Tests putAppend(String,String) method
     * trying to store a key with two paired
     * multi_level_chars (i.e. "..").
     */
    public void testSimpleFieldSetPutAppend_StringString_WithTwoPairedMultiLevelChars() {
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        String methodKey = "foo..bar";
        String methodValue = "foobar";
        methodSFS.putAppend(methodKey, methodValue);
        assertEquals(methodSFS.get(methodKey), methodValue);
    }

    /**
     * Tests put() and get() methods
     * using a normal Map behaviour
     * and without MULTI_LEVEL_CHARs
     */
    public void testSimpleFieldSetPutAndGet_NoMultiLevel() {
        String[][] methodPairsArray = {
                {"A", "a"}, {"B", "b"}, {"C", "c"}, {"D", "d"}, {"E", "e"}, {"F", "f"}};
        assertTrue(checkPutAndGetPairs(methodPairsArray));
    }

    /**
     * Tests put() and get() methods
     * using a normal Map behaviour
     * and with MULTI_LEVEL_CHARs
     */
    public void testSimpleFieldSetPutAndGet_MultiLevel() {
        String[][] methodPairsArray_DoubleLevel = {
                {"A.A", "aa"},
                {"A.B", "ab"},
                {"A.C", "ac"},
                {"A.D", "ad"},
                {"A.E", "ae"},
                {"A.F", "af"}};
        String[][] methodPairsArray_MultiLevel = {
                {"A.A.A.A", "aa"},
                {"A.B.A", "ab"},
                {"A.C.Cc", "ac"},
                {"A.D.F", "ad"},
                {"A.E.G", "ae"},
                {"A.F.J.II.UI.BOO", "af"}};
        assertTrue(checkPutAndGetPairs(methodPairsArray_DoubleLevel));
        assertTrue(checkPutAndGetPairs(methodPairsArray_MultiLevel));
    }


    /**
     * It puts key-value pairs in a SimpleFieldSet
     * and verify if it can do the correspondant
     * get correctly.
     *
     * @param aPairsArray
     * @return true if it is correct
     */
    private boolean checkPutAndGetPairs(String[][] aPairsArray) {
        boolean retValue = true;
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        //putting values
        for (int i = 0; i < aPairsArray.length; i++)
            methodSFS.putSingle(aPairsArray[i][0], aPairsArray[i][1]);
        for (int i = 0; i < aPairsArray.length; i++)        //getting values
            retValue &= methodSFS.get(aPairsArray[i][0]).equals(aPairsArray[i][1]);
        retValue &= checkSimpleFieldSetSize(methodSFS, aPairsArray.length);
        return retValue;
    }

    @Test
    public void testGet() throws IOException {
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).get("Key"));
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).get("freenet.support.SimpleFieldSet"));
    }

    /**
     * Tests subset(String) method
     * putting two levels keys and
     * fetching it through subset() method
     * on the first level and then get()
     * on the second
     */
    public void testSimpleFieldSetSubset_String() {
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        String[][] methodPairsArray_MultiLevel = {
                {"A", "A", "aa"},
                {"A", "B", "ab"},
                {"A", "C", "ac"},
                {"A", "D", "ad"},
                {"A", "E", "ae"},
                {"A", "F", "af"}};
        //putting values
        for (int i = 0; i < methodPairsArray_MultiLevel.length; i++)
            methodSFS.putSingle(methodPairsArray_MultiLevel[i][0]
                            + SimpleFieldSet.MULTI_LEVEL_CHAR
                            + methodPairsArray_MultiLevel[i][1],
                    methodPairsArray_MultiLevel[i][2]);
        //getting subsets and then values
        for (int i = 0; i < methodPairsArray_MultiLevel.length; i++)
            assertEquals(
                    methodSFS.subset(
                            methodPairsArray_MultiLevel[i][0]).get(methodPairsArray_MultiLevel[i][1]),
                    methodPairsArray_MultiLevel[i][2]);
        assertTrue(checkSimpleFieldSetSize(methodSFS, methodPairsArray_MultiLevel.length));
    }

    @Test
    public void testSubset() throws IOException {
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).subset("Key"));
    }

    /**
     * Tests putAllOverwrite(SimpleFieldSet) method
     * trying to overwrite a whole SimpleFieldSet
     * with another with same keys but different
     * values
     */
    public void testPutAllOverwrite() {
        String methodAppendedString = "buu";
        SimpleFieldSet methodSFS = sfsFromSampleStringPairs();
        SimpleFieldSet methodNewSFS = this.sfsFromStringPairs(methodAppendedString);
        methodSFS.putAllOverwrite(methodNewSFS);
        for (int i = 0; i < SAMPLE_STRING_PAIRS.length; i++)
            assertEquals(methodSFS.get(SAMPLE_STRING_PAIRS[i][0]),
                    SAMPLE_STRING_PAIRS[i][1] + methodAppendedString);
        SimpleFieldSet nullSFS = new SimpleFieldSet(false);
        nullSFS.putAllOverwrite(methodNewSFS);
        for (int i = 0; i < SAMPLE_STRING_PAIRS.length; i++)
            assertEquals(nullSFS.get(SAMPLE_STRING_PAIRS[i][0]),
                    SAMPLE_STRING_PAIRS[i][1] + methodAppendedString);
    }

    @Test
    public void testPutAllOverwrite2() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.putAllOverwrite(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true));
    }

    /**
     * Tests put(String,SimpleFieldSet) method
     */
    public void testPut_StringSimpleFieldSet() {
        String methodKey = "prefix";
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        methodSFS.put(methodKey, sfsFromSampleStringPairs());
        for (int i = 0; i < SAMPLE_STRING_PAIRS.length; i++)
            assertEquals(
                    methodSFS.get(methodKey + SimpleFieldSet.MULTI_LEVEL_CHAR + SAMPLE_STRING_PAIRS[i][0]),
                    SAMPLE_STRING_PAIRS[i][1]);
    }

    /**
     * Tests put(String,SimpleFieldSet) method
     */
    public void testTPut_StringSimpleFieldSet() {
        String methodKey = "prefix";
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        methodSFS.tput(methodKey, sfsFromSampleStringPairs());
        for (int i = 0; i < SAMPLE_STRING_PAIRS.length; i++)
            assertEquals(methodSFS.get(methodKey + SimpleFieldSet.MULTI_LEVEL_CHAR + SAMPLE_STRING_PAIRS[i][0]),
                    SAMPLE_STRING_PAIRS[i][1]);
    }

    @Test
    public void testTput() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.tput("Key",
                SimpleFieldSet.readFrom(new ByteArrayInputStream(
                                new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                        true, true, true, true));
    }

    @Test
    public void testTput2() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).tput("Key", null);
    }

    /**
     * Tests put(String,SimpleFieldSet) and
     * tput(String,SimpleFieldSet) trying to
     * add empty data structures
     */
    public void testPutAndTPut_WithEmpty() {
        SimpleFieldSet methodEmptySFS = new SimpleFieldSet(true);
        SimpleFieldSet methodSampleSFS = sfsFromSampleStringPairs();
        try {
            methodSampleSFS.put("sample", methodEmptySFS);
            fail("Expected Exception Error Not Thrown!");
        } catch (IllegalArgumentException anException) {
            assertNotNull(anException);
        }
        try {
            methodSampleSFS.tput("sample", methodSampleSFS);
        } catch (IllegalArgumentException aException) {
            fail("Not expected exception thrown : " + aException.getMessage());
        }
    }

    /**
     * It creates a SFS from the SAMPLE_STRING_PAIRS
     * and putting a suffix after every value
     *
     * @param aSuffix to put after every value
     * @return the SimpleFieldSet created
     */
    private SimpleFieldSet sfsFromStringPairs(String aSuffix) {
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        //creating new
        for (int i = 0; i < SAMPLE_STRING_PAIRS.length; i++)
            methodSFS.putSingle(SAMPLE_STRING_PAIRS[i][0],
                    SAMPLE_STRING_PAIRS[i][1] + aSuffix);
        return methodSFS;
    }

    @Test
    public void testPut() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("Key", 'A');
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut10() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("freenet.support.SimpleFieldSet", 10.0);
        assertFalse(readFromResult.isEmpty());
        assertEquals("freenet.support.SimpleFieldSet==MTAuMA\nAAAAAAAAAAAAAAAAAAAAAAAA\n", readFromResult.toString());
    }

    @Test
    public void testPut11() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("freenet.support.SimpleFieldSet", 10.0);
        assertFalse(readFromResult.isEmpty());
        assertEquals("freenet.support.SimpleFieldSet==MTAuMA\nAAAAAAAAAAAAAAAAAAAAAAAA\n", readFromResult.toString());
    }

    @Test
    public void testPut12() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("Key", 42);
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut13() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.put("Key", 42);
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPut14() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("Key", 42);
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut15() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("freenet.support.SimpleFieldSet", 42);
        assertFalse(readFromResult.isEmpty());
        assertEquals("freenet.support.SimpleFieldSet=42\nAAAAAAAAAAAAAAAAAAAAAAAA\n", readFromResult.toString());
    }

    @Test
    public void testPut16() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("freenet.support.SimpleFieldSet", 42);
        assertFalse(readFromResult.isEmpty());
        assertEquals("freenet.support.SimpleFieldSet=42\nAAAAAAAAAAAAAAAAAAAAAAAA\n", readFromResult.toString());
    }

    @Test
    public void testPut17() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("Key", 42L);
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut18() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.put("Key", 42L);
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPut19() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("Key", 42L);
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut2() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.put("Key", 'A');
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPut20() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("freenet.support.SimpleFieldSet", 42L);
        assertFalse(readFromResult.isEmpty());
        assertEquals("freenet.support.SimpleFieldSet=42\nAAAAAAAAAAAAAAAAAAAAAAAA\n", readFromResult.toString());
    }

    @Test
    public void testPut21() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("freenet.support.SimpleFieldSet", 42L);
        assertFalse(readFromResult.isEmpty());
        assertEquals("freenet.support.SimpleFieldSet=42\nAAAAAAAAAAAAAAAAAAAAAAAA\n", readFromResult.toString());
    }

    @Test
    public void testPut22() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        thrown.expect(IllegalArgumentException.class);
        readFromResult.put("Key",
                SimpleFieldSet.readFrom(new ByteArrayInputStream(
                                new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                        true, true, true, true));
    }

    @Test
    public void testPut23() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).put("Key", (SimpleFieldSet) null);
    }

    @Test
    public void testPut24() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("Key", (short) 1);
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut25() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.put("Key", (short) 1);
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPut26() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("Key", (short) 1);
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut27() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("freenet.support.SimpleFieldSet", (short) 1);
        assertFalse(readFromResult.isEmpty());
        assertEquals("freenet.support.SimpleFieldSet=1\nAAAAAAAAAAAAAAAAAAAAAAAA\n", readFromResult.toString());
    }

    @Test
    public void testPut28() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("freenet.support.SimpleFieldSet", (short) 1);
        assertFalse(readFromResult.isEmpty());
        assertEquals("freenet.support.SimpleFieldSet=1\nAAAAAAAAAAAAAAAAAAAAAAAA\n", readFromResult.toString());
    }

    @Test
    public void testPut29() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("Key", true);
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut3() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("Key", 'A');
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut30() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.put("Key", true);
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPut31() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("Key", true);
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut32() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("freenet.support.SimpleFieldSet", true);
        assertFalse(readFromResult.isEmpty());
        assertEquals("freenet.support.SimpleFieldSet=true\nAAAAAAAAAAAAAAAAAAAAAAAA\n", readFromResult.toString());
    }

    @Test
    public void testPut33() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("freenet.support.SimpleFieldSet", true);
        assertFalse(readFromResult.isEmpty());
        assertEquals("freenet.support.SimpleFieldSet=true\nAAAAAAAAAAAAAAAAAAAAAAAA\n", readFromResult.toString());
    }

    @Test
    public void testPut34() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("Key",
                new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut35() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.put("Key",
                new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65});
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPut36() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("Key", new double[]{10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut37() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.put("Key", new double[]{10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0});
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPut38() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("Key", new double[]{10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut39() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("Key", new float[]{10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut4() {
        thrown.expect(IllegalArgumentException.class);
        (new SimpleFieldSet(true)).put("Key", '\n');
    }

    @Test
    public void testPut40() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.put("Key", new float[]{10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f});
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPut41() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("Key", new float[]{10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut42() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("Key", new int[]{1, 1, 1, 1, 1, 1, 1, 1});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut43() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.put("Key", new int[]{1, 1, 1, 1, 1, 1, 1, 1});
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPut44() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("Key", new int[]{1, 1, 1, 1, 1, 1, 1, 1});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut45() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("Key", new long[]{1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut46() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.put("Key", new long[]{1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L});
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPut47() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("Key", new long[]{1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut48() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("Key", new String[]{"foo", "foo", "foo"});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut49() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.put("Key", new String[]{"foo", "foo", "foo"});
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPut5() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("freenet.support.SimpleFieldSet", 'A');
        assertFalse(readFromResult.isEmpty());
        assertEquals("freenet.support.SimpleFieldSet=A\nAAAAAAAAAAAAAAAAAAAAAAAA\n", readFromResult.toString());
    }

    @Test
    public void testPut50() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("Key", new String[]{"foo", "foo", "foo"});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut51() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("Key", new short[]{1, 1, 1, 1, 1, 1, 1, 1});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut52() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.put("Key", new short[]{1, 1, 1, 1, 1, 1, 1, 1});
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPut53() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("Key", new short[]{1, 1, 1, 1, 1, 1, 1, 1});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut54() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("Key", new boolean[]{true, true, true, true, true, true, true, true});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut55() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.put("Key", new boolean[]{true, true, true, true, true, true, true, true});
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPut56() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("Key", new boolean[]{true, true, true, true, true, true, true, true});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut6() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("freenet.support.SimpleFieldSet", 'A');
        assertFalse(readFromResult.isEmpty());
        assertEquals("freenet.support.SimpleFieldSet=A\nAAAAAAAAAAAAAAAAAAAAAAAA\n", readFromResult.toString());
    }

    @Test
    public void testPut7() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.put("Key", 10.0);
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPut8() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.put("Key", 10.0);
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPut9() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.put("Key", 10.0);
        assertFalse(readFromResult.isEmpty());
    }

    /**
     * Tests put(String,boolean) and getBoolean(String,boolean)
     * methods consistency.
     * The default value (returned if the key is not found) is set to "false"
     * and the real value is always set to "true", so
     * we are sure if it finds the right value or not
     * (and does not use the default).
     */
    public void testPut_StringBoolean() {
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        int length = 15;
        for (int i = 0; i < length; i++)
            methodSFS.put(Integer.toString(i), true);
        for (int i = 0; i < length; i++)
            assertEquals(methodSFS.getBoolean(Integer.toString(i), false), true);
        assertTrue(checkSimpleFieldSetSize(methodSFS, length));
    }


    /**
     * Checks if the provided SimpleFieldSet
     * has the right size
     *
     * @param aSimpleFieldSet
     * @param expectedSize
     * @return true if the size is the expected
     */
    private boolean checkSimpleFieldSetSize(SimpleFieldSet aSimpleFieldSet, int expectedSize) {
        int actualSize = 0;
        Iterator<String> methodKeyIterator = aSimpleFieldSet.keyIterator();
        while (methodKeyIterator.hasNext()) {
            methodKeyIterator.next();
            actualSize++;
        }
        return expectedSize == actualSize;
    }

    /**
     * Tests put(String,int) and
     * [getInt(String),getInt(String,int)]
     * methods consistency.
     * The default value (returned if the key is not found)
     * is set to a not present int value, so we are sure
     * if it finds the right value or not
     * (and does not use the default).
     */
    public void testPut_StringInt() {
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        int[][] methodPairsArray =
                {{1, 1}, {2, 2}, {3, 3}, {4, 4}};
        for (int i = 0; i < methodPairsArray.length; i++)
            methodSFS.put(Integer.toString(methodPairsArray[i][0]), methodPairsArray[i][1]);

        assertTrue(checkSimpleFieldSetSize(methodSFS, methodPairsArray.length));

        for (int i = 0; i < methodPairsArray.length; i++) {
            try {
                assertEquals(methodSFS.getInt(Integer.toString(methodPairsArray[i][0])),
                        methodPairsArray[i][1]);
                assertEquals(methodSFS.getInt(Integer.toString(methodPairsArray[i][0]), 5),
                        methodPairsArray[i][1]);
            } catch (FSParseException aException) {
                fail("Not expected exception thrown : " + aException.getMessage());
            }
        }
    }

    /**
     * Tests put(String,long) and
     * [getLong(String),getLong(String,long)]
     * methods consistency.
     * The default value (returned if the key is not found)
     * is set to a not present long value, so we are sure
     * if it finds the right value or not
     * (and does not use the default).
     */
    public void testPut_StringLong() {
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        long[][] methodPairsArray =
                {{1, 1}, {2, 2}, {3, 3}, {4, 4}};
        for (int i = 0; i < methodPairsArray.length; i++)
            methodSFS.put(Long.toString(methodPairsArray[i][0]), methodPairsArray[i][1]);

        assertTrue(checkSimpleFieldSetSize(methodSFS, methodPairsArray.length));

        for (int i = 0; i < methodPairsArray.length; i++) {
            try {
                assertEquals(methodSFS.getLong(Long.toString(methodPairsArray[i][0])),
                        methodPairsArray[i][1]);
                assertEquals(methodSFS.getLong(Long.toString(methodPairsArray[i][0]), 5),
                        methodPairsArray[i][1]);
            } catch (FSParseException aException) {
                fail("Not expected exception thrown : " + aException.getMessage());
            }
        }
    }

    /**
     * Tests put(String,char) and
     * [getChar(String),getChar(String,char)]
     * methods consistency.
     * The default value (returned if the key is not found)
     * is set to a not present char value, so we are sure
     * if it finds the right value or not
     * (and does not use the default).
     */
    public void testPut_StringChar() {
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        char[][] methodPairsArray =
                {{'1', '1'}, {'2', '2'}, {'3', '3'}, {'4', '4'}};
        for (int i = 0; i < methodPairsArray.length; i++)
            methodSFS.put(String.valueOf(methodPairsArray[i][0]), methodPairsArray[i][1]);

        assertTrue(checkSimpleFieldSetSize(methodSFS, methodPairsArray.length));

        for (int i = 0; i < methodPairsArray.length; i++) {
            try {
                assertEquals(methodSFS.getChar(String.valueOf(methodPairsArray[i][0])),
                        methodPairsArray[i][1]);
                assertEquals(methodSFS.getChar(String.valueOf(methodPairsArray[i][0]), '5'),
                        methodPairsArray[i][1]);
            } catch (FSParseException aException) {
                fail("Not expected exception thrown : " + aException.getMessage());
            }
        }
    }

    /**
     * Tests put(String,short) and
     * [getShort(String)|getShort(String,short)]
     * methods consistency.
     * The default value (returned if the key is not found)
     * is set to a not present short value, so we are sure
     * if it finds the right value or not
     * (and does not use the default).
     */
    public void testPut_StringShort() {
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        short[][] methodPairsArray =
                {{1, 1}, {2, 2}, {3, 3}, {4, 4}};
        for (int i = 0; i < methodPairsArray.length; i++)
            methodSFS.put(Short.toString(methodPairsArray[i][0]), methodPairsArray[i][1]);

        assertTrue(checkSimpleFieldSetSize(methodSFS, methodPairsArray.length));

        for (int i = 0; i < methodPairsArray.length; i++) {
            try {
                assertEquals(methodSFS.getShort(Short.toString(methodPairsArray[i][0])),
                        methodPairsArray[i][1]);
                assertEquals(methodSFS.getShort(Short.toString(methodPairsArray[i][0]), (short) 5),
                        methodPairsArray[i][1]);
            } catch (FSParseException aException) {
                fail("Not expected exception thrown : " + aException.getMessage());
            }
        }
    }

    /**
     * Tests put(String,double) and
     * [getDouble(String)|getDouble(String,double)]
     * methods consistency.
     * The default value (returned if the key is not found)
     * is set to a not present double value, so we are sure
     * if it finds the right value or not
     * (and does not use the default).
     */
    public void testPut_StringDouble() {
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        double[][] methodPairsArray =
                {{1, 1}, {2, 2}, {3, 3}, {4, 4}};
        for (int i = 0; i < methodPairsArray.length; i++)
            methodSFS.put(Double.toString(methodPairsArray[i][0]), methodPairsArray[i][1]);

        assertTrue(checkSimpleFieldSetSize(methodSFS, methodPairsArray.length));

        for (int i = 0; i < methodPairsArray.length; i++) {
            try {
                //there is no assertEquals(Double,Double) so we are obliged to do this way -_-
                assertEquals(
                        Double.compare((methodSFS.getDouble(Double.toString(methodPairsArray[i][0]))),
                                methodPairsArray[i][1]), 0);
                assertEquals(
                        Double.compare(methodSFS.getDouble(Double.toString(methodPairsArray[i][0]), 5),
                                methodPairsArray[i][1]), 0);
            } catch (FSParseException aException) {
                fail("Not expected exception thrown : " + aException.getMessage());
            }
        }
    }

    @Test
    public void testWriteTo() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.writeTo(new ByteArrayOutputStream());
    }

    @Test
    public void testWriteTo10() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.writeTo(new StringWriter(), "Prefix", false, true);
    }

    @Test
    public void testWriteTo2() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.writeTo(new ByteArrayOutputStream());
    }

    @Test
    public void testWriteTo3() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.writeTo(new ByteArrayOutputStream(), 3);
    }

    @Test
    public void testWriteTo4() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.writeTo(new ByteArrayOutputStream(), 3);
    }

    @Test
    public void testWriteTo5() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.writeTo(new ByteArrayOutputStream(), 0);
    }

    @Test
    public void testWriteTo6() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.writeTo(new StringWriter());
    }

    @Test
    public void testWriteTo7() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.writeTo(new StringWriter());
    }

    @Test
    public void testWriteTo8() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.writeTo(new StringWriter(), "Prefix", true, true);
    }

    @Test
    public void testWriteTo9() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.writeTo(new StringWriter(), "Prefix", false, true);
    }

    @Test
    public void testWriteToOrdered() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.writeToOrdered(new StringWriter());
    }

    @Test
    public void testWriteToOrdered2() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.writeToOrdered(new StringWriter());
    }

    @Test
    public void testWriteToBigBuffer() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.writeToBigBuffer(new ByteArrayOutputStream());
    }

    @Test
    public void testWriteToBigBuffer2() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.writeToBigBuffer(new ByteArrayOutputStream());
    }

    @Test
    public void testToString() throws IOException {
        assertEquals("AAAAAAAAAAAAAAAAAAAAAAAA\n", SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).toString());
        assertEquals("End\n", (new SimpleFieldSet(true)).toString());
    }

    @Test
    public void testToString2() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.setHeader("Headers");
        assertEquals("# Headers\nAAAAAAAAAAAAAAAAAAAAAAAA\n", readFromResult.toString());
    }

    @Test
    public void testToOrderedString() throws IOException {
        assertEquals("AAAAAAAAAAAAAAAAAAAAAAAA\n", SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).toOrderedString());
        assertEquals("End\n", (new SimpleFieldSet(true)).toOrderedString());
    }

    @Test
    public void testToOrderedString2() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.setHeader("Headers");
        assertEquals("# Headers\nAAAAAAAAAAAAAAAAAAAAAAAA\n", readFromResult.toOrderedString());
    }

    @Test
    public void testToOrderedStringWithBase64() throws IOException {
        assertEquals("AAAAAAAAAAAAAAAAAAAAAAAA\n", SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).toOrderedStringWithBase64());
        assertEquals("End\n", (new SimpleFieldSet(true)).toOrderedStringWithBase64());
    }

    @Test
    public void testSetEndMarker() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.setEndMarker("S");
        assertEquals("S", readFromResult.getEndMarker());
    }

    /**
     * Generates a string for the SFS parser in the canonical form:
     * key=value
     * END
     *
     * @param aStringPairsArray
     * @return a String ready to be read by a SFS parser
     */
    private String sfsReadyString(String[][] aStringPairsArray) {

        StringBuilder methodStringToReturn = new StringBuilder();
        for (int i = 0; i < aStringPairsArray.length; i++)
            methodStringToReturn.append(aStringPairsArray[i][0]).append(KEY_VALUE_SEPARATOR).append(aStringPairsArray[i][1]).append('\n');
        methodStringToReturn.append(SAMPLE_END_MARKER);
        return methodStringToReturn.toString();
    }

    /**
     * Tests SimpleFieldSet(String,boolean,boolean) constructor,
     * with simple and border cases of the canonical form.
     */
    public void testSimpleFieldSet_StringBooleanBoolean() {
        String[][] methodStringPairs = SAMPLE_STRING_PAIRS;
        String methodStringToParse = sfsReadyString(methodStringPairs);
        try {
            SimpleFieldSet methodSFS = new SimpleFieldSet(methodStringToParse, false, false, false);
            for (int i = 0; i < methodStringPairs.length; i++)
                assertEquals(methodSFS.get(methodStringPairs[i][0]),
                        methodStringPairs[i][1]);
        } catch (IOException aException) {
            fail("Not expected exception thrown : " + aException.getMessage());
        }
    }

    /**
     * Tests SimpleFieldSet(BufferedReader,boolean,boolean) constructor,
     * with simple and border cases of the canonical form.
     */
    public void testSimpleFieldSet_BufferedReaderBooleanBoolean() {
        String[][] methodStringPairs = SAMPLE_STRING_PAIRS;
        BufferedReader methodBufferedReader =
                new BufferedReader(new StringReader(sfsReadyString(methodStringPairs)));
        try {
            SimpleFieldSet methodSFS = new SimpleFieldSet(methodBufferedReader, false, false);
            for (int i = 0; i < methodStringPairs.length; i++)
                assertEquals(methodSFS.get(methodStringPairs[i][0]),
                        methodStringPairs[i][1]);
        } catch (IOException aException) {
            fail("Not expected exception thrown : " + aException.getMessage());
        }
    }

    /**
     * Generates a SimpleFieldSet using the
     * SAMPLE_STRING_PAIRS and sfs put method
     *
     * @return a SimpleFieldSet
     */
    private SimpleFieldSet sfsFromSampleStringPairs() {
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        for (int i = 0; i < SAMPLE_STRING_PAIRS.length; i++)
            methodSFS.putSingle(SAMPLE_STRING_PAIRS[i][0],
                    SAMPLE_STRING_PAIRS[i][1]);
        assertTrue(checkSimpleFieldSetSize(methodSFS,
                SAMPLE_STRING_PAIRS.length));
        return methodSFS;
    }

    /**
     * Tests SimpleFieldSet(SimpleFieldSet) constructor,
     * with simple and border cases of the canonical form.
     */
    public void testSimpleFieldSet_SimpleFieldSet() {
        SimpleFieldSet methodSFS = new SimpleFieldSet(sfsFromSampleStringPairs());
        String[][] methodStringPairs = SAMPLE_STRING_PAIRS;
        for (int i = 0; i < methodStringPairs.length; i++)
            assertEquals(methodSFS.get(methodStringPairs[i][0]),
                    methodStringPairs[i][1]);
    }

    /**
     * Tests {get,set}EndMarker(String) methods
     * using them after a String parsing
     */
    public void testEndMarker() {
        String methodEndMarker = "ANOTHER-ENDING";
        String methodStringToParse = sfsReadyString(SAMPLE_STRING_PAIRS);
        try {
            SimpleFieldSet methodSFS = new SimpleFieldSet(methodStringToParse, false, false, false);
            assertEquals(methodSFS.getEndMarker(), SAMPLE_END_MARKER);
            methodSFS.setEndMarker(methodEndMarker);
            assertEquals(methodSFS.getEndMarker(), methodEndMarker);
        } catch (IOException aException) {
            fail("Not expected exception thrown : " + aException.getMessage());
        }
    }

    /**
     * Tests isEmpty() method.
     */
    public void testIsEmpty() {
        SimpleFieldSet methodSFS = sfsFromSampleStringPairs();
        assertFalse(methodSFS.isEmpty());
        methodSFS = new SimpleFieldSet(true);
        assertTrue(methodSFS.isEmpty());
    }

    @Test
    public void testIsEmpty2() throws IOException {
        assertTrue(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).isEmpty());
    }

    /**
     * Tests directSubsetNameIterator() method.
     * It uses SAMPLE_STRING_PAIRS and for this reason
     * the expected subset is "foo".
     */
    public void testDirectSubsetNameIterator() {
        SimpleFieldSet methodSFS = sfsFromSampleStringPairs();
        String expectedSubset = SAMPLE_STRING_PAIRS[0][0];    //"foo"
        Iterator<String> methodIter = methodSFS.directSubsetNameIterator();
        while (methodIter.hasNext())
            assertEquals(methodIter.next(), expectedSubset);
        methodSFS = new SimpleFieldSet(true);
        methodIter = methodSFS.directSubsetNameIterator();
        assertNull(methodIter);
    }

    @Test
    public void testDirectSubsetNameIterator2() throws IOException {
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).directSubsetNameIterator());
    }

    /**
     * Tests nameOfDirectSubsets() method.
     */
    public void testNamesOfDirectSubsets() {
        String[] expectedResult = {SAMPLE_STRING_PAIRS[0][0]};
        SimpleFieldSet methodSFS = sfsFromSampleStringPairs();
        assertTrue(Arrays.equals(methodSFS.namesOfDirectSubsets(), expectedResult));

        methodSFS = new SimpleFieldSet(true);
        assertTrue(Arrays.equals(methodSFS.namesOfDirectSubsets(), new String[0]));
    }

    @Test
    public void testNamesOfDirectSubsets2() throws IOException {
        assertEquals(0, SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).namesOfDirectSubsets().length);
    }

    @Test
    public void testReadFrom() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet.readFrom(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile(), true, true);
    }

    @Test
    public void testReadFrom10() throws IOException {
        SimpleFieldSet actualReadFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{61, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                false, true);
        assertFalse(actualReadFromResult.isEmpty());
        assertEquals("=AAAAAAAAAAAAAAAAAAAAAAA\nEnd\n", actualReadFromResult.toString());
    }

    @Test
    public void testReadFrom11() throws IOException {
        SimpleFieldSet actualReadFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{61, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false);
        assertFalse(actualReadFromResult.isEmpty());
        assertEquals("=AAAAAAAAAAAAAAAAAAAAAAA\nEnd\n", actualReadFromResult.toString());
    }

    @Test
    public void testReadFrom12() throws IOException {
        SimpleFieldSet actualReadFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        assertTrue(actualReadFromResult.isEmpty());
        assertEquals("AAAAAAAAAAAAAAAAAAAAAAAA", actualReadFromResult.getEndMarker());
    }

    @Test
    public void testReadFrom13() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet.readFrom(null, true, true, true, true);
    }

    @Test
    public void testReadFrom14() throws IOException {
        assertTrue(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{35, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).isEmpty());
    }

    @Test
    public void testReadFrom15() throws IOException {
        SimpleFieldSet actualReadFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{61, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        assertFalse(actualReadFromResult.isEmpty());
        assertEquals("=AAAAAAAAAAAAAAAAAAAAAAA\nEnd\n", actualReadFromResult.toString());
    }

    @Test
    public void testReadFrom16() throws IOException {
        SimpleFieldSet actualReadFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 61}),
                true, true, true, true);
        assertFalse(actualReadFromResult.isEmpty());
        assertEquals("AAAAAAAAAAAAAAAAAAAAAAA=\nEnd\n", actualReadFromResult.toString());
    }

    @Test
    public void testReadFrom17() throws IOException {
        thrown.expect(EOFException.class);
        SimpleFieldSet.readFrom(new ByteArrayInputStream(new byte[]{}), true, true, true, true);
    }

    @Test
    public void testReadFrom18() throws IOException {
        SimpleFieldSet actualReadFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{61, 61, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        assertFalse(actualReadFromResult.isEmpty());
        assertEquals("==AAAAAAAAAAAAAAAAAAAAAA\nEnd\n", actualReadFromResult.toString());
    }

    @Test
    public void testReadFrom19() throws IOException {
        SimpleFieldSet actualReadFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{61, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                false, true, true, true);
        assertFalse(actualReadFromResult.isEmpty());
        assertEquals("=AAAAAAAAAAAAAAAAAAAAAAA\nEnd\n", actualReadFromResult.toString());
    }

    @Test
    public void testReadFrom2() throws IOException {
        SimpleFieldSet actualReadFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true);
        assertTrue(actualReadFromResult.isEmpty());
        assertEquals("AAAAAAAAAAAAAAAAAAAAAAAA", actualReadFromResult.getEndMarker());
    }

    @Test
    public void testReadFrom20() throws IOException {
        SimpleFieldSet actualReadFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{61, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        assertFalse(actualReadFromResult.isEmpty());
        assertEquals("=AAAAAAAAAAAAAAAAAAAAAAA\nEnd\n", actualReadFromResult.toString());
    }

    @Test
    public void testReadFrom21() throws IOException {
        SimpleFieldSet actualReadFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{61, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, false);
        assertFalse(actualReadFromResult.isEmpty());
        assertEquals("=AAAAAAAAAAAAAAAAAAAAAAA\nEnd\n", actualReadFromResult.toString());
    }

    @Test
    public void testReadFrom3() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet.readFrom((InputStream) null, true, true);
    }

    @Test
    public void testReadFrom4() throws IOException {
        assertTrue(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{35, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true).isEmpty());
    }

    @Test
    public void testReadFrom5() throws IOException {
        SimpleFieldSet actualReadFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{61, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true);
        assertFalse(actualReadFromResult.isEmpty());
        assertEquals("=AAAAAAAAAAAAAAAAAAAAAAA\nEnd\n", actualReadFromResult.toString());
    }

    @Test
    public void testReadFrom6() throws IOException {
        SimpleFieldSet actualReadFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 61}),
                true, true);
        assertFalse(actualReadFromResult.isEmpty());
        assertEquals("AAAAAAAAAAAAAAAAAAAAAAA=\nEnd\n", actualReadFromResult.toString());
    }

    @Test
    public void testReadFrom7() throws IOException {
        thrown.expect(EOFException.class);
        SimpleFieldSet.readFrom(new ByteArrayInputStream(new byte[]{}), true, true);
    }

    @Test
    public void testReadFrom8() throws IOException {
        SimpleFieldSet actualReadFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{61, 61, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true);
        assertFalse(actualReadFromResult.isEmpty());
        assertEquals("==AAAAAAAAAAAAAAAAAAAAAA\nEnd\n", actualReadFromResult.toString());
    }

    @Test
    public void testReadFrom9() throws IOException {
        SimpleFieldSet actualReadFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{61, 10, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true);
        assertFalse(actualReadFromResult.isEmpty());
        assertEquals("AAAAAAAAAAAAAAAAAAAAAA", actualReadFromResult.getEndMarker());
        assertEquals("=\nAAAAAAAAAAAAAAAAAAAAAA\n", actualReadFromResult.toString());
    }

    @Test
    public void testGetInt() throws IOException {
        assertEquals(1, SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getInt("Key", 1));
        assertEquals(1, SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getInt("freenet.support.SimpleFieldSet", 1));
    }

    @Test
    public void testGetDouble() throws IOException {
        assertEquals(10.0, SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getDouble("Key", 10.0), 0.0);
        assertEquals(10.0, SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getDouble("freenet.support.SimpleFieldSet", 10.0), 0.0);
    }

    @Test
    public void testGetLong() throws IOException {
        assertEquals(1L, SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getLong("Key", 1L));
        assertEquals(1L, SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getLong("freenet.support.SimpleFieldSet", 1L));
    }

    @Test
    public void testGetShort() throws IOException {
        assertEquals((short) 1, SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getShort("Key", (short) 1));
        assertEquals((short) 1, SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getShort("freenet.support.SimpleFieldSet", (short) 1));
    }

    @Test
    public void testGetByte() throws IOException {
        assertEquals((byte) 65, SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getByte("Key", (byte) 65));
        assertEquals((byte) 65, SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getByte("freenet.support.SimpleFieldSet", (byte) 65));
    }

    @Test
    public void testGetChar() throws IOException {
        assertEquals('A', SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getChar("Key", 'A'));
        assertEquals('A', SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getChar("freenet.support.SimpleFieldSet", 'A'));
    }

    @Test
    public void testGetBoolean() throws IOException {
        assertTrue(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getBoolean("Key", true));
        assertFalse(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getBoolean("Key", false));
        assertTrue(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getBoolean("freenet.support.SimpleFieldSet", true));
    }

    @Test
    public void testPutOverwrite() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.putOverwrite("Key", "value");
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPutOverwrite2() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.putOverwrite("Key", "value");
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPutOverwrite3() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.putOverwrite("Key", "value");
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPutOverwrite4() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).putOverwrite("Key", (String) null);
    }

    @Test
    public void testPutOverwrite5() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.putOverwrite("Key", new String[]{"foo", "foo", "foo"});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPutOverwrite6() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.putOverwrite("Key", new String[]{"foo", "foo", "foo"});
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPutOverwrite7() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.putOverwrite("Key", new String[]{"foo", "foo", "foo"});
        assertFalse(readFromResult.isEmpty());
    }

    /**
     * Test the putOverwrite(String,String) method.
     */
    public void testPutOverwrite_String() {
        String methodKey = "foo.bar";
        String[] methodValues = {"boo", "bar", "zoo"};
        String expectedResult = "zoo";
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        for (int i = 0; i < methodValues.length; i++)
            methodSFS.putOverwrite(methodKey, methodValues[i]);
        assertEquals(methodSFS.get(methodKey), expectedResult);
    }

    /**
     * Test the putOverwrite(String,String[]) method.
     */
    public void testPutOverwrite_StringArray() {
        String methodKey = "foo.bar";
        String[] methodValues = {"boo", "bar", "zoo"};
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        methodSFS.putOverwrite(methodKey, methodValues);
        assertTrue(Arrays.equals(methodSFS.getAll(methodKey), methodValues));
    }

    @Test
    public void testPutEncoded() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.putEncoded("Key", new String[]{"foo", "foo", "foo"});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPutEncoded2() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.putEncoded("Key", new String[]{"foo", "foo", "foo"});
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPutEncoded3() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.putEncoded("Key", new String[]{"foo", "foo", "foo"});
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testSetHeader() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.setHeader("foo", "foo", "foo");
        assertEquals("# foo\n# foo\n# foo\nAAAAAAAAAAAAAAAAAAAAAAAA\n", readFromResult.toString());
    }

    /**
     * Test the putAppend(String,String) method.
     */
    public void testPutAppend() {
        String methodKey = "foo.bar";
        String[] methodValues = {"boo", "bar", "zoo"};
        String expectedResult = "boo" + SimpleFieldSet.MULTI_VALUE_CHAR
                + "bar" + SimpleFieldSet.MULTI_VALUE_CHAR
                + "zoo";
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        for (int i = 0; i < methodValues.length; i++)
            methodSFS.putAppend(methodKey, methodValues[i]);
        assertEquals(methodSFS.get(methodKey), expectedResult);
    }

    @Test
    public void testPutAppend2() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.putAppend("Key", "value");
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPutAppend3() {
        SimpleFieldSet simpleFieldSet = new SimpleFieldSet(true);
        simpleFieldSet.putAppend("Key", "value");
        assertFalse(simpleFieldSet.isEmpty());
    }

    @Test
    public void testPutAppend4() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, false, true, true);
        readFromResult.putAppend("Key", "value");
        assertFalse(readFromResult.isEmpty());
    }

    @Test
    public void testPutAppend5() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).putAppend("Key", null);
    }

    /**
     * Tests the getAll(String) method.
     */
    public void testGetAll() {
        String methodKey = "foo.bar";
        String[] methodValues = {"boo", "bar", "zoo"};
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        for (int i = 0; i < methodValues.length; i++)
            methodSFS.putAppend(methodKey, methodValues[i]);
        assertTrue(Arrays.equals(methodSFS.getAll(methodKey), methodValues));
    }

    @Test
    public void testGetAll2() throws IOException {
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getAll("Key"));
    }

    @Test
    public void testGetAll3() throws IOException {
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getAll("freenet.support.SimpleFieldSet"));
    }

    @Test
    public void testGetAllEncoded() throws IllegalBase64Exception, IOException {
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getAllEncoded("Key"));
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getAllEncoded("freenet.support.SimpleFieldSet"));
    }

    /**
     * Tests the getIntArray(String) method
     */
    public void testGetIntArray() {
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        String keyPrefix = "foo";
        for (int i = 0; i < 15; i++)
            methodSFS.putAppend(keyPrefix, String.valueOf(i));
        int[] result = methodSFS.getIntArray(keyPrefix);
        for (int i = 0; i < 15; i++)
            assertTrue(result[i] == i);

    }

    @Test
    public void testGetIntArray2() throws IOException {
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getIntArray("Key"));
    }

    @Test
    public void testGetIntArray3() throws IOException {
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getIntArray("freenet.support.SimpleFieldSet"));
    }

    @Test
    public void testGetShortArray() throws IOException {
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getShortArray("Key"));
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getShortArray("freenet.support.SimpleFieldSet"));
    }

    @Test
    public void testGetLongArray() throws IOException {
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getLongArray("Key"));
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getLongArray("freenet.support.SimpleFieldSet"));
    }

    /**
     * Tests the getDoubleArray(String) method
     */
    public void testGetDoubleArray() {
        SimpleFieldSet methodSFS = new SimpleFieldSet(true);
        String keyPrefix = "foo";
        for (int i = 0; i < 15; i++)
            methodSFS.putAppend(keyPrefix, String.valueOf((double) i));
        double[] result = methodSFS.getDoubleArray(keyPrefix);
        for (int i = 0; i < 15; i++)
            assertTrue(result[i] == (i));

    }

    @Test
    public void testGetDoubleArray2() throws IOException {
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getDoubleArray("Key"));
    }

    @Test
    public void testGetDoubleArray3() throws IOException {
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getDoubleArray("freenet.support.SimpleFieldSet"));
    }

    @Test
    public void testGetFloatArray() throws IOException {
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getFloatArray("Key"));
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getFloatArray("freenet.support.SimpleFieldSet"));
    }

    @Test
    public void testGetBooleanArray() throws IOException {
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getBooleanArray("Key"));
        assertNull(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).getBooleanArray("freenet.support.SimpleFieldSet"));
    }

    /**
     * Tests removeValue(String) method
     */
    public void testRemoveValue() {
        SimpleFieldSet methodSFS = sfsFromSampleStringPairs();
        methodSFS.removeValue("foo");
        assertNull(methodSFS.get(SAMPLE_STRING_PAIRS[0][0]));
        for (int i = 1; i < SAMPLE_STRING_PAIRS.length; i++)
            assertEquals(methodSFS.get(SAMPLE_STRING_PAIRS[i][0]),
                    SAMPLE_STRING_PAIRS[i][1]);
    }

    @Test
    public void testRemoveValue2() throws IOException {
        SimpleFieldSet readFromResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true);
        readFromResult.removeValue("Key");
        assertTrue(readFromResult.isEmpty());
    }

    @Test
    public void testRemoveValue3() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).removeValue("freenet.support.SimpleFieldSet");
    }

    /**
     * Tests removeSubset(String) method
     */
    public void testRemoveSubset() {
        SimpleFieldSet methodSFS = sfsFromSampleStringPairs();
        methodSFS.removeSubset("foo");
        for (int i = 1; i < 4; i++)
            assertNull(methodSFS.get(SAMPLE_STRING_PAIRS[i][0]));
        assertEquals(methodSFS.get(SAMPLE_STRING_PAIRS[0][0]),
                SAMPLE_STRING_PAIRS[0][1]);
        for (int i = 4; i < 6; i++)
            assertEquals(methodSFS.get(SAMPLE_STRING_PAIRS[i][0]),
                    SAMPLE_STRING_PAIRS[i][1]);
    }

    @Test
    public void testRemoveSubset2() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).removeSubset("Key");
    }

    /**
     * Searches for a key in a given String[][] array.
     * We consider that keys are stored in String[x][0]
     *
     * @param aStringPairsArray
     * @param aPrefix           that could be put before found key
     * @param aKey              to be searched
     * @return true if there is the key
     */
    private boolean isAKey(String[][] aStringPairsArray, String aPrefix, String aKey) {
        for (int i = 0; i < aStringPairsArray.length; i++)
            if (aKey.equals(aPrefix + aStringPairsArray[i][0]))
                return true;
        return false;
    }

    /**
     * Verifies if all keys in a String[][]
     * (We consider that keys are stored in String[x][0])
     * are the same that the Iterator provides.
     * In this way both hasNext() and next() methods
     * are tested.
     *
     * @param aStringPairsArray
     * @param aPrefix           that could be put before found key
     * @param aIterator
     * @return true if they have the same key set
     */
    private boolean areAllContainedKeys(String[][] aStringPairsArray, String aPrefix, Iterator<String> aIterator) {
        boolean retValue = true;
        int actualLength = 0;
        while (aIterator.hasNext()) {
            actualLength++;
            retValue &= isAKey(aStringPairsArray, aPrefix, aIterator.next());
        }
        retValue &= (actualLength == aStringPairsArray.length);
        return retValue;
    }

    /**
     * Tests the Iterator given for the
     * SimpleFieldSet class.
     * It tests hasNext() and next() methods.
     */
    public void testKeyIterator() {
        SimpleFieldSet methodSFS = sfsFromSampleStringPairs();
        Iterator<String> itr = methodSFS.keyIterator();
        assertTrue(areAllContainedKeys(SAMPLE_STRING_PAIRS, "", itr));
    }

    @Test
    public void testKeyIterator2() throws IOException {
        assertEquals("", ((SimpleFieldSet.KeyIterator) SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).keyIterator()).prefix);
        assertNull(((SimpleFieldSet.KeyIterator) SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).keyIterator()).subsetIterator);
    }

    @Test
    public void testKeyIterator3() throws IOException {
        SimpleFieldSet.KeyIterator actualKeyIteratorResult = SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).keyIterator("Prefix");
        assertEquals("Prefix", actualKeyIteratorResult.prefix);
        assertNull(actualKeyIteratorResult.subsetIterator);
    }

    /**
     * Tests the Iterator created using prefix
     * given for the SimpleFieldSet class
     */
    public void testKeyIterator_String() {
        String methodPrefix = "bob";
        SimpleFieldSet methodSFS = sfsFromSampleStringPairs();
        Iterator<String> itr = methodSFS.keyIterator(methodPrefix);
        assertTrue(areAllContainedKeys(SAMPLE_STRING_PAIRS, methodPrefix, itr));
    }

    /**
     * Tests the toplevelIterator given for the
     * SimpleFieldSet class.
     * It tests hasNext() and next() methods.
     * <p>
     * TODO: improve the test
     */
    public void testToplevelKeyIterator() {
        SimpleFieldSet methodSFS = sfsFromSampleStringPairs();
        Iterator<String> itr = methodSFS.toplevelKeyIterator();

        for (int i = 0; i < 3; i++) {
            assertTrue(itr.hasNext());
            assertTrue(isAKey(SAMPLE_STRING_PAIRS, "", (String) itr.next()));
        }
        assertFalse(itr.hasNext());
    }

    @Test
    public void testToplevelKeyIterator2() throws IOException {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).toplevelKeyIterator();
    }

    @Test
    public void testDirectKeyValues() throws IOException {
        assertTrue(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).directKeyValues().isEmpty());
    }

    @Test
    public void testDirectKeys() throws IOException {
        assertTrue(SimpleFieldSet.readFrom(
                new ByteArrayInputStream(
                        new byte[]{65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65}),
                true, true, true, true).directKeys().isEmpty());
    }

    public void testKeyIterationPastEnd() {
        System.out.println("Starting iterator test");

        SimpleFieldSet sfs = new SimpleFieldSet(true);
        sfs.putOverwrite("test", "test");

        Iterator<String> keyIterator = sfs.keyIterator();
        assertEquals("test", keyIterator.next());

        try {
            String s = keyIterator.next();
            fail("Expected NoSuchElementException, but got " + s);
        } catch (NoSuchElementException e) {
            //Expected
        }
    }

    public void testBase64() throws IOException {
        checkBase64("test", " ", "IA");
        for (String[] s : SAMPLE_STRING_PAIRS) {
            String evilValue = "=" + s[1];
            String base64 = Base64.encodeUTF8(evilValue);
            checkBase64(s[0], evilValue, base64);
        }
    }

    private void checkBase64(String key, String value, String base64Value) throws IOException {
        SimpleFieldSet sfs = new SimpleFieldSet(true);
        sfs.putSingle(key, value);
        assertEquals(sfs.toOrderedString(), key + "=" + value + "\nEnd\n");
        StringWriter sw = new StringWriter();
        sfs.writeTo(sw, "", false, true);
        String written = sw.toString();
        assertEquals(written, key + "==" + base64Value + "\nEnd\n");
        LineReader r = Readers.fromBufferedReader(new BufferedReader(new StringReader(written)));
        SimpleFieldSet sfsCheck = new SimpleFieldSet(r, 1024, 1024, true, false, true, true);
        assertEquals(sfsCheck.get(key), value);
    }

    public void testEmptyValue() throws IOException {
        String written = "foo.blah=\nEnd\n";
        LineReader r = Readers.fromBufferedReader(new BufferedReader(new StringReader(written)));
        SimpleFieldSet sfsCheck = new SimpleFieldSet(r, 1024, 1024, true, false, true, false);
        assertTrue(sfsCheck.get("foo.blah").equals(""));
        r = Readers.fromBufferedReader(new BufferedReader(new StringReader(written)));
        sfsCheck = new SimpleFieldSet(r, 1024, 1024, true, false, true, true);
        assertTrue(sfsCheck.get("foo.blah").equals(""));
    }

    public void testSplit() {
        assertTrue(Arrays.equals(SimpleFieldSet.split("blah"), new String[]{"blah"}));
        assertTrue(Arrays.equals(SimpleFieldSet.split("blah; blah"), new String[]{"blah", " blah"}));
        assertTrue(Arrays.equals(SimpleFieldSet.split("blah;1;2"), new String[]{"blah", "1", "2"}));
        assertTrue(Arrays.equals(SimpleFieldSet.split("blah;1;2;"), new String[]{"blah", "1", "2", ""}));
        assertTrue(Arrays.equals(SimpleFieldSet.split("blah;1;2;;"), new String[]{"blah", "1", "2", "", ""}));
        assertTrue(Arrays.equals(SimpleFieldSet.split(";blah;1;2;;"), new String[]{"", "blah", "1", "2", "", ""}));
        assertTrue(Arrays.equals(SimpleFieldSet.split(";;blah;1;2;;"), new String[]{"", "", "blah", "1", "2", "", ""}));
        assertTrue(Arrays.equals(SimpleFieldSet.split(";;;"), new String[]{"", "", ""}));
        assertEquals(1, SimpleFieldSet.split("String").length);
        assertEquals(0, SimpleFieldSet.split(null).length);
        assertEquals(0, SimpleFieldSet.split("").length);
    }
}
