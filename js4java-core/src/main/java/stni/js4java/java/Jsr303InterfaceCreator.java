package stni.js4java.java;

import stni.js4java.jsdoc.JsDoc;
import stni.js4java.jsdoc.JsDocTag;
import stni.js4java.jsdoc.JsDocedElement;
import stni.js4java.jsdoc.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Jsr303InterfaceCreator extends AbstractInterfaceCreator {

    private static final String IS_VALID = "isValid";

    public Jsr303InterfaceCreator(TypeResolver typeResolver) {
        super(typeResolver);
    }

    @Override
    public List<InterfaceDescriptor> createInterfaces(List<JsDoc> jsDocs, String sourceName, String targetPackage) {
        List<InterfaceDescriptor> res = new ArrayList<>();
        for (JsDoc jsDoc : jsDocs) {
            if (isValidator(jsDoc,new Imports())) {
                String name = validElementName(jsDoc) + "ConstraintValidator";
                res.add(new InterfaceDescriptor(name, createInterface(jsDoc, sourceName, name, targetPackage)));
            }
        }
        return res;
    }

    private boolean isValidator(JsDoc jsDoc, Imports imports) {
        return validElementName(jsDoc) != null &&
                jsDoc.getTags(Tag.PARAM).size() == 1 &&
                jsDoc.getReturnTag() != null && resolveType(jsDoc.getReturnTag(), imports).equals("boolean");
    }

    private String validElementName(JsDoc jsDoc) {
        final String name = jsDoc.getElement().getName();
        return name.startsWith(IS_VALID) && name.length() > IS_VALID.length() ? name.substring(IS_VALID.length()) : null;
    }

    public String createInterface(JsDoc jsDoc, String sourceName, String name, String targetPackage) {
        final Imports imports = initImports(sourceName, targetPackage);
        final String method = method(jsDoc, sourceName, imports);
        return packge(targetPackage) + imports(imports) + classDef(jsDoc, sourceName, name, imports) + method + footer();
    }

    private Imports initImports(String sourceName, String targetPackage) {
        return new Imports()
                .add("org.springframework.beans.factory.annotation.Autowired")
                .add("javax.validation.ConstraintValidator")
                .add("javax.validation.ConstraintValidatorContext")
                .add("java.lang.annotation.Annotation")
                .add(targetPackage + "." + sourceName);
    }

    private String method(JsDoc jsDoc, String sourceName, Imports imports) {
        String res = "";
        if (jsDoc.getElement().isFunction()) {
            final JsDocTag ret = jsDoc.getReturnTag();
            if (ret == null) {
                throw new InterfaceCreatorException("@return is missing for function '" + jsDoc.getElement().getName() + "'");
            }
            JsDoc newJsDoc = validatorJsDoc(jsDoc);
            res += javadoc(jsDoc) + methodSignature(newJsDoc, true, imports) + "{\n" +
                    "    return " + decapitalize(sourceName) + "." + jsDoc.getElement().getName() +
                    "(" + jsDoc.getTag(Tag.PARAM).getParameter() + ");\n" +
                    "  }\n\n";
        }
        return res;
    }

    private JsDoc validatorJsDoc(JsDoc jsDoc) {
        final List<JsDocTag> tags = jsDoc.getTags();
        tags.add(new JsDocTag(Tag.PARAM, "ConstraintValidatorContext", "context", ""));
        return new JsDoc(jsDoc.getDescription(), tags, new JsDocedElement("isValid", true));
    }

    private String classDef(JsDoc jsDoc, String sourceName, String name, Imports imports) {
        return "public class " + name + " implements ConstraintValidator<Annotation, " + resolveType(jsDoc.getTag(Tag.PARAM), imports) + ">{\n\n" +
                "  @Autowired\n" +
                "  private " + sourceName + " " + decapitalize(sourceName) + ";\n\n" +
                "  public void initialize(Annotation constraintAnnotation) {}\n\n";
    }

}
