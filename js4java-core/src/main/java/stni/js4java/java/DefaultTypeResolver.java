package stni.js4java.java;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class DefaultTypeResolver implements TypeResolver {
    private static final Pattern TYPED_ARRAY = Pattern.compile("Array\\.<(.*?)>");

    @Override
    public String toJavaType(String jsType, Set<String> imports) {
        if (jsType.contains("|") || jsType.contains("function") || jsType.contains("{") || jsType.contains("...") || jsType.contains("=")) {
            throw new TypeResolverException("Not supported are: -type unions (a|b) -function types (function(...)) -record types ({a:b}) -varargs (...a) -optional types (a=)");
        }
        boolean nullable = (jsType.charAt(0) == '?');
        boolean nonNullable = (jsType.charAt(0) == '!');
        if (nullable || nonNullable) {
            jsType = jsType.substring(1);
        }

        switch (jsType) {
            case "string":
                return "String";
            case "number":
                return nullable ? "Double" : "double";
            case "boolean":
                return nullable ? "Boolean" : "boolean";
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
                return resolveNonDefaultType(jsType, imports);
        }
    }

    protected String resolveNonDefaultType(String jsType, Set<String> imports) {
        throw new TypeResolverException("Unknown type '" + jsType + "'");
    }
}
