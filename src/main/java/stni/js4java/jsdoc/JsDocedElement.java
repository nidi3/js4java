package stni.js4java.jsdoc;

/**
 *
 */
public class JsDocedElement {
    private final String name;
    private final boolean isFunction;

    public JsDocedElement(String name, boolean isFunction) {
        this.name = name;
        this.isFunction = isFunction;
    }

    public String getName() {
        return name;
    }

    public boolean isFunction() {
        return isFunction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JsDocedElement that = (JsDocedElement) o;

        if (isFunction != that.isFunction) {
            return false;
        }
        if (!name.equals(that.name)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (isFunction ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return isFunction ? ("function " + name) : ("var " + name);
    }
}
