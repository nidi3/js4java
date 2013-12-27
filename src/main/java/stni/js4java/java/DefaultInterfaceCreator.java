package stni.js4java.java;

import stni.js4java.jsdoc.JsDoc;
import stni.js4java.jsdoc.JsDocTag;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static stni.js4java.jsdoc.Tag.PARAM;
import static stni.js4java.jsdoc.Tag.RETURN;

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
            writeHeader(name, bufferedOut);
            for (JsDoc jsDoc : jsDocs) {
                writeMethod(jsDoc, bufferedOut);
            }
            writeFooter(bufferedOut);
        }
    }

    private void writeHeader(String name, BufferedWriter out) throws IOException {
        name = name.replace('/', '.');
        int lastDot = name.lastIndexOf('.');
        writeln(out, "package " + name.substring(0, lastDot) + ";");
        writeln(out, "import java.util.Date;");
        writeln(out, "public interface " + name.substring(lastDot + 1) + "{");
    }

    private void writeMethod(JsDoc jsDoc, BufferedWriter out) throws IOException {
        if (jsDoc.getElement().isFunction()) {
            final JsDocTag ret = jsDoc.getTag(RETURN);
            if (ret == null) {
                throw new InterfaceCreatorException("@return is missing for function '" + jsDoc.getElement().getName() + "'");
            }
            writeJavadoc(out, jsDoc);
            writeMethodSignature(out, jsDoc);
            writeln(out, "");
        }
    }

    private void writeMethodSignature(BufferedWriter out, JsDoc jsDoc) throws IOException {
        write(out, "  " + resolveType(jsDoc.getTag(RETURN)) + " " + jsDoc.getElement().getName() + "(");
        boolean first = true;
        for (JsDocTag tag : jsDoc.getTags(PARAM)) {
            if (first) {
                first = false;
            } else {
                write(out, ", ");
            }
            write(out, resolveType(tag) + " " + tag.getParameter());
        }
        writeln(out, ");");
    }

    private void writeJavadoc(BufferedWriter out, JsDoc jsDoc) throws IOException {
        writeln(out, "  /**");
        writeln(out, "   * " + jsDoc.getDescription());
        for (JsDocTag tag : jsDoc.getTags(PARAM)) {
            writeln(out, "   * @param " + tag.getParameter() + " " + tag.getDescription());
        }
        writeln(out, "   * @return " + jsDoc.getTag(RETURN).getDescription());
        writeln(out, "   */");
    }


    private String resolveType(JsDocTag tag) {
        return typeResolver.toJavaType(tag.getType());
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
