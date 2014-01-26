package stni.js4java.java;

import stni.js4java.jsdoc.JsDoc;
import stni.js4java.jsdoc.JsDocTag;

import static stni.js4java.jsdoc.Tag.PARAM;

/**
 *
 */
public abstract class AbstractInterfaceCreator implements InterfaceCreator {
    private final TypeResolver typeResolver;

    public AbstractInterfaceCreator(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    protected String header(String name, String targetPackage, Imports imports) {
        return packge(targetPackage) + imports(imports) + interfaceDef(name);
    }

    protected String packge(String targetPackage) {
        return "package " + targetPackage + ";\n\n";
    }

    protected String imports(Imports imports) {
        final StringBuilder s = new StringBuilder();
        for (String imprt : imports) {
            s.append("import " + imprt + ";\n");
        }
        return s.append("\n").toString();
    }

    protected String interfaceDef(String name) {
        return "public interface " + name + "{\n";
    }

    protected String method(JsDoc jsDoc,Imports imports) {
        String res = "";
        if (jsDoc.getElement().isFunction()) {
            final JsDocTag ret = jsDoc.getReturnTag();
            if (ret == null) {
                throw new InterfaceCreatorException("@return is missing for function '" + jsDoc.getElement().getName() + "'");
            }
            res += javadoc(jsDoc) + methodSignature(jsDoc, false, imports) + ";\n\n";
        }
        return res;
    }

    protected String javadoc(JsDoc jsDoc) {
        final StringBuilder s = new StringBuilder()
                .append("  /**\n")
                .append("   * " + jsDoc.getDescription() + "\n");
        for (JsDocTag tag : jsDoc.getTags(PARAM)) {
            s.append("   * @param " + tag.getParameter() + " " + tag.getDescription() + "\n");
        }
        return s
                .append("   * @return " + jsDoc.getReturnTag().getDescription() + "\n")
                .append("   */\n")
                .toString();
    }

    protected String methodSignature(JsDoc jsDoc, boolean isPublic, Imports imports) {
        final StringBuilder s = new StringBuilder()
                .append(isPublic ? "  public " : "  ")
                .append(resolveType(jsDoc.getReturnTag(), imports) + " " + jsDoc.getElement().getName() + "(");
        boolean first = true;
        for (JsDocTag tag : jsDoc.getTags(PARAM)) {
            if (first) {
                first = false;
            } else {
                s.append(", ");
            }
            s.append(resolveType(tag, imports) + " " + tag.getParameter());
        }
        return s.append(")").toString();
    }

    protected String footer() {
        return "}";
    }

    protected String resolveType(JsDocTag tag, Imports imports) {
        return typeResolver.toJavaType(tag.getType(), imports);
    }

    protected String decapitalize(String s) {
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }
}
