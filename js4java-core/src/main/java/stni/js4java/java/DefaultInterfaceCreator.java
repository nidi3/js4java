package stni.js4java.java;

import stni.js4java.jsdoc.JsDoc;

import java.util.Collections;
import java.util.List;

/**
 *
 */
public class DefaultInterfaceCreator extends AbstractInterfaceCreator {
    public DefaultInterfaceCreator(TypeResolver typeResolver) {
        super(typeResolver);
    }

    @Override
    public List<InterfaceDescriptor> createInterfaces(List<JsDoc> jsDocs, String sourceName, String targetPackage) {
        final String methods = methods(jsDocs, new Imports());
        final String s = header(sourceName, targetPackage, new Imports()) + methods + footer();
        return Collections.singletonList(new InterfaceDescriptor(sourceName, s));
    }

    private String methods(List<JsDoc> jsDocs, Imports imports) {
        final StringBuilder s = new StringBuilder();
        for (JsDoc jsDoc : jsDocs) {
            s.append(method(jsDoc, imports));
        }
        return s.toString();
    }

}
