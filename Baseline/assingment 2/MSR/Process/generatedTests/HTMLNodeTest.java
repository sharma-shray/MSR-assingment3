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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import freenet.clients.http.complexhtmlnodes.PeerTrustInputForAddPeerBoxNode;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test case for {@link freenet.support.HTMLNode} class.
 *
 * @author Alberto Bacchelli &lt;sback@freenetproject.org&gt;
 */
public class HTMLNodeTest extends TestCase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private HTMLNode exampleNodeNonEmpty;
    private HTMLNode exampleNodeEmpty;

    /**
     * Example node name in ASCII only. Not permitted to be empty.
     */
    private static final String SAMPLE_OKAY_NODE_NAME_NON_EMPTY = "sampleNode";

    /**
     * Example node name in ASCII only. Not permitted to be empty. It must be on the
     * EmptyTag list, so we use a real tag name
     */
    private static final String SAMPLE_OKAY_NODE_NAME_EMPTY = "area";

    /**
     * Example node name that includes an invalid char.
     */
    private static final String SAMPLE_WRONG_NODE_NAME = "s\u03a2mpleNode";

    /* example node attribute in ASCII only. */
    private static final String SAMPLE_OKAY_ATTRIBUTE_NAME = "sampleAttributeName";

    /**
     * Example attribute name that includes an invalid char.
     */
    private static final String SAMPLE_WRONG_ATTRIBUTE_NAME = "s\u03a2mpleAttributeName";

    //example node attribute value that includes a not ASCII char [Greek epsilon]
    private static final String SAMPLE_ATTRIBUTE_VALUE = "sampleAttribut\u03b5Value";

    //example node content that includes a not ASCII char [Greek omicron]
    private static final String SAMPLE_NODE_CONTENT = "sampleNodeC\u03bfntent";

    @Test
    public void testHTMLDoctypeConstructor() {
        HTMLNode.HTMLDoctype actualHtmlDoctype = new HTMLNode.HTMLDoctype("Doctype", "System Uri");
        assertNull(actualHtmlDoctype.getContent());
        assertEquals("doctype", actualHtmlDoctype.name);
        List<HTMLNode> expectedChildren = actualHtmlDoctype.children;
        assertSame(expectedChildren, actualHtmlDoctype.getChildren());
    }

    @Test
    public void testSetReadOnly() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        HTMLNode.text(3).setReadOnly();
    }

    @Test
    public void testConstructor() {
        HTMLNode actualHtmlNode = new HTMLNode(HTMLNode.text(3), true);
        assertEquals("3", actualHtmlNode.getContent());
        assertEquals("#", actualHtmlNode.name);
        assertNull(actualHtmlNode.getFirstTag());
        assertTrue(actualHtmlNode.getChildren().isEmpty());
    }

    @Test
    public void testConstructor10() {
        HTMLNode actualHtmlNode = new HTMLNode("_blank");
        assertNull(actualHtmlNode.getContent());
        assertEquals("_blank", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        assertSame(expectedChildren, actualHtmlNode.getChildren());
    }

    @Test
    public void testConstructor11() {
        HTMLNode actualHtmlNode = new HTMLNode("a");
        assertNull(actualHtmlNode.getContent());
        assertEquals("a", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        assertSame(expectedChildren, actualHtmlNode.getChildren());
    }

    @Test
    public void testConstructor12() {
        HTMLNode actualHtmlNode = new HTMLNode("h1");
        assertNull(actualHtmlNode.getContent());
        assertEquals("h1", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        assertSame(expectedChildren, actualHtmlNode.getChildren());
    }

    @Test
    public void testConstructor13() {
        HTMLNode actualHtmlNode = new HTMLNode("Name", "Not all who wander are lost");
        assertNull(actualHtmlNode.getContent());
        assertEquals("name", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        List<HTMLNode> children = actualHtmlNode.getChildren();
        assertSame(expectedChildren, children);
        assertEquals(1, children.size());
        HTMLNode getResult = children.get(0);
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("#", getResult.name);
        assertNull(getResult.getFirstTag());
    }

    @Test
    public void testConstructor14() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode((String) null, "Not all who wander are lost");
    }

    @Test
    public void testConstructor15() {
        HTMLNode actualHtmlNode = new HTMLNode("%", "Not all who wander are lost");
        assertEquals("Not all who wander are lost", actualHtmlNode.getContent());
        assertEquals("%", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        assertSame(expectedChildren, actualHtmlNode.getChildren());
    }

    @Test
    public void testConstructor16() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("", "Not all who wander are lost");
    }

    @Test
    public void testConstructor17() {
        HTMLNode actualHtmlNode = new HTMLNode("Name", (String) null);
        assertNull(actualHtmlNode.getContent());
        assertEquals("name", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        assertSame(expectedChildren, actualHtmlNode.getChildren());
    }

    @Test
    public void testConstructor18() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("^[A-Za-z][A-Za-z0-9]*$", "Not all who wander are lost");
    }

    @Test
    public void testConstructor19() {
        HTMLNode actualHtmlNode = new HTMLNode("_blank", "Not all who wander are lost");
        assertNull(actualHtmlNode.getContent());
        assertEquals("_blank", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        List<HTMLNode> children = actualHtmlNode.getChildren();
        assertSame(expectedChildren, children);
        assertEquals(1, children.size());
        HTMLNode getResult = children.get(0);
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("#", getResult.name);
        assertNull(getResult.getFirstTag());
    }

    @Test
    public void testConstructor2() {
        HTMLNode actualHtmlNode = new HTMLNode(HTMLNode.text(3), false);
        assertEquals("3", actualHtmlNode.getContent());
        assertEquals("#", actualHtmlNode.name);
        assertNull(actualHtmlNode.getFirstTag());
        assertTrue(actualHtmlNode.getChildren().isEmpty());
    }

    @Test
    public void testConstructor20() {
        HTMLNode actualHtmlNode = new HTMLNode("a", "Not all who wander are lost");
        assertNull(actualHtmlNode.getContent());
        assertEquals("a", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        List<HTMLNode> children = actualHtmlNode.getChildren();
        assertSame(expectedChildren, children);
        assertEquals(1, children.size());
        HTMLNode getResult = children.get(0);
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("#", getResult.name);
        assertNull(getResult.getFirstTag());
    }

    @Test
    public void testConstructor21() {
        HTMLNode actualHtmlNode = new HTMLNode("h1", "Not all who wander are lost");
        assertNull(actualHtmlNode.getContent());
        assertEquals("h1", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        List<HTMLNode> children = actualHtmlNode.getChildren();
        assertSame(expectedChildren, children);
        assertEquals(1, children.size());
        HTMLNode getResult = children.get(0);
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("#", getResult.name);
        assertNull(getResult.getFirstTag());
    }

    @Test
    public void testConstructor22() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", "Attribute Name", "Attribute Value");
    }

    @Test
    public void testConstructor23() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("UUU", "Attribute Name", "Attribute Value");
    }

    @Test
    public void testConstructor24() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode(null, "Attribute Name", "Attribute Value");
    }

    @Test
    public void testConstructor25() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("#", "Attribute Name", "Attribute Value");
    }

    @Test
    public void testConstructor26() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("%", "Attribute Name", "Attribute Value");
    }

    @Test
    public void testConstructor27() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("attributeName is not legal", "Attribute Name", "Attribute Value");
    }

    @Test
    public void testConstructor28() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("", "Attribute Name", "Attribute Value");
    }

    @Test
    public void testConstructor29() {
        HTMLNode actualHtmlNode = new HTMLNode("Name", "UUU", "Attribute Value");
        assertNull(actualHtmlNode.getContent());
        assertEquals("name", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        assertSame(expectedChildren, actualHtmlNode.getChildren());
    }

    @Test
    public void testConstructor3() {
        HTMLNode actualHtmlNode = new HTMLNode(HTMLNode.text(57), false);
        assertEquals("57", actualHtmlNode.getContent());
        assertEquals("#", actualHtmlNode.name);
        assertNull(actualHtmlNode.getFirstTag());
        assertTrue(actualHtmlNode.getChildren().isEmpty());
    }

    @Test
    public void testConstructor30() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", (String) null, "Attribute Value");
    }

    @Test
    public void testConstructor31() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", "#", "Attribute Value");
    }

    @Test
    public void testConstructor32() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", "UUU", (String) null);
    }

    @Test
    public void testConstructor33() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("_blank", "Attribute Name", "Attribute Value");
    }

    @Test
    public void testConstructor34() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("h1", "Attribute Name", "Attribute Value");
    }

    @Test
    public void testConstructor35() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testConstructor36() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("UUU", "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testConstructor37() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode(null, "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testConstructor38() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("#", "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testConstructor39() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("%", "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testConstructor4() {
        HTMLNode actualHtmlNode = new HTMLNode("Name");
        assertNull(actualHtmlNode.getContent());
        assertEquals("name", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        assertSame(expectedChildren, actualHtmlNode.getChildren());
    }

    @Test
    public void testConstructor40() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("attributeName is not legal", "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testConstructor41() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("", "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testConstructor42() {
        HTMLNode actualHtmlNode = new HTMLNode("Name", "UUU", "Attribute Value", "Not all who wander are lost");
        assertNull(actualHtmlNode.getContent());
        assertEquals("name", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        List<HTMLNode> children = actualHtmlNode.getChildren();
        assertSame(expectedChildren, children);
        assertEquals(1, children.size());
        HTMLNode getResult = children.get(0);
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("#", getResult.name);
        assertNull(getResult.getFirstTag());
    }

    @Test
    public void testConstructor43() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", (String) null, "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testConstructor44() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", "#", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testConstructor45() {
        HTMLNode actualHtmlNode = new HTMLNode("%", "UUU", "Attribute Value", "Not all who wander are lost");
        assertEquals("Not all who wander are lost", actualHtmlNode.getContent());
        assertEquals("%", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        assertSame(expectedChildren, actualHtmlNode.getChildren());
    }

    @Test
    public void testConstructor46() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", "UUU", (String) null, "Not all who wander are lost");
    }

    @Test
    public void testConstructor47() {
        HTMLNode actualHtmlNode = new HTMLNode("Name", "UUU", "Attribute Value", null);
        assertNull(actualHtmlNode.getContent());
        assertEquals("name", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        assertSame(expectedChildren, actualHtmlNode.getChildren());
    }

    @Test
    public void testConstructor48() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("_blank", "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testConstructor49() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("h1", "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testConstructor5() {
        HTMLNode actualHtmlNode = new HTMLNode("#");
        assertNull(actualHtmlNode.getContent());
        assertEquals("#", actualHtmlNode.name);
        assertNull(actualHtmlNode.getFirstTag());
    }

    @Test
    public void testConstructor50() {
        HTMLNode actualHtmlNode = new HTMLNode("Name", new String[]{"foo", "foo", "foo"},
                new String[]{"foo", "foo", "foo"});
        assertNull(actualHtmlNode.getContent());
        assertEquals("name", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        assertSame(expectedChildren, actualHtmlNode.getChildren());
    }

    @Test
    public void testConstructor51() {
        HTMLNode actualHtmlNode = new HTMLNode("#", new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"});
        assertNull(actualHtmlNode.getContent());
        assertEquals("#", actualHtmlNode.name);
        assertNull(actualHtmlNode.getFirstTag());
    }

    @Test
    public void testConstructor52() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode(null, new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"});
    }

    @Test
    public void testConstructor53() {
        HTMLNode actualHtmlNode = new HTMLNode("%", new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"});
        assertNull(actualHtmlNode.getContent());
        assertEquals("%", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        assertSame(expectedChildren, actualHtmlNode.getChildren());
    }

    @Test
    public void testConstructor54() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("", new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"});
    }

    @Test
    public void testConstructor55() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", new String[]{"#", "foo", "foo"}, new String[]{"foo", "foo", "foo"});
    }

    @Test
    public void testConstructor56() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", new String[]{null, "foo", "foo"}, new String[]{"foo", "foo", "foo"});
    }

    @Test
    public void testConstructor57() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", new String[]{"Attribute Names", "foo", "foo"}, new String[]{"foo", "foo", "foo"});
    }

    @Test
    public void testConstructor58() {
        HTMLNode actualHtmlNode = new HTMLNode("Name", (String[]) null, new String[]{"foo", "foo", "foo"});
        assertNull(actualHtmlNode.getContent());
        assertEquals("name", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        assertSame(expectedChildren, actualHtmlNode.getChildren());
    }

    @Test
    public void testConstructor59() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", new String[]{"#"}, new String[]{"foo", "foo", "foo"});
    }

    @Test
    public void testConstructor6() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode(null);
    }

    @Test
    public void testConstructor60() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", new String[]{"foo", "foo", "foo"}, new String[]{null, "foo", "foo"});
    }

    @Test
    public void testConstructor61() {
        HTMLNode actualHtmlNode = new HTMLNode("Name", new String[]{"foo", "foo", "foo"}, (String[]) null);
        assertNull(actualHtmlNode.getContent());
        assertEquals("name", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        assertSame(expectedChildren, actualHtmlNode.getChildren());
    }

    @Test
    public void testConstructor62() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("UUU", new String[]{"#", "foo", "foo"}, new String[]{"foo", "foo", "foo"});
    }

    @Test
    public void testConstructor63() {
        HTMLNode actualHtmlNode = new HTMLNode("_blank", new String[]{"foo", "foo", "foo"},
                new String[]{"foo", "foo", "foo"});
        assertNull(actualHtmlNode.getContent());
        assertEquals("_blank", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        assertSame(expectedChildren, actualHtmlNode.getChildren());
    }

    @Test
    public void testConstructor64() {
        HTMLNode actualHtmlNode = new HTMLNode("Name", new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"},
                "Not all who wander are lost");
        assertNull(actualHtmlNode.getContent());
        assertEquals("name", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        List<HTMLNode> children = actualHtmlNode.getChildren();
        assertSame(expectedChildren, children);
        assertEquals(1, children.size());
        HTMLNode getResult = children.get(0);
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("#", getResult.name);
        assertNull(getResult.getFirstTag());
    }

    @Test
    public void testConstructor65() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode(null, new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"},
                "Not all who wander are lost");
    }

    @Test
    public void testConstructor66() {
        HTMLNode actualHtmlNode = new HTMLNode("%", new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"},
                "Not all who wander are lost");
        assertEquals("Not all who wander are lost", actualHtmlNode.getContent());
        assertEquals("%", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        assertSame(expectedChildren, actualHtmlNode.getChildren());
    }

    @Test
    public void testConstructor67() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("", new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"},
                "Not all who wander are lost");
    }

    @Test
    public void testConstructor68() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", new String[]{"#", "foo", "foo"}, new String[]{"foo", "foo", "foo"},
                "Not all who wander are lost");
    }

    @Test
    public void testConstructor69() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", new String[]{null, "foo", "foo"}, new String[]{"foo", "foo", "foo"},
                "Not all who wander are lost");
    }

    @Test
    public void testConstructor7() {
        HTMLNode actualHtmlNode = new HTMLNode("%");
        assertNull(actualHtmlNode.getContent());
        assertEquals("%", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        assertSame(expectedChildren, actualHtmlNode.getChildren());
    }

    @Test
    public void testConstructor70() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", new String[]{"Attribute Names", "foo", "foo"}, new String[]{"foo", "foo", "foo"},
                "Not all who wander are lost");
    }

    @Test
    public void testConstructor71() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", new String[]{"#"}, new String[]{"foo", "foo", "foo"}, "Not all who wander are lost");
    }

    @Test
    public void testConstructor72() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("Name", new String[]{"foo", "foo", "foo"}, new String[]{null, "foo", "foo"},
                "Not all who wander are lost");
    }

    @Test
    public void testConstructor73() {
        HTMLNode actualHtmlNode = new HTMLNode("Name", new String[]{"foo", "foo", "foo"}, (String[]) null,
                "Not all who wander are lost");
        assertNull(actualHtmlNode.getContent());
        assertEquals("name", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        List<HTMLNode> children = actualHtmlNode.getChildren();
        assertSame(expectedChildren, children);
        assertEquals(1, children.size());
        HTMLNode getResult = children.get(0);
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("#", getResult.name);
        assertNull(getResult.getFirstTag());
    }

    @Test
    public void testConstructor74() {
        HTMLNode actualHtmlNode = new HTMLNode("Name", new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"},
                null);
        assertNull(actualHtmlNode.getContent());
        assertEquals("name", actualHtmlNode.name);
        List<HTMLNode> expectedChildren = actualHtmlNode.children;
        assertSame(expectedChildren, actualHtmlNode.getChildren());
    }

    @Test
    public void testConstructor75() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("UUU", new String[]{"#", "foo", "foo"}, new String[]{"foo", "foo", "foo"},
                "Not all who wander are lost");
    }

    @Test
    public void testConstructor8() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("");
    }

    @Test
    public void testConstructor9() {
        thrown.expect(IllegalArgumentException.class);
        new HTMLNode("^[A-Za-z][A-Za-z0-9]*$");
    }

    @Test
    public void testNewlineOpen() {
        assertFalse(HTMLNode.text(3).newlineOpen("Name"));
    }

    @Test
    public void testClone() {
        HTMLNode actualCloneResult = HTMLNode.text(3).clone();
        assertEquals("3", actualCloneResult.getContent());
        assertEquals("#", actualCloneResult.name);
        assertNull(actualCloneResult.getFirstTag());
        assertTrue(actualCloneResult.getChildren().isEmpty());
    }

    @Test
    public void testCheckNamePattern() {
        assertTrue(HTMLNode.text(3).checkNamePattern("Str"));
        assertFalse(HTMLNode.text(3).checkNamePattern(""));
        assertFalse(HTMLNode.text(3).checkNamePattern("#"));
        assertFalse(HTMLNode.text(3).checkNamePattern("^[A-Za-z][A-Za-z0-9]*$"));
        assertTrue(HTMLNode.text(3).checkNamePattern("_blank"));
        assertTrue(HTMLNode.text(3).checkNamePattern("a"));
        assertTrue(HTMLNode.text(3).checkNamePattern("h1"));
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        exampleNodeNonEmpty = null;
        exampleNodeEmpty = null;
        try {
            exampleNodeNonEmpty = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY);
            exampleNodeEmpty = new HTMLNode(SAMPLE_OKAY_NODE_NAME_EMPTY);
        } catch (IllegalArgumentException iae1) {
            fail("Unexpected exception thrown!");
        }
        assertNotNull(exampleNodeNonEmpty);
        assertNotNull(exampleNodeEmpty);
        assertEquals(0, exampleNodeEmpty.children.size());
        assertEquals(0, exampleNodeNonEmpty.children.size());
    }

    /**
     * Tests HTMLNode(String,String,String,String) constructor
     * using non-ASCII chars
     */
    public void testHTMLNode_StringStringStringString_WrongNodeName() {
        try {
            new HTMLNode(SAMPLE_WRONG_NODE_NAME, SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE, SAMPLE_NODE_CONTENT);
            fail("Expected exception not thrown!");
        } catch (IllegalArgumentException iae1) {
        }
        try {
            new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY, SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE, SAMPLE_NODE_CONTENT);
        } catch (IllegalArgumentException iae1) {
            fail("Unexpected exception thrown!");
        }
    }

    public void testHTMLNode_StringStringStringString_WrongAttributeName() {
        try {
            new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY, SAMPLE_WRONG_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE, SAMPLE_NODE_CONTENT);
            fail("Expected exception not thrown!");
        } catch (IllegalArgumentException iae1) {
        }
        try {
            new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY, SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE, SAMPLE_NODE_CONTENT);
        } catch (IllegalArgumentException iae1) {
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * Tests HTMLNode(String,String[],String[],String) constructor
     * verifying if all attributes are correctly inserted
     */
    public void testHTMLNode_AttributesArray() {
        int size = 100;
        String[] methodAttributesName = new String[size];
        String[] methodAttributesValue = new String[size];
        for (int i = 0; i < size; i++) {
            methodAttributesName[i] = "AttributeName" + i;
            methodAttributesValue[i] = "Value " + i;
        }
        HTMLNode methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY,
                methodAttributesName, methodAttributesValue,
                SAMPLE_NODE_CONTENT);
        //checks presence
        for (int i = 0; i < size; i++)
            assertEquals(methodAttributesValue[i],
                    methodHTMLNode.getAttribute(methodAttributesName[i]));
        //checks size
        assertEquals(size, methodHTMLNode.getAttributes().size());
    }

    /**
     * Tests addAttribute(String,String) method
     * adding the same attribute many times
     * and verifying it keeps only one
     * reference to it.
     */
    public void testSameAttributeManyTimes() {
        int times = 100;
        String methodAttributeName = "exampleAttributeName";
        String methodAttributeValue = "exampleAttributeValue";
        for (int i = 0; i < times; i++) {
            exampleNodeNonEmpty.addAttribute(methodAttributeName, methodAttributeValue);
            assertEquals(exampleNodeNonEmpty.getAttributes().size(), 1);
        }
    }

    /**
     * Tests addChild(HTMLNode) method
     * adding the Node itself as its
     * child. The method should rise an exception
     */
    public void testAddChildUsingTheNodeItselfAsChild() {
        try {
            exampleNodeNonEmpty.addChild(exampleNodeNonEmpty);
            fail("Expected Exception Error Not Thrown!");
        } catch (IllegalArgumentException anException) {
            assertNotNull(anException);
        }
    }

    /**
     * Tests addChildren(HTMLNode[]) method
     * adding the Node itself as its
     * child. The method should rise an exception
     */
    public void testAddChildrenUsingTheNodeItselfAsChild() {
        HTMLNode[] methodHTMLNodesArray = {new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY),
                exampleNodeNonEmpty,
                new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY + "1")};
        try {
            exampleNodeNonEmpty.addChildren(methodHTMLNodesArray);
            fail("Expected Exception Error Not Thrown!");
        } catch (IllegalArgumentException anException) {
            assertNotNull(anException);
        }
    }

    @Test
    public void testAddChild() {
        HTMLNode textResult = HTMLNode.text(3);
        textResult.addChild(HTMLNode.text(3));
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild10() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("_blank");
        assertNull(actualAddChildResult.getContent());
        assertEquals("_blank", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        assertSame(expectedChildren, actualAddChildResult.getChildren());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild11() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("h1");
        assertNull(actualAddChildResult.getContent());
        assertEquals("h1", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        assertSame(expectedChildren, actualAddChildResult.getChildren());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild12() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("Node Name", "Not all who wander are lost");
    }

    @Test
    public void testAddChild13() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("UUU", "Not all who wander are lost");
        assertNull(actualAddChildResult.getContent());
        assertEquals("uuu", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        List<HTMLNode> children = actualAddChildResult.getChildren();
        assertSame(expectedChildren, children);
        assertEquals(1, children.size());
        HTMLNode getResult = children.get(0);
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("#", getResult.name);
        assertNull(getResult.getFirstTag());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild14() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild(null, "Not all who wander are lost");
    }

    @Test
    public void testAddChild15() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("%", "Not all who wander are lost");
        assertEquals("Not all who wander are lost", actualAddChildResult.getContent());
        assertEquals("%", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        assertSame(expectedChildren, actualAddChildResult.getChildren());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild16() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("element name is not legal", "Not all who wander are lost");
    }

    @Test
    public void testAddChild17() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("", "Not all who wander are lost");
    }

    @Test
    public void testAddChild18() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("^[A-Za-z][A-Za-z0-9]*$", "Not all who wander are lost");
    }

    @Test
    public void testAddChild19() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("UUU", null);
        assertNull(actualAddChildResult.getContent());
        assertEquals("uuu", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        assertSame(expectedChildren, actualAddChildResult.getChildren());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild2() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("Node Name");
    }

    @Test
    public void testAddChild20() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("_blank", "Not all who wander are lost");
        assertNull(actualAddChildResult.getContent());
        assertEquals("_blank", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        List<HTMLNode> children = actualAddChildResult.getChildren();
        assertSame(expectedChildren, children);
        assertEquals(1, children.size());
        HTMLNode getResult = children.get(0);
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("#", getResult.name);
        assertNull(getResult.getFirstTag());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild21() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("h1", "Not all who wander are lost");
        assertNull(actualAddChildResult.getContent());
        assertEquals("h1", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        List<HTMLNode> children = actualAddChildResult.getChildren();
        assertSame(expectedChildren, children);
        assertEquals(1, children.size());
        HTMLNode getResult = children.get(0);
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("#", getResult.name);
        assertNull(getResult.getFirstTag());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild22() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("Node Name", "Attribute Name", "Attribute Value");
    }

    @Test
    public void testAddChild23() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("UUU", "Attribute Name", "Attribute Value");
    }

    @Test
    public void testAddChild24() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild(null, "Attribute Name", "Attribute Value");
    }

    @Test
    public void testAddChild25() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("#", "Attribute Name", "Attribute Value");
    }

    @Test
    public void testAddChild26() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("%", "Attribute Name", "Attribute Value");
    }

    @Test
    public void testAddChild27() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("element name is not legal", "Attribute Name", "Attribute Value");
    }

    @Test
    public void testAddChild28() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("", "Attribute Name", "Attribute Value");
    }

    @Test
    public void testAddChild29() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("^[A-Za-z][A-Za-z0-9]*$", "Attribute Name", "Attribute Value");
    }

    @Test
    public void testAddChild3() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("UUU");
        assertNull(actualAddChildResult.getContent());
        assertEquals("uuu", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        assertSame(expectedChildren, actualAddChildResult.getChildren());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild30() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("UUU", "UUU", "Attribute Value");
        assertNull(actualAddChildResult.getContent());
        assertEquals("uuu", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        assertSame(expectedChildren, actualAddChildResult.getChildren());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild31() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("UUU", (String) null, "Attribute Value");
    }

    @Test
    public void testAddChild32() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("UUU", "#", "Attribute Value");
    }

    @Test
    public void testAddChild33() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("UUU", "UUU", (String) null);
    }

    @Test
    public void testAddChild34() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("_blank", "Attribute Name", "Attribute Value");
    }

    @Test
    public void testAddChild35() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("Node Name", "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testAddChild36() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("UUU", "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testAddChild37() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild(null, "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testAddChild38() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("#", "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testAddChild39() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("%", "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testAddChild4() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild((String) null);
    }

    @Test
    public void testAddChild40() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3)
                .addChild("element name is not legal", "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testAddChild41() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("", "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testAddChild42() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3)
                .addChild("^[A-Za-z][A-Za-z0-9]*$", "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testAddChild43() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("UUU", "UUU", "Attribute Value", "Not all who wander are lost");
        assertNull(actualAddChildResult.getContent());
        assertEquals("uuu", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        List<HTMLNode> children = actualAddChildResult.getChildren();
        assertSame(expectedChildren, children);
        assertEquals(1, children.size());
        HTMLNode getResult = children.get(0);
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("#", getResult.name);
        assertNull(getResult.getFirstTag());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild44() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("UUU", (String) null, "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testAddChild45() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("UUU", "#", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testAddChild46() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("%", "UUU", "Attribute Value", "Not all who wander are lost");
        assertEquals("Not all who wander are lost", actualAddChildResult.getContent());
        assertEquals("%", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        assertSame(expectedChildren, actualAddChildResult.getChildren());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild47() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("UUU", "UUU", (String) null, "Not all who wander are lost");
    }

    @Test
    public void testAddChild48() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("UUU", "UUU", "Attribute Value", null);
        assertNull(actualAddChildResult.getContent());
        assertEquals("uuu", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        assertSame(expectedChildren, actualAddChildResult.getChildren());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild49() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("_blank", "Attribute Name", "Attribute Value", "Not all who wander are lost");
    }

    @Test
    public void testAddChild5() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("#");
        assertNull(actualAddChildResult.getContent());
        assertEquals("#", actualAddChildResult.name);
        assertNull(actualAddChildResult.getFirstTag());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild50() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("Node Name", new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"});
    }

    @Test
    public void testAddChild51() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("UUU", new String[]{"foo", "foo", "foo"},
                new String[]{"foo", "foo", "foo"});
        assertNull(actualAddChildResult.getContent());
        assertEquals("uuu", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        assertSame(expectedChildren, actualAddChildResult.getChildren());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild52() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild(null, new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"});
    }

    @Test
    public void testAddChild53() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("#", new String[]{"foo", "foo", "foo"},
                new String[]{"foo", "foo", "foo"});
        assertNull(actualAddChildResult.getContent());
        assertEquals("#", actualAddChildResult.name);
        assertNull(actualAddChildResult.getFirstTag());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild54() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("%", new String[]{"foo", "foo", "foo"},
                new String[]{"foo", "foo", "foo"});
        assertNull(actualAddChildResult.getContent());
        assertEquals("%", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        assertSame(expectedChildren, actualAddChildResult.getChildren());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild55() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("", new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"});
    }

    @Test
    public void testAddChild56() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3)
                .addChild("^[A-Za-z][A-Za-z0-9]*$", new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"});
    }

    @Test
    public void testAddChild57() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("UUU", new String[]{"#", "foo", "foo"}, new String[]{"foo", "foo", "foo"});
    }

    @Test
    public void testAddChild58() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("UUU", new String[]{null, "foo", "foo"}, new String[]{"foo", "foo", "foo"});
    }

    @Test
    public void testAddChild59() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("UUU", (String[]) null, new String[]{"foo", "foo", "foo"});
        assertNull(actualAddChildResult.getContent());
        assertEquals("uuu", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        assertSame(expectedChildren, actualAddChildResult.getChildren());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild6() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("%");
        assertNull(actualAddChildResult.getContent());
        assertEquals("%", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        assertSame(expectedChildren, actualAddChildResult.getChildren());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild60() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("UUU", new String[]{"#"}, new String[]{"foo", "foo", "foo"});
    }

    @Test
    public void testAddChild61() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("UUU", new String[]{"foo", "foo", "foo"}, new String[]{null, "foo", "foo"});
    }

    @Test
    public void testAddChild62() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("UUU", new String[]{"foo", "foo", "foo"}, (String[]) null);
        assertNull(actualAddChildResult.getContent());
        assertEquals("uuu", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        assertSame(expectedChildren, actualAddChildResult.getChildren());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild63() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3)
                .addChild("Node Name", new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"},
                        "Not all who wander are lost");
    }

    @Test
    public void testAddChild64() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("UUU", new String[]{"foo", "foo", "foo"},
                new String[]{"foo", "foo", "foo"}, "Not all who wander are lost");
        assertNull(actualAddChildResult.getContent());
        assertEquals("uuu", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        List<HTMLNode> children = actualAddChildResult.getChildren();
        assertSame(expectedChildren, children);
        assertEquals(1, children.size());
        HTMLNode getResult = children.get(0);
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("#", getResult.name);
        assertNull(getResult.getFirstTag());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild65() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3)
                .addChild(null, new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"},
                        "Not all who wander are lost");
    }

    @Test
    public void testAddChild66() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("%", new String[]{"foo", "foo", "foo"},
                new String[]{"foo", "foo", "foo"}, "Not all who wander are lost");
        assertEquals("Not all who wander are lost", actualAddChildResult.getContent());
        assertEquals("%", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        assertSame(expectedChildren, actualAddChildResult.getChildren());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild67() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3)
                .addChild("", new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"},
                        "Not all who wander are lost");
    }

    @Test
    public void testAddChild68() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3)
                .addChild("^[A-Za-z][A-Za-z0-9]*$", new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"},
                        "Not all who wander are lost");
    }

    @Test
    public void testAddChild69() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3)
                .addChild("UUU", new String[]{"#", "foo", "foo"}, new String[]{"foo", "foo", "foo"},
                        "Not all who wander are lost");
    }

    @Test
    public void testAddChild7() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("element name is not legal");
    }

    @Test
    public void testAddChild70() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3)
                .addChild("UUU", new String[]{null, "foo", "foo"}, new String[]{"foo", "foo", "foo"},
                        "Not all who wander are lost");
    }

    @Test
    public void testAddChild71() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3)
                .addChild("UUU", new String[]{"#"}, new String[]{"foo", "foo", "foo"}, "Not all who wander are lost");
    }

    @Test
    public void testAddChild72() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3)
                .addChild("UUU", new String[]{"foo", "foo", "foo"}, new String[]{null, "foo", "foo"},
                        "Not all who wander are lost");
    }

    @Test
    public void testAddChild73() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("UUU", new String[]{"foo", "foo", "foo"}, (String[]) null,
                "Not all who wander are lost");
        assertNull(actualAddChildResult.getContent());
        assertEquals("uuu", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        List<HTMLNode> children = actualAddChildResult.getChildren();
        assertSame(expectedChildren, children);
        assertEquals(1, children.size());
        HTMLNode getResult = children.get(0);
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("#", getResult.name);
        assertNull(getResult.getFirstTag());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild74() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode actualAddChildResult = textResult.addChild("UUU", new String[]{"foo", "foo", "foo"},
                new String[]{"foo", "foo", "foo"}, null);
        assertNull(actualAddChildResult.getContent());
        assertEquals("uuu", actualAddChildResult.name);
        List<HTMLNode> expectedChildren = actualAddChildResult.children;
        assertSame(expectedChildren, actualAddChildResult.getChildren());
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChild8() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("");
    }

    @Test
    public void testAddChild9() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addChild("^[A-Za-z][A-Za-z0-9]*$");
    }

    /**
     * Tests addChild(String) method
     * using the same name every time
     * and verifying that a real new
     * HTML is always added.
     */
    public void testAddChildSameName() {
        int times = 100;
        for (int i = 1; i <= times; i++) {
            exampleNodeNonEmpty.addChild(SAMPLE_OKAY_NODE_NAME_NON_EMPTY);
            assertEquals(exampleNodeNonEmpty.children.size(), i);
        }
    }

    /**
     * Tests addChild(HTMLNode) method
     * verifying the behavior when adding
     * the same HTMLNode instance two times.
     * It should raise an IllegalArgument exception.
     */
    public void testAddChildSameObject() {
        HTMLNode methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY);
        exampleNodeNonEmpty.addChild(methodHTMLNode);
        try {
            exampleNodeNonEmpty.addChild(methodHTMLNode);
            fail("Expected Exception Error Not Thrown!");
        } catch (IllegalArgumentException anException) {
            assertNotNull(anException);
        }
    }

    @Test
    public void testAddChildren() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        HTMLNode textResult = HTMLNode.text(3);
        textResult.addChildren(new ArrayList<HTMLNode>());
    }

    @Test
    public void testAddChildren2() {
        HTMLNode textResult = HTMLNode.text(3);

        ArrayList<HTMLNode> htmlNodeList = new ArrayList<HTMLNode>();
        htmlNodeList.add(HTMLNode.text(3));
        textResult.addChildren(htmlNodeList);
        assertEquals(1, textResult.getChildren().size());
    }

    @Test
    public void testAddChildren3() {
        HTMLNode textResult = HTMLNode.text(3);
        HTMLNode textResult1 = HTMLNode.text(3);
        HTMLNode textResult2 = HTMLNode.text(3);
        textResult.addChildren(new HTMLNode[]{textResult1, textResult2, HTMLNode.text(3)});
        assertEquals(3, textResult.getChildren().size());
    }

    @Test
    public void testAddChildren4() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        HTMLNode textResult = HTMLNode.text(3);
        PeerTrustInputForAddPeerBoxNode peerTrustInputForAddPeerBoxNode = new PeerTrustInputForAddPeerBoxNode();
        HTMLNode textResult1 = HTMLNode.text(3);
        textResult.addChildren(new HTMLNode[]{peerTrustInputForAddPeerBoxNode, textResult1, HTMLNode.text(3)});
    }

    @Test
    public void testAddChildren5() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        HTMLNode.text(3).addChildren(new HTMLNode[]{});
    }

    /**
     * Tests addChildren(HTMLNode[]) method
     * verifying the behavior when adding
     * the same HTMLNode instance two times.
     */
    public void testAddChildrenSameObject() {
        HTMLNode methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY);
        HTMLNode[] methodHTMLNodesArray = {methodHTMLNode,
                methodHTMLNode};
        try {
            exampleNodeNonEmpty.addChildren(methodHTMLNodesArray);
            fail("Expected Exception Error Not Thrown!");
        } catch (IllegalArgumentException anException) {
            assertNotNull(anException);
        }
    }

    /**
     * Tests addChildren(String,String,String) method
     * verifying if the child is correctly added
     * and if it generates good output using generate() method.
     */
    public void testAddChild_StringStringString() {
        HTMLNode methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_EMPTY);
        methodHTMLNode.addChild(SAMPLE_OKAY_NODE_NAME_EMPTY,
                SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE);
        List<HTMLNode> childrenList = methodHTMLNode.children;
        assertEquals(1, childrenList.size());
        assertEquals(generateNoContentNodeOutput(SAMPLE_OKAY_NODE_NAME_EMPTY,
                SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE),
                childrenList.get(0).generate());
    }

    /**
     * Tests addChildren(String,String,String,String) method
     * verifying if the child is correctly added
     * and if it generates good output using generate() method.
     */
    public void testAddChild_StringStringStringString() {
        HTMLNode methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY);
        methodHTMLNode.addChild(SAMPLE_OKAY_NODE_NAME_NON_EMPTY,
                SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE,
                SAMPLE_NODE_CONTENT);
        List<HTMLNode> childrenList = methodHTMLNode.children;
        assertEquals(1, childrenList.size());
        assertEquals(generateFullNodeOutput(SAMPLE_OKAY_NODE_NAME_NON_EMPTY,
                SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE,
                SAMPLE_NODE_CONTENT),
                childrenList.get(0).generate());
    }

    /**
     * Tests addChildren(String,String[],String[]) method
     * verifying if the child is correctly added
     * and the child attributes are corrects.
     */
    public void testAddChild_StringArrayArray() {
        String[] methodAttributesNamesArray = {"firstName", "secondName", "thirdName"};
        String[] methodAttributesValuesArray = {"firstValue", "secondValue", "thirdValue"};
        HTMLNode methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY);
        methodHTMLNode.addChild(SAMPLE_OKAY_NODE_NAME_NON_EMPTY,
                methodAttributesNamesArray, methodAttributesValuesArray);
        testSingleChildAttributes(methodHTMLNode,
                methodAttributesNamesArray, methodAttributesValuesArray);
    }

    /**
     * Tests addChildren(String,String[],String[],String) method
     * verifying if the child is correctly added
     * and the child attributes are corrects.
     */
    public void testAddChild_StringArrayArrayString() {
        String[] methodAttributesNamesArray = {"firstName", "secondName", "thirdName"};
        String[] methodAttributesValuesArray = {"firstValue", "secondValue", "thirdValue"};
        HTMLNode methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY);
        methodHTMLNode.addChild(SAMPLE_OKAY_NODE_NAME_NON_EMPTY,
                methodAttributesNamesArray, methodAttributesValuesArray,
                SAMPLE_NODE_CONTENT);
        testSingleChildAttributes(methodHTMLNode,
                methodAttributesNamesArray, methodAttributesValuesArray);
    }

    @Test
    public void testGetFirstTag() {
        assertNull(HTMLNode.text(3).getFirstTag());
        assertEquals("name", (new HTMLNode("Name")).getFirstTag());
    }

    /**
     * Check the passed HTMLNode only child attributes
     *
     * @param aHTMLNode        where we fetch the only child
     * @param attibutesNames   the attributes names to check
     * @param attributesValues the attributes values to check
     */
    private void testSingleChildAttributes(HTMLNode aHTMLNode, String[] attibutesNames, String[] attributesValues) {
        List<HTMLNode> childrenList = aHTMLNode.children;
        assertEquals(1, childrenList.size());
        HTMLNode childHTMLNode = childrenList.get(0);
        assertEquals(attibutesNames.length, childHTMLNode.getAttributes().size());
        for (int i = 0; i < attibutesNames.length; i++)
            assertEquals(attributesValues[i],
                    childHTMLNode.getAttribute(attibutesNames[i]));
    }

    /**
     * Tests getContent() method using
     * common sample HTMLNode, and "#"
     * "%" named nodes
     */
    public void testGetContent() {
        HTMLNode methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY);
        assertNull(methodHTMLNode.getContent());

        methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY, SAMPLE_NODE_CONTENT);
        //since the HTMLNode name is not "#", or "%",
        //the content will be a new child with the "#" name
        assertEquals(SAMPLE_NODE_CONTENT,
                methodHTMLNode.children.get(0).getContent());
        assertNull(methodHTMLNode.getContent());

        methodHTMLNode = new HTMLNode("#", SAMPLE_NODE_CONTENT);
        assertEquals(SAMPLE_NODE_CONTENT,
                methodHTMLNode.getContent());
        methodHTMLNode = new HTMLNode("%", SAMPLE_NODE_CONTENT);
        assertEquals(SAMPLE_NODE_CONTENT,
                methodHTMLNode.getContent());
    }

    /**
     * Tests getAttribute() method using
     * common sample HTMLNode, and "#"
     * "%" named nodes
     */
    public void testGetAttribute() {
        HTMLNode methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY);
        assertNull(methodHTMLNode.getAttribute(SAMPLE_OKAY_ATTRIBUTE_NAME));

        methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY, SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE);
        assertEquals(SAMPLE_ATTRIBUTE_VALUE, methodHTMLNode.getAttribute(SAMPLE_OKAY_ATTRIBUTE_NAME));
        methodHTMLNode = new HTMLNode("#", SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE);
        assertEquals(SAMPLE_ATTRIBUTE_VALUE, methodHTMLNode.getAttribute(SAMPLE_OKAY_ATTRIBUTE_NAME));
        methodHTMLNode = new HTMLNode("%", SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE);
        assertEquals(SAMPLE_ATTRIBUTE_VALUE, methodHTMLNode.getAttribute(SAMPLE_OKAY_ATTRIBUTE_NAME));
    }

    @Test
    public void testGetAttribute2() {
        assertNull(HTMLNode.text(3).getAttribute("Attribute Name"));
    }

    /**
     * Tests getAttributes() and setAttribute(String,String)
     * methods verifying if attributes are correctly
     * inserted and fetched.
     */
    public void testAddGetAttributes() {
        int attributesNumber = 100;
        String methodAttributeName = "";
        String counterString = "";
        HTMLNode methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY);
        for (int i = 0; i < attributesNumber; i++) {
            counterString = String.valueOf(i);
            methodAttributeName = "attribute " + counterString;
            assertEquals(i, methodHTMLNode.getAttributes().size());
            methodHTMLNode.addAttribute(methodAttributeName, counterString);
            assertEquals(counterString, methodHTMLNode.getAttribute(methodAttributeName));
            assertEquals(counterString, methodHTMLNode.getAttributes().get(methodAttributeName));
        }
    }

    @Test
    public void testGetAttributes() {
        assertTrue(HTMLNode.text(3).getAttributes().isEmpty());
    }

    @Test
    public void testAddAttribute() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        HTMLNode.text(3).addAttribute("Attribute Name", "Attribute Value");
    }

    @Test
    public void testAddAttribute2() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addAttribute(null, "Attribute Value");
    }

    @Test
    public void testAddAttribute3() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.text(3).addAttribute("Attribute Name", null);
    }

    /**
     * Tests addAttribute(String,String) method
     * trying to insert an attribute with a null
     * as name value. It should rise an
     * IllegalArgument exception
     */
    public void testAddAttribute_nullAttributeName() {
        HTMLNode methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY);
        try {
            methodHTMLNode.addAttribute(null, SAMPLE_ATTRIBUTE_VALUE);
            fail("Expected Exception Error Not Thrown!");
        } catch (IllegalArgumentException anException) {
            assertNotNull(anException);
        }
    }

    /**
     * Tests addAttribute(String,String) method
     * trying to insert an attribute with a null
     * as attribute value. It should rise an
     * IllegalArgument exception
     */
    public void testAddAttribute_nullAttributeValue() {
        HTMLNode methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY);
        try {
            methodHTMLNode.addAttribute(SAMPLE_WRONG_ATTRIBUTE_NAME, null);
            fail("Expected Exception Error Not Thrown!");
        } catch (IllegalArgumentException anException) {
            assertNotNull(anException);
        }
    }

    /**
     * Tests HTMLNode(String,String,String,String) and
     * HTMLNode(String,String,String) constructors
     * trying to create a node that has attribute name
     * null. It should raise an IllegalArgument exception
     */
    public void testHTMLNode_nullAttributeName() {
        try {
            new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY,
                    null, SAMPLE_ATTRIBUTE_VALUE,
                    SAMPLE_NODE_CONTENT);
            fail("Expected Exception Error Not Thrown!");
        } catch (IllegalArgumentException anException) {
            assertNotNull(anException);
        }
        try {
            new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY,
                    null, SAMPLE_ATTRIBUTE_VALUE);
            fail("Expected Exception Error Not Thrown!");
        } catch (IllegalArgumentException anException) {
            assertNotNull(anException);
        }
    }

    /**
     * Tests HTMLNode(String,String,String,String) and
     * HTMLNode(String,String,String) constructors
     * trying to create a node that has attribute value
     * null. It should raise an IllegalArgument exception
     */
    public void testHTMLNode_nullAttributeValue() {
        try {
            new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY,
                    SAMPLE_WRONG_ATTRIBUTE_NAME, null,
                    SAMPLE_NODE_CONTENT);
            fail("Expected Exception Error Not Thrown!");
        } catch (IllegalArgumentException anException) {
            assertNotNull(anException);
        }
        try {
            new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY,
                    SAMPLE_WRONG_ATTRIBUTE_NAME, null);
            fail("Expected Exception Error Not Thrown!");
        } catch (IllegalArgumentException anException) {
            assertNotNull(anException);
        }
    }

    /**
     * Tests HTMLNode(String,String[],String[],String)
     * constructor trying to create a node that has
     * attributes name null. It should raise an
     * IllegalArgument exception
     */
    public void testHTMLNodeArray_nullAttributeName() {
        String[] methodAttributesNameArray = {"first", null, "after"};
        String[] methodAttributesValueArray = {SAMPLE_ATTRIBUTE_VALUE,
                SAMPLE_ATTRIBUTE_VALUE, SAMPLE_ATTRIBUTE_VALUE};
        testHTMLNodeArray_null(methodAttributesNameArray, methodAttributesValueArray);
    }

    /**
     * Tests HTMLNode(String,String[],String[],String)
     * constructor trying to create a node that has
     * attributes value null. It should raise an
     * IllegalArgument exception
     */
    public void testHTMLNodeArray_nullAttributeValue() {
        String[] methodAttributesNameArray = {SAMPLE_WRONG_ATTRIBUTE_NAME,
                SAMPLE_WRONG_ATTRIBUTE_NAME, SAMPLE_WRONG_ATTRIBUTE_NAME};
        String[] methodAttributesValueArray = {"first", null, "after"};
        testHTMLNodeArray_null(methodAttributesNameArray, methodAttributesValueArray);
    }

    /**
     * Tests HTMLNode(String,String[],String[],String)
     * constructor trying to create a node that has
     * different length for attributes names array and
     * attributes values array. It should raise an
     * IllegalArgument exception
     */
    public void testHTMLNode_attributeArrays_differentLengths() {
        String[] methodAttributesNameArray = {SAMPLE_WRONG_ATTRIBUTE_NAME,
                SAMPLE_WRONG_ATTRIBUTE_NAME};
        String[] methodAttributesValueArray = {SAMPLE_ATTRIBUTE_VALUE,
                SAMPLE_ATTRIBUTE_VALUE, SAMPLE_ATTRIBUTE_VALUE};
        testHTMLNodeArray_null(methodAttributesNameArray, methodAttributesValueArray);
    }

    /**
     * Tests if the passed arrays raise an IllegalArgumentException
     * using them to create a new HTMLNode (i.e. one of the name or value
     * must be null)
     *
     * @param attributesNames  the array of attribute names
     * @param attributesValues the array of attribute values
     */
    private void testHTMLNodeArray_null(String[] attributesNames, String[] attributesValues) {
        try {
            new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY,
                    attributesNames, attributesValues,
                    SAMPLE_NODE_CONTENT);
            fail("Expected Exception Error Not Thrown!");
        } catch (IllegalArgumentException anException) {
            assertNotNull(anException);
        }
    }

    /**
     * Fetches the first line of a String
     *
     * @param aString the String to consider
     * @return the first line of the String
     */
    private String readFirstLine(String aString) {
        int newLineIndex = aString.indexOf('\n');
        if (newLineIndex == -1)
            return aString;
        return aString.substring(0, newLineIndex);
    }

    /**
     * Tests generate() method with a
     * HTMLNode that has "textarea","div","a"
     * as node name, since they generates a different
     * output from all other names.
     */
    public void testGenerate_fromHTMLNode_textareaDivA() {
        HTMLNode methodHTMLNode;
        String[] nodeNamesArray = {"textarea", "div", "a"};
        for (int i = 0; i < nodeNamesArray.length; i++) {
            boolean newlines = new HTMLNode("a").newlineOpen(nodeNamesArray[i]);
            methodHTMLNode = new HTMLNode(nodeNamesArray[i],
                    SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE);
            assertEquals(generateFullNodeOutput(nodeNamesArray[i],
                    SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE, "", newlines),
                    methodHTMLNode.generate());
        }
    }

    @Test
    public void testHTMLDoctypeGenerate() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        HTMLNode.HTMLDoctype htmlDoctype = new HTMLNode.HTMLDoctype("Doctype", "System Uri");
        htmlDoctype.generate(new StringBuilder());
    }

    /**
     * Tests generate() method when the
     * node has a special name
     * (i.e. "div","form","input","script","table","tr","td")
     * and a child
     */
    public void testGenerate_fromHTMLNodeWithChild_SpecialNames() {
        HTMLNode methodHTMLNode;
        String[] nodeNamesArray = {"div", "form",// input is an empty element!
                "script", "table", "tr", "td"};
        HTMLNode methodChildNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY,
                SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE,
                SAMPLE_NODE_CONTENT);
        for (int i = 0; i < nodeNamesArray.length; i++) {
            methodHTMLNode = new HTMLNode(nodeNamesArray[i],
                    SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE,
                    SAMPLE_NODE_CONTENT);
            methodHTMLNode.addChild(methodChildNode);

            assertEquals(("<" + nodeNamesArray[i] + " ").toLowerCase() +
                            SAMPLE_OKAY_ATTRIBUTE_NAME + "=" +
                            "\"" + SAMPLE_ATTRIBUTE_VALUE + "\">\n" +
                            // FIXME why is this using 2 tabs? I don't understand ...
                            "\t\t" + SAMPLE_NODE_CONTENT +

                            //child
                            generateFullNodeOutput(SAMPLE_OKAY_NODE_NAME_NON_EMPTY,
                                    SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE,
                                    SAMPLE_NODE_CONTENT) +
                            "\n\t" +
                            ("</" + nodeNamesArray[i] + ">\n").toLowerCase()
                            + "\t",

                    methodHTMLNode.generate());
        }
    }

    /**
     * Tests generate() method with a
     * HTMLNode with only the name.
     * The resulting string should be in the form:
     * <node_name />
     */
    public void testGenerate_fromHTMLNode_String() {
        HTMLNode methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_EMPTY);
        assertEquals(("<" + SAMPLE_OKAY_NODE_NAME_EMPTY + " />").toLowerCase(),
                methodHTMLNode.generate());
    }

    /**
     * Tests generate() method with a
     * HTMLNode with the name and content.
     * The resulting string should be in the form:
     * <node_name>Node_Content</node_name>
     */
    public void testGenerate_fromHTMLNode_StringString() {
        HTMLNode methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY, SAMPLE_NODE_CONTENT);
        assertEquals(("<" + SAMPLE_OKAY_NODE_NAME_NON_EMPTY + ">").toLowerCase() +
                        SAMPLE_NODE_CONTENT +
                        ("</" + SAMPLE_OKAY_NODE_NAME_NON_EMPTY + ">").toLowerCase(),
                methodHTMLNode.generate());
    }

    /**
     * Tests generate() method with a
     * HTMLNode with the name, an attribute and its value.
     * The resulting string should be in the form:
     * <node_name Attribute_Name="Attribute_Value" />
     */
    public void testGenerate_fromHTMLNode_StringStringString() {
        HTMLNode methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_EMPTY,
                SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE);
        assertEquals(generateNoContentNodeOutput(SAMPLE_OKAY_NODE_NAME_EMPTY,
                SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE),
                methodHTMLNode.generate());
    }

    /**
     * Tests generate() method with a
     * HTMLNode with the name, an attribute and its value.
     * The resulting string should be in the form:
     * <node_name Attribute_Name="Attribute_Value">Node_Content</node_name>
     */
    public void testGenerate_fromHTMLNode_StringStringStringString() {
        HTMLNode methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY,
                SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE,
                SAMPLE_NODE_CONTENT);
        assertEquals(generateFullNodeOutput(SAMPLE_OKAY_NODE_NAME_NON_EMPTY,
                SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE,
                SAMPLE_NODE_CONTENT),
                methodHTMLNode.generate());
    }

    /**
     * Generates the correct output for the HTMLNode.generate() method
     * when called from a single node having only a name and an attribute
     * name and value
     *
     * @param aName           the HTMLNode name
     * @param aAttributeName  the HTMLNode attribute name
     * @param aAttributeValue the HTMLNode attribute value
     * @return the correct output expected by HTMLNode.generate() method
     */
    private String generateNoContentNodeOutput(String aName, String aAttributeName, String aAttributeValue) {
        return ("<" + aName + " ").toLowerCase() +
                aAttributeName + "=" +
                "\"" + aAttributeValue + "\"" +
                " />";
    }

    private String generateFullNodeOutput(String aName, String aAttributeName, String aAttributeValue, String aContent) {
        return generateFullNodeOutput(aName, aAttributeName, aAttributeValue, aContent, false);
    }

    /**
     * Generates the correct output for the HTMLNode.generate() method
     * when called from a single node having the specified parameters
     *
     * @param aName           the HTMLNode name
     * @param aAttributeName  the HTMLNode attribute name
     * @param aAttributeValue the HTMLNode attribute value
     * @param aContent        the HTMLNode content
     * @return the correct output expected by HTMLNode.generate() method
     */
    private String generateFullNodeOutput(String aName, String aAttributeName, String aAttributeValue, String aContent, boolean indent) {
        StringBuffer sb = new StringBuffer();
        sb.append("<" + aName.toLowerCase() + " ");
        sb.append(aAttributeName + "=");
        sb.append("\"" + aAttributeValue + "\">");
        String indenting = indent ? "\n\t" : "";
        if (!aContent.equals(""))
            sb.append(indenting + aContent);
        sb.append(indenting + ("</" + aName + ">").toLowerCase());
        if (indent) sb.append(indenting);
        return sb.toString();
    }

    @Test
    public void testGenerate() {
        assertEquals("3", HTMLNode.text(3).generate());
        assertEquals("<name></name>", (new HTMLNode("Name")).generate());
        assertEquals("<name>Not all who wander are lost</name>",
                (new HTMLNode("Name", "Not all who wander are lost")).generate());
        assertEquals("<name foo=\"foo\"></name>",
                (new HTMLNode("Name", new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"})).generate());
        assertEquals("", (new HTMLNode("#")).generate());
        assertEquals("null", (new HTMLNode("%")).generate());
        assertEquals("<area />", (new HTMLNode("area")).generate());
        assertEquals("<body>\n\t</body>\n\t", (new HTMLNode("body")).generate());
        assertEquals("<body>\n\t\tNot all who wander are lost\n\t</body>\n\t",
                (new HTMLNode("body", "Not all who wander are lost")).generate());
        assertEquals("<h1></h1>\n\t", (new HTMLNode("h1")).generate());
    }

    @Test
    public void testGenerate10() {
        HTMLNode htmlNode = new HTMLNode("Name");
        htmlNode.setContent("Not all who wander are lost");
        StringBuilder stringBuilder = new StringBuilder();
        assertSame(stringBuilder, htmlNode.generate(stringBuilder));
    }

    @Test
    public void testGenerate11() {
        HTMLNode textResult = HTMLNode.text(3);
        StringBuilder stringBuilder = new StringBuilder();
        assertSame(stringBuilder, textResult.generate(stringBuilder, 2));
    }

    @Test
    public void testGenerate12() {
        HTMLNode htmlNode = new HTMLNode("Name");
        StringBuilder stringBuilder = new StringBuilder();
        assertSame(stringBuilder, htmlNode.generate(stringBuilder, 2));
    }

    @Test
    public void testGenerate13() {
        HTMLNode htmlNode = new HTMLNode("Name", "Not all who wander are lost");
        StringBuilder stringBuilder = new StringBuilder();
        assertSame(stringBuilder, htmlNode.generate(stringBuilder, 2));
    }

    @Test
    public void testGenerate14() {
        HTMLNode htmlNode = new HTMLNode("Name", new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"});
        StringBuilder stringBuilder = new StringBuilder();
        assertSame(stringBuilder, htmlNode.generate(stringBuilder, 2));
    }

    @Test
    public void testGenerate15() {
        HTMLNode htmlNode = new HTMLNode("#");
        StringBuilder stringBuilder = new StringBuilder();
        assertSame(stringBuilder, htmlNode.generate(stringBuilder, 2));
    }

    @Test
    public void testGenerate16() {
        HTMLNode htmlNode = new HTMLNode("%");
        StringBuilder stringBuilder = new StringBuilder();
        assertSame(stringBuilder, htmlNode.generate(stringBuilder, 2));
    }

    @Test
    public void testGenerate17() {
        HTMLNode htmlNode = new HTMLNode("Name");
        htmlNode.setContent("Not all who wander are lost");
        StringBuilder stringBuilder = new StringBuilder();
        assertSame(stringBuilder, htmlNode.generate(stringBuilder, 2));
    }

    @Test
    public void testGenerate2() {
        HTMLNode htmlNode = new HTMLNode("Name");
        htmlNode.setContent("Not all who wander are lost");
        assertEquals("<name>Not all who wander are lost</name>", htmlNode.generate());
    }

    @Test
    public void testGenerate3() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        (new HTMLNode.HTMLDoctype("Doctype", "System Uri")).generate();
    }

    @Test
    public void testGenerate4() {
        HTMLNode textResult = HTMLNode.text(3);
        StringBuilder stringBuilder = new StringBuilder();
        assertSame(stringBuilder, textResult.generate(stringBuilder));
    }

    @Test
    public void testGenerate5() {
        HTMLNode htmlNode = new HTMLNode("Name");
        StringBuilder stringBuilder = new StringBuilder();
        assertSame(stringBuilder, htmlNode.generate(stringBuilder));
    }

    @Test
    public void testGenerate6() {
        HTMLNode htmlNode = new HTMLNode("Name", "Not all who wander are lost");
        StringBuilder stringBuilder = new StringBuilder();
        assertSame(stringBuilder, htmlNode.generate(stringBuilder));
    }

    @Test
    public void testGenerate7() {
        HTMLNode htmlNode = new HTMLNode("Name", new String[]{"foo", "foo", "foo"}, new String[]{"foo", "foo", "foo"});
        StringBuilder stringBuilder = new StringBuilder();
        assertSame(stringBuilder, htmlNode.generate(stringBuilder));
    }

    @Test
    public void testGenerate8() {
        HTMLNode htmlNode = new HTMLNode("#");
        StringBuilder stringBuilder = new StringBuilder();
        assertSame(stringBuilder, htmlNode.generate(stringBuilder));
    }

    @Test
    public void testGenerate9() {
        HTMLNode htmlNode = new HTMLNode("%");
        StringBuilder stringBuilder = new StringBuilder();
        assertSame(stringBuilder, htmlNode.generate(stringBuilder));
    }

    /**
     * Tests generate() method with a
     * HTMLNode that has a child.
     * <node_name Attribute_Name="Attribute_Value">Node_Content
     * <child_node_name child_Attribute_Name="child_Attribute_Value">child_Node_Content</child_node_name>
     * </node_name>
     */
    public void testGenerate_HTMLNode_withChild() {
        HTMLNode methodHTMLNode = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY,
                SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE,
                SAMPLE_NODE_CONTENT);
        HTMLNode methodHTMLNodeChild = new HTMLNode(SAMPLE_OKAY_NODE_NAME_NON_EMPTY,
                SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE,
                SAMPLE_NODE_CONTENT);

        methodHTMLNode.addChild(methodHTMLNodeChild);

        assertEquals(("<" + SAMPLE_OKAY_NODE_NAME_NON_EMPTY + " ").toLowerCase() +
                        SAMPLE_OKAY_ATTRIBUTE_NAME + "=" +
                        "\"" + SAMPLE_ATTRIBUTE_VALUE + "\">" +
                        SAMPLE_NODE_CONTENT +

                        //child
                        generateFullNodeOutput(SAMPLE_OKAY_NODE_NAME_NON_EMPTY,
                                SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE,
                                SAMPLE_NODE_CONTENT) +

                        ("</" + SAMPLE_OKAY_NODE_NAME_NON_EMPTY + ">").toLowerCase(),
                methodHTMLNode.generate());
    }

    /**
     * Tests generate() method with a
     * HTMLNode that has "%" as name.
     * The expected output is just the HTMLNode content
     */
    public void testGenerate_fromHTMLNode_percentName() {
        HTMLNode methodHTMLNode = new HTMLNode("%",
                SAMPLE_OKAY_ATTRIBUTE_NAME, SAMPLE_ATTRIBUTE_VALUE,
                SAMPLE_NODE_CONTENT);
        assertEquals(SAMPLE_NODE_CONTENT,
                methodHTMLNode.generate());
    }

    /**
     * Tests HTMLDoctype.generate() method
     * comparing the result with the expected
     * String. It is useful for regression tests.
     */
    public void testHTMLDoctype_generate() {
        String sampleDocType = "html";
        String sampleSystemUri = "-//W3C//DTD XHTML 1.1//EN";
        HTMLNode methodHTMLNodeDoc = new HTMLNode.HTMLDoctype(sampleDocType, sampleSystemUri);
        methodHTMLNodeDoc.addChild(SAMPLE_OKAY_NODE_NAME_EMPTY);
        String generatedString = methodHTMLNodeDoc.generate();
        //consider only the HTMLDocType generated text
        assertEquals("<!DOCTYPE " + sampleDocType + " PUBLIC \"" + sampleSystemUri + "\">",
                readFirstLine(generatedString));

    }

    @Test
    public void testGenerateChildren() {
        assertEquals("3", HTMLNode.text(3).generateChildren());
        assertEquals("", (new HTMLNode("Name")).generateChildren());
        assertEquals("Not all who wander are lost",
                (new HTMLNode("Name", "Not all who wander are lost")).generateChildren());
    }

    @Test
    public void testSetContent() {
        HTMLNode textResult = HTMLNode.text(3);
        textResult.setContent("Not all who wander are lost");
        assertEquals("Not all who wander are lost", textResult.getContent());
    }

    @Test
    public void testLink() {
        HTMLNode actualLinkResult = HTMLNode.link("Path");
        assertNull(actualLinkResult.getContent());
        assertEquals("a", actualLinkResult.name);
        List<HTMLNode> expectedChildren = actualLinkResult.children;
        assertSame(expectedChildren, actualLinkResult.getChildren());
    }

    @Test
    public void testLink2() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.link(null);
    }

    @Test
    public void testLinkInNewWindow() {
        HTMLNode actualLinkInNewWindowResult = HTMLNode.linkInNewWindow("Path");
        assertNull(actualLinkInNewWindowResult.getContent());
        assertEquals("a", actualLinkInNewWindowResult.name);
        List<HTMLNode> expectedChildren = actualLinkInNewWindowResult.children;
        assertSame(expectedChildren, actualLinkInNewWindowResult.getChildren());
    }

    @Test
    public void testLinkInNewWindow2() {
        thrown.expect(IllegalArgumentException.class);
        HTMLNode.linkInNewWindow(null);
    }

    @Test
    public void testText() {
        HTMLNode actualTextResult = HTMLNode.text(3);
        assertEquals("3", actualTextResult.getContent());
        assertEquals("#", actualTextResult.name);
        assertNull(actualTextResult.getFirstTag());
    }

    @Test
    public void testText2() {
        HTMLNode actualTextResult = HTMLNode.text(3L);
        assertEquals("3", actualTextResult.getContent());
        assertEquals("#", actualTextResult.name);
        assertNull(actualTextResult.getFirstTag());
    }

    @Test
    public void testText3() {
        HTMLNode actualTextResult = HTMLNode.text("Text");
        assertEquals("Text", actualTextResult.getContent());
        assertEquals("#", actualTextResult.name);
        assertNull(actualTextResult.getFirstTag());
    }

    @Test
    public void testText4() {
        HTMLNode actualTextResult = HTMLNode.text((String) null);
        assertNull(actualTextResult.getContent());
        assertEquals("#", actualTextResult.name);
        assertNull(actualTextResult.getFirstTag());
    }

    @Test
    public void testText5() {
        HTMLNode actualTextResult = HTMLNode.text((short) 1);
        assertEquals("1", actualTextResult.getContent());
        assertEquals("#", actualTextResult.name);
        assertNull(actualTextResult.getFirstTag());
    }

    @Test
    public void testRemoveChildren() {
        HTMLNode textResult = HTMLNode.text(3);
        textResult.removeChildren();
        assertTrue(textResult.getChildren().isEmpty());
    }

}
