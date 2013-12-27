package stni.js4java.jsdoc;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static stni.js4java.jsdoc.Tag.*;

/**
 *
 */
enum TagType {
    TAG_ONLY("", CONST, CONSTRUCTOR, DICT, EXPOSE, FINAL, INHERIT_DOC, INTERFACE, NOSIDEEFFECTS, OVERRIDE, PRIVATE, PROTECTED, STRUCT) {
        protected JsDocTag tagOf(Matcher matcher) {
            return new JsDocTag(matcher.group(1), null, null, null);
        }
    },
    TYPE_ONLY("\\{(.*?)\\}", DEFINE, ENUM, EXTENDS, IMPLEMENTS, LENDS, THIS, TEMPLATE, TYPE, TYPEDEF) {
        protected JsDocTag tagOf(Matcher matcher) {
            return new JsDocTag(matcher.group(1), matcher.group(2), null, null);
        }
    },
    DESC_ONLY("(.*)", DEPRECATED, LICENSE, PRESERVE) {
        protected JsDocTag tagOf(Matcher matcher) {
            return new JsDocTag(matcher.group(1), null, null, matcher.group(2));
        }
    },
    TYPE_AND_DESC("\\{(.*?)\\}\\s*(.*)", RETURN) {
        protected JsDocTag tagOf(Matcher matcher) {
            return new JsDocTag(matcher.group(1), matcher.group(2), null, matcher.group(3));
        }
    },
    FULL("\\{(.*?)\\}\\s*(\\w+)\\s*(.*)", PARAM) {
        protected JsDocTag tagOf(Matcher matcher) {
            return new JsDocTag(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4));
        }
    };

    private final static Pattern NAME = Pattern.compile("@(\\w+)(\\s+.*)?");
    private final Pattern regex;
    private final Set<String> names;

    TagType(String regex, Tag... tags) {
        this.regex = Pattern.compile("@(\\w+)\\s*" + regex);
        this.names = new HashSet<>();
        for (Tag tag : tags) {
            names.add(tag.realName());
        }
    }

    public static JsDocTag tagOf(String line) {
        final Matcher matcher = NAME.matcher(line);
        if (!matcher.matches()) {
            throw new JsDocParserException("Wrong format of line '" + line + "'");
        }
        String name = matcher.group(1);
        TagType type = ofName(name);
        final Matcher correctMatcher = type.regex.matcher(line);
        correctMatcher.find();
        return type.tagOf(correctMatcher);
    }

    protected abstract JsDocTag tagOf(Matcher matcher);

    private static TagType ofName(String name) {
        for (TagType tagType : values()) {
            if (tagType.names.contains(name)) {
                return tagType;
            }
        }
        throw new JsDocParserException("Unknown tag name '" + name + "'");
    }
}
