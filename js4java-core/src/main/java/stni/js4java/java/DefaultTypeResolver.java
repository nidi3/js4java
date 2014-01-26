package stni.js4java.java;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class DefaultTypeResolver implements TypeResolver {
    private static final Pattern TYPED_ARRAY = Pattern.compile("Array\\.<(.*?)>");

    @Override
    public String toJavaType(String jsType, Imports imports) {
        if (jsType.contains("|") || jsType.contains("function") || jsType.contains("{") || jsType.contains("...") || jsType.contains("=")) {
            throw new TypeResolverException("Not supported are: -type unions (a|b) -function types (function(...)) -record types ({a:b}) -varargs (...a) -optional types (a=)");
        }
        boolean nullable = (jsType.charAt(0) == '?');
        boolean nonNullable = (jsType.charAt(0) == '!');
        if (nullable || nonNullable) {
            jsType = jsType.substring(1);
        }

        switch (jsType) {
            case "*":
            case "?":
                return "Object";
            case "string":
                return "String";
            case "number":
                return nonNullable ? "double" : "Double";
            case "boolean":
                return nonNullable ? "boolean" : "Boolean";
            case "Date":
                imports.add("java.util.Date");
                return "Date";
            case "Array":
                return "Object[]";
            default:
                final Matcher arrayMatcher = TYPED_ARRAY.matcher(jsType);
                if (arrayMatcher.matches()) {
                    return toJavaType(arrayMatcher.group(1), imports) + "[]";
                }
                if (imports.resolve(jsType) != null) {
                    return jsType;
                }
                return resolveNonDefaultType(jsType, imports);
        }
    }

    @Override
    public String boxedJava(String type) {
        switch (type) {
            case "double":
                return "Double";
            case "boolean":
                return "Boolean";
            default:
                return type;
        }
    }

    @Override
    public String boxedJs(String type) {
        return type.startsWith("!") ? type.substring(1) : type;
    }

    protected String resolveNonDefaultType(String jsType, Imports imports) {
        throw new TypeResolverException("Unknown type '" + jsType + "'");
    }
}
