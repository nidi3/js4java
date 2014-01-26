package stni.js4java.java;

import org.junit.Test;
import stni.js4java.jsdoc.JsDoc;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class DefaultInterfaceCreatorTest extends AbstractInterfaceCreatorTest {

    @Test(expected = InterfaceCreatorException.class)
    public void noReturnType() throws IOException {
        InterfaceCreator creator = new DefaultInterfaceCreator(new DefaultTypeResolver());
        final JsDoc jsDoc = doc(func("a"));
        creator.createInterfaces(Collections.singletonList(jsDoc), "Iface", "pack");
    }

    @Test
    public void noParams() throws IOException {
        InterfaceCreator creator = new DefaultInterfaceCreator(new DefaultTypeResolver());

        final List<JsDoc> jsDocs = Arrays.asList(
                doc(func("a"), returnTag("string")),
                doc(decl("b"), returnTag("string")));

        final List<InterfaceDescriptor> noParams = creator.createInterfaces(jsDocs, "NoParams", "stni.js4java.java");
        assertEquals(1, noParams.size());
        assertEquals("NoParams", noParams.get(0).getName());
        assertEquals(loadResource("NoParams.java"), noParams.get(0).getContent());
    }

    @Test
    public void standardTypes() throws IOException {
        InterfaceCreator creator = new DefaultInterfaceCreator(new ClasspathTypeResolver(Arrays.asList("java.util"), getClass().getClassLoader()));

        final List<JsDoc> jsDocs = Arrays.asList(
                doc(func("a"), returnTag("string")),
                doc(func("b"),
                        returnTag("Date"),
                        paramTag("!number", "a"),
                        paramTag("number", "b"),
                        paramTag("!boolean", "c"),
                        paramTag("boolean", "d"),
                        paramTag("List", "e")
                ));

        final List<InterfaceDescriptor> ids = creator.createInterfaces(jsDocs, "StandardTypes", "stni.js4java.java");
        assertEquals(1, ids.size());
        assertEquals("StandardTypes", ids.get(0).getName());
        assertEquals(loadResource("StandardTypes.java"), ids.get(0).getContent());
    }
}
