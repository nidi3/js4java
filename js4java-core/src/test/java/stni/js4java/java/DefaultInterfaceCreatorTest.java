package stni.js4java.java;

import org.junit.Test;
import stni.js4java.jsdoc.JsDoc;
import stni.js4java.jsdoc.JsDocTag;
import stni.js4java.jsdoc.JsDocedElement;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static stni.js4java.jsdoc.Tag.PARAM;
import static stni.js4java.jsdoc.Tag.RETURN;

/**
 *
 */
public class DefaultInterfaceCreatorTest {

    @Test(expected = InterfaceCreatorException.class)
    public void noReturnType() throws IOException {
        InterfaceCreator creator = new DefaultInterfaceCreator(new DefaultTypeResolver());
        final JsDoc jsDoc = new JsDoc("bla", Arrays.<JsDocTag>asList(), new JsDocedElement("a", true));
        creator.createInterface(Collections.singletonList(jsDoc), "pack.Iface", new StringWriter());

    }

    @Test
    public void noParams() throws IOException {
        InterfaceCreator creator = new DefaultInterfaceCreator(new DefaultTypeResolver());

        final List<JsDoc> jsDocs = Arrays.asList(
                new JsDoc("bla", Arrays.asList(new JsDocTag(RETURN, "string", null, "blu")), new JsDocedElement("a", true)),
                new JsDoc("bla", Arrays.asList(new JsDocTag(RETURN, "string", null, "blu")), new JsDocedElement("b", false)));

        final StringWriter out = new StringWriter();
        creator.createInterface(jsDocs, "stni.js4java.java.NoParams", out);
        assertEquals(loadResource("NoParams.java"), out.toString());
    }

    @Test
    public void standardTypes() throws IOException {
        InterfaceCreator creator = new DefaultInterfaceCreator(new ClasspathTypeResolver(Arrays.asList("java.util"), getClass().getClassLoader()));

        final List<JsDoc> jsDocs = Arrays.asList(
                new JsDoc("bla", Arrays.asList(new JsDocTag(RETURN, "string", null, "blu")), new JsDocedElement("a", true)),
                new JsDoc("bla", Arrays.asList(
                        new JsDocTag(RETURN, "Date", null, "blu"),
                        new JsDocTag(PARAM, "number", "a", "w"),
                        new JsDocTag(PARAM, "?number", "b", "w"),
                        new JsDocTag(PARAM, "boolean", "c", "w"),
                        new JsDocTag(PARAM, "?boolean", "d", "w"),
                        new JsDocTag(PARAM, "List", "e", "w")
                ), new JsDocedElement("b", true)));
        final StringWriter out = new StringWriter();
        creator.createInterface(jsDocs, "stni.js4java.java.StandardTypes", out);
        assertEquals(loadResource("StandardTypes.java"), out.toString());
    }

    private String loadResource(String name) throws IOException {
        final InputStream in = getClass().getResourceAsStream(name);
        byte[] buf = new byte[10000];
        int read = in.read(buf, 0, in.available());
        return new String(buf, 0, read, "utf-8");
    }
}
