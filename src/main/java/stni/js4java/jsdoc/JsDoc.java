package stni.js4java.jsdoc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class JsDoc implements Iterable<JsDocTag> {
    private final String description;
    private final Map<String, JsDocTag> tags;
    private final JsDocedElement element;

    public JsDoc(String description, List<JsDocTag> tagList, JsDocedElement element) {
        this.description = description;
        this.element = element;
        tags = new HashMap<>();
        for (JsDocTag tag : tagList) {
            tags.put(tag.getName(), tag);
        }
    }

    public String getDescription() {
        return description;
    }

    public JsDocTag getTag(String name) {
        return tags.get(name);
    }

    public JsDocedElement getElement() {
        return element;
    }

    @Override
    public Iterator<JsDocTag> iterator() {
        return tags.values().iterator();
    }

    public int size() {
        return tags.size();
    }
}
