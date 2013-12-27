package stni.js4java.jsdoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class DefaultJsDocParser implements JsDocParser {
    private static final Pattern JS_DOC_START = Pattern.compile("^\\s*/\\*\\*");
    private static final Pattern JS_DOC_END = Pattern.compile("\\*/\\s*$");
    private static final Pattern FUNCTION = Pattern.compile("^\\s*function\\s+(\\w+)");
    private static final Pattern VARIABLE = Pattern.compile("^\\s*var\\s+(\\w+)(\\s*=\\s*function)?");
    private static final String END = "*/";
    private static final String START_OF_LINE = "\\s*\\*\\s*";

    @Override
    public List<JsDoc> parseJsDoc(Reader in) throws IOException {
        try (final BufferedReader bufferedIn = new BufferedReader(in)) {
            return parseReader(bufferedIn);
        }
    }

    private List<JsDoc> parseReader(BufferedReader in) throws IOException {
        List<JsDoc> res = new ArrayList<>();
        String line;
        while ((line = in.readLine()) != null) {
            if (isJsDocStart(line)) {
                res.add(parseBlock(in, line));
            }
        }
        return res;
    }

    private boolean isJsDocStart(String line) {
        return JS_DOC_START.matcher(line).find();
    }

    private JsDoc parseBlock(BufferedReader in, String line) throws IOException {
        final int startPos = line.indexOf("/**") + 3;
        final int endPos = line.indexOf("*/");
        if (endPos > 0) {
            return new JsDoc(line.substring(startPos, endPos).trim(), Collections.<JsDocTag>emptyList(), parseElement(line.substring(endPos + 2)));
        }

        List<JsDocTag> tags = new ArrayList<>();
        JsDocTag tag = null;
        String desc = line.substring(startPos).trim();
        while ((line = readLine(in.readLine())) != END) {
            if (line.charAt(0) == '@') {
                tag = parseTag(line);
                tags.add(tag);
            } else if (tag != null) {
                tag.addDescription(line);
            } else {
                desc += " " + line;
            }
        }
        return new JsDoc(desc.trim(), tags, parseElement(in.readLine()));
    }

    private JsDocedElement parseElement(String line) {
        final Matcher func = FUNCTION.matcher(line);
        if (func.find()) {
            return new JsDocedElement(func.group(1), true);
        }
        final Matcher var = VARIABLE.matcher(line);
        if (var.find()) {
            return new JsDocedElement(var.group(1), var.groupCount() > 1 && var.group(2) != null);
        }
        throw new JsDocParserException("Unparsable code '" + line + "'");
    }

    private JsDocTag parseTag(String line) {
        return TagType.tagOf(line.trim());
    }

    private String readLine(String line) {
        if (isJsDocEnd(line)) {
            return END;
        }
        return line.replaceFirst(START_OF_LINE, "");
    }

    private boolean isJsDocEnd(String line) {
        return JS_DOC_END.matcher(line).find();
    }

}
