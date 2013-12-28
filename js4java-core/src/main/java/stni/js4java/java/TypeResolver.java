package stni.js4java.java;

import java.util.Set;

/**
 *
 */
public interface TypeResolver {
    String toJavaType(String jsType, Set<String> imports);
}
