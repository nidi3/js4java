package stni.js4java.java;

import stni.js4java.jsdoc.JsDoc;
import stni.js4java.jsdoc.JsDocTag;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static stni.js4java.jsdoc.Tag.PARAM;

/**
 *
 */
public class DefaultInterfaceCreator implements InterfaceCreator {
    private final TypeResolver typeResolver;

    public DefaultInterfaceCreator(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    public void createInterface(List<JsDoc> jsDocs, String name, Writer out) throws IOException {
        try (BufferedWriter bufferedOut = new BufferedWriter(out)) {
            Set<String> imports = new TreeSet<>();
            final String methods = writeMethods(jsDocs, imports);

            writeHeader(bufferedOut, name, imports);
            write(bufferedOut, methods);
            writeFooter(bufferedOut);
        }
    }

    private String writeMethods(List<JsDoc> jsDocs, Set<String> imports) throws IOException {
        final StringWriter sw = new StringWriter();
        final BufferedWriter bsw = new BufferedWriter(sw);
        for (JsDoc jsDoc : jsDocs) {
            writeMethod(bsw, jsDoc, imports);
        }
        bsw.flush();
        return sw.toString();
    }

    private void writeHeader(BufferedWriter out, String name, Set<String> imports) throws IOException {
        name = name.replace('/', '.');
        int lastDot = name.lastIndexOf('.');
        writeln(out, "package " + name.substring(0, lastDot) + ";");
        writeln(out, "");
        for (String imprt : imports) {
            writeln(out, "import " + imprt + ";");
        }
        writeln(out, "");
        writeln(out, "public interface " + name.substring(lastDot + 1) + "{");
    }

    private void writeMethod(BufferedWriter out, JsDoc jsDoc, Set<String> imports) throws IOException {
        if (jsDoc.getElement().isFunction()) {
            final JsDocTag ret = jsDoc.getReturnTag();
            if (ret == null) {
                throw new InterfaceCreatorException("@return is missing for function '" + jsDoc.getElement().getName() + "'");
            }
            writeJavadoc(out, jsDoc);
            writeMethodSignature(out, imports, jsDoc);
            writeln(out, "");
        }
    }

    private void writeMethodSignature(BufferedWriter out, Set<String> imports, JsDoc jsDoc) throws IOException {
        write(out, "  " + resolveType(jsDoc.getReturnTag(), imports) + " " + jsDoc.getElement().getName() + "(");
        boolean first = true;
        for (JsDocTag tag : jsDoc.getTags(PARAM)) {
            if (first) {
                first = false;
            } else {
                write(out, ", ");
            }
            write(out, resolveType(tag, imports) + " " + tag.getParameter());
        }
        writeln(out, ");");
    }

    private void writeJavadoc(BufferedWriter out, JsDoc jsDoc) throws IOException {
        writeln(out, "  /**");
        writeln(out, "   * " + jsDoc.getDescription());
        for (JsDocTag tag : jsDoc.getTags(PARAM)) {
            writeln(out, "   * @param " + tag.getParameter() + " " + tag.getDescription());
        }
        writeln(out, "   * @return " + jsDoc.getReturnTag().getDescription());
        writeln(out, "   */");
    }


    private String resolveType(JsDocTag tag, Set<String> imports) {
        return typeResolver.toJavaType(tag.getType(), imports);
    }

    private void writeFooter(BufferedWriter out) throws IOException {
        write(out, "}");
    }

    private void write(BufferedWriter out, String text) throws IOException {
        out.write(text);
    }

    private void writeln(BufferedWriter out, String text) throws IOException {
        out.write(text);
        out.newLine();
    }
}
