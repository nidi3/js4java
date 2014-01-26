package stni.js4java.java;

import stni.js4java.jsdoc.JsDoc;
import stni.js4java.jsdoc.JsDocTag;
import stni.js4java.jsdoc.JsDocedElement;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static stni.js4java.jsdoc.Tag.PARAM;
import static stni.js4java.jsdoc.Tag.RETURN;

/**
 *
 */
public abstract class AbstractInterfaceCreatorTest {
    protected JsDocTag returnTag(String type) {
        return new JsDocTag(RETURN, type, null, "ret");
    }

    protected JsDocTag paramTag(String type, String name) {
        return new JsDocTag(PARAM, type, name, "w");
    }

    protected JsDoc doc(JsDocedElement element, JsDocTag... tags) {
        return new JsDoc("bla", Arrays.asList(tags), element);
    }

    protected JsDocedElement func(String name) {
        return new JsDocedElement(name, true);
    }

    protected JsDocedElement decl(String name) {
        return new JsDocedElement(name, false);
    }

    protected String loadResource(String name) throws IOException {
        final InputStream in = getClass().getResourceAsStream(name);
        byte[] buf = new byte[10000];
        int read = in.read(buf, 0, in.available());
        return new String(buf, 0, read, "utf-8");
    }
}
