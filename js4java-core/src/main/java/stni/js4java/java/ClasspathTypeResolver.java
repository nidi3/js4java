package stni.js4java.java;

import java.util.List;

/**
 *
 */
public class ClasspathTypeResolver extends DefaultTypeResolver {
    private final List<String> knownPackages;
    private final ClassLoader classLoader;

    public ClasspathTypeResolver(List<String> knownPackages, ClassLoader classLoader) {
        this.knownPackages = knownPackages;
        this.classLoader = classLoader;
    }

    @Override
    protected String resolveNonDefaultType(String jsType, Imports imports) {
        for (String knownPackage : knownPackages) {
            final String className = knownPackage + "." + jsType;
            if (exists(className)) {
                imports.add(className);
                return jsType;
            }
        }
        throw new TypeResolverException("Unknown type '" + jsType + "'");
    }

    private boolean exists(String className) {
        try {
            Class.forName(className, false, classLoader);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
