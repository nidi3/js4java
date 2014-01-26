package stni.js4java.java;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 */
public class Imports implements Iterable<String> {
    private final Set<String> imports = new TreeSet<>();

    public Imports add(String imprt) {
        imports.add(imprt);
        return this;
    }

    public String resolve(String simpleName) {
        String found = null;
        for (String imprt : imports) {
            if (imprt.endsWith("." + simpleName)) {
                if (found == null) {
                    found = imprt;
                } else {
                    return null;
                }
            }
        }
        return found;
    }

    @Override
    public Iterator<String> iterator() {
        return imports.iterator();
    }
}
