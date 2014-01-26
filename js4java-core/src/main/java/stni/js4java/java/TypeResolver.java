package stni.js4java.java;

/**
 *
 */
public interface TypeResolver {
    String toJavaType(String jsType, Imports imports);

    String boxedJava(String type);

    String boxedJs(String type);
}
