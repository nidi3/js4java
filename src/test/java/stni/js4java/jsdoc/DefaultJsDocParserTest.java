package stni.js4java.jsdoc;

import org.junit.Ignore;
import org.junit.Test;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class DefaultJsDocParserTest {
    private DefaultJsDocParser parser = new DefaultJsDocParser();

    @Test
    public void empty() throws Exception {
        assertTrue(parser.parseJsDoc(new StringReader("")).isEmpty());
    }

    @Test
    public void withoutJsDoc() throws Exception {
        assertTrue(parser.parseJsDoc(new StringReader("bla\nblu/**")).isEmpty());
    }

    @Test
    public void emptyJsDoc() throws Exception {
        final List<JsDoc> jsDocs = parser.parseJsDoc(new StringReader("bla\n/** */var a"));
        assertEquals(1, jsDocs.size());
        assertEquals("", jsDocs.get(0).getDescription());
    }

    @Test
    public void onlyOneLineDesc() throws Exception {
        final List<JsDoc> jsDocs = parser.parseJsDoc(new StringReader("bla\n/** Desc. */ var a"));
        assertEquals(1, jsDocs.size());
        assertEquals("Desc.", jsDocs.get(0).getDescription());
        assertFalse(jsDocs.get(0).iterator().hasNext());
    }

    @Test
    public void onlyDesc() throws Exception {
        final List<JsDoc> jsDocs = parser.parseJsDoc(new StringReader("bla\n/** Desc \n * second line \n * */ \n var a"));
        assertEquals(1, jsDocs.size());
        assertEquals("Desc second line", jsDocs.get(0).getDescription());
        assertFalse(jsDocs.get(0).iterator().hasNext());
    }

    @Test
    @Ignore
    public void onlyTag() throws Exception {
        final List<JsDoc> jsDocs = parser.parseJsDoc(new StringReader("bla\n/** @const */ var a"));
        assertEquals(1, jsDocs.size());
        assertEquals("", jsDocs.get(0).getDescription());
        final Iterator<JsDocTag> iter = jsDocs.get(0).iterator();
        assertEquals(new JsDocTag("Const", null, null, null), iter.next());
        assertFalse(iter.hasNext());
    }

    @Test(expected = JsDocParserException.class)
    public void unknownTag() throws Exception {
        parser.parseJsDoc(new StringReader("bla\n/** desc \n * @bla \n */ \n more"));
    }

    @Test
    public void simpleTag() throws Exception {
        assertOneTag(new JsDocTag("const", null, null, null), "bla\n/** desc \n * @const \n */ \n var a;");
    }

    @Test
    public void descTag() throws Exception {
        assertOneTag(new JsDocTag("deprecated", null, null, "bla second"), "bla\n/** desc \n * @deprecated bla \n *second\n */ \n var a");
    }

    @Test
    public void typeTag() throws Exception {
        assertOneTag(new JsDocTag("type", "number", null, null), "bla\n/** desc \n * @type {number} \n */ \n var a");
    }

    @Test
    public void typeAndDescTag() throws Exception {
        assertOneTag(new JsDocTag("return", "number", null, "The result."), "bla\n/** desc \n * @return {number} The result.\n */ \n var a");
    }

    @Test
    public void fullTag() throws Exception {
        assertOneTag(new JsDocTag("param", "number", "n", "The result."), "bla\n/** desc \n * @param {number} n The result.\n */ \n var a");
    }

    @Test
    public void docedFunction() throws Exception {
        final List<JsDoc> jsDocs = parser.parseJsDoc(new StringReader("bla\n/** desc \n */ \n function bla(){"));
        assertEquals(new JsDocedElement("bla", true), jsDocs.get(0).getElement());
    }

    @Test
    public void docedVariable() throws Exception {
        final List<JsDoc> jsDocs = parser.parseJsDoc(new StringReader("bla\n/** desc \n */ \n var bla;"));
        assertEquals(new JsDocedElement("bla", false), jsDocs.get(0).getElement());
    }

    @Test
    public void docedFunctionAsVariable() throws Exception {
        final List<JsDoc> jsDocs = parser.parseJsDoc(new StringReader("bla\n/** desc \n */ \n var bla=function(){"));
        assertEquals(new JsDocedElement("bla", true), jsDocs.get(0).getElement());
    }

    private void assertOneTag(JsDocTag expected, String input) throws Exception {
        final List<JsDoc> jsDocs = parser.parseJsDoc(new StringReader(input));
        assertEquals(1, jsDocs.size());
        final JsDoc tag = jsDocs.get(0);
        assertEquals(expected, tag.getTag(expected.getName()));
        assertEquals(1, tag.size());
    }
}
