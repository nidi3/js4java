package stni.js4java.jsdoc;

/**
 *
 */
public class JsDocTag {
    private final String name;
    private final String type;
    private final String parameter;
    private String description;

    public JsDocTag(String name, String type, String parameter, String description) {
        this.name = name;
        this.type = type;
        this.parameter = parameter;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getParameter() {
        return parameter;
    }

    public String getDescription() {
        return description;
    }

    void addDescription(String desc) {
        description += " " + desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JsDocTag jsDocTag = (JsDocTag) o;

        if (description != null ? !description.equals(jsDocTag.description) : jsDocTag.description != null) {
            return false;
        }
        if (!name.equals(jsDocTag.name)) {
            return false;
        }
        if (parameter != null ? !parameter.equals(jsDocTag.parameter) : jsDocTag.parameter != null) {
            return false;
        }
        if (type != null ? !type.equals(jsDocTag.type) : jsDocTag.type != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (parameter != null ? parameter.hashCode() : 0);
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "@" + name + " {" + type + "} " + parameter + " " + description;
    }
}
