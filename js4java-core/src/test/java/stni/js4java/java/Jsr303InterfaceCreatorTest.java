package stni.js4java.java;

import org.junit.Before;
import org.junit.Test;
import stni.js4java.jsdoc.JsDoc;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class Jsr303InterfaceCreatorTest extends AbstractInterfaceCreatorTest {
    InterfaceCreator creator;

    @Before
    public void init() {
        creator = new Jsr303InterfaceCreator(new DefaultTypeResolver());
    }

    @Test
    public void ignoreWrongName() throws IOException {
        final List<JsDoc> jsDocs = Arrays.asList(
                doc(func("a"), returnTag("boolean"), paramTag("string", "email")),
                doc(func("isValid"), returnTag("boolean"), paramTag("string", "email")));

        assertEquals(0, creator.createInterfaces(jsDocs, "Ignore", "stni.js4java.java").size());
    }

    @Test
    public void ignoreWrongParams() throws IOException {
        final List<JsDoc> jsDocs = Arrays.asList(
                doc(func("isValidEmail"), returnTag("boolean")),
                doc(func("isValidEmail"), returnTag("boolean"), paramTag("string", "email1"), paramTag("string", "email2")));

        assertEquals(0, creator.createInterfaces(jsDocs, "Ignore", "stni.js4java.java").size());
    }

    @Test
    public void ignoreWrongReturn() throws IOException {
        final List<JsDoc> jsDocs = Arrays.asList(
                doc(func("isValidEmail"), returnTag("string"), paramTag("string", "email")),
                doc(func("isValidEmail"), paramTag("string", "email")));

        assertEquals(0, creator.createInterfaces(jsDocs, "Ignore", "stni.js4java.java").size());
    }

    @Test
    public void simple() throws IOException {
        final List<JsDoc> jsDocs = Arrays.asList(
                doc(func("isValidEmail"), returnTag("boolean"), paramTag("string", "email")));

        final List<InterfaceDescriptor> simple = creator.createInterfaces(jsDocs, "Validators", "stni.js4java.java");
        assertEquals(1, simple.size());
        assertEquals("EmailConstraintValidator", simple.get(0).getName());
        assertEquals(loadResource("EmailConstraintValidator.java"), simple.get(0).getContent());
    }

    @Test
    public void multiple() throws IOException {
        final List<JsDoc> jsDocs = Arrays.asList(
                doc(func("isValidEmail"), returnTag("boolean"), paramTag("string", "email")),
                doc(func("isValidName"), returnTag("boolean"), paramTag("string", "name")));

        final List<InterfaceDescriptor> multiple = creator.createInterfaces(jsDocs, "Validators", "stni.js4java.java");
        assertEquals(2, multiple.size());
        assertEquals("EmailConstraintValidator", multiple.get(0).getName());
        assertEquals(loadResource("EmailConstraintValidator.java"), multiple.get(0).getContent());
        assertEquals("NameConstraintValidator", multiple.get(1).getName());
        assertEquals(loadResource("NameConstraintValidator.java"), multiple.get(1).getContent());

    }


}
