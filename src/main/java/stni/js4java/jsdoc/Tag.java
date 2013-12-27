package stni.js4java.jsdoc;

/**
 *
 */
public enum Tag {
    CONST, CONSTRUCTOR, DICT, EXPOSE, FINAL, INHERIT_DOC, INTERFACE, NOSIDEEFFECTS, OVERRIDE, PRIVATE, PROTECTED, STRUCT,
    DEFINE, ENUM, EXTENDS, IMPLEMENTS, LENDS, THIS, TEMPLATE, TYPE, TYPEDEF,
    DEPRECATED, LICENSE, PRESERVE,
    RETURN,
    PARAM;

    public String realName() {
        String res = name().toLowerCase();
        int pos = res.indexOf('_');
        while (pos >= 0) {
            res = res.substring(0, pos) + Character.toUpperCase(res.charAt(pos + 1)) + res.substring(pos + 2);
            pos = res.indexOf('_', pos);
        }
        return res;
    }
}
