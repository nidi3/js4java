package stni.js4java.jsdoc;


import java.util.*;

import static stni.js4java.jsdoc.Tag.RETURN;
import static stni.js4java.jsdoc.Tag.RETURNS;

/**
 *
 */
public class JsDoc implements Iterable<List<JsDocTag>> {
    private final String description;
    private final Map<String, List<JsDocTag>> tags;
    private final JsDocedElement element;

    public JsDoc(String description, List<JsDocTag> tagList, JsDocedElement element) {
        this.description = description;
        this.element = element;
        tags = new HashMap<>();
        for (JsDocTag tag : tagList) {
            List<JsDocTag> existing = tags.get(tag.getName());
            if (existing == null) {
                existing = new ArrayList<>(1);
                tags.put(tag.getName(), existing);
            }
            existing.add(tag);
        }
    }

    public String getDescription() {
        return description;
    }

    public JsDocTag getTag(String name) {
        final List<JsDocTag> tag = tags.get(name);
        return tag == null ? null : tag.get(0);
    }

    public List<JsDocTag> getTags() {
        List<JsDocTag> res = new ArrayList<>();
        for (List<JsDocTag> tagList : tags.values()) {
            res.addAll(tagList);
        }
        return res;
    }

    public List<JsDocTag> getTags(String name) {
        final List<JsDocTag> tag = tags.get(name);
        return tag == null ? Collections.<JsDocTag>emptyList() : tag;
    }

    public JsDocTag getTag(Tag name) {
        return getTag(name.realName());
    }

    public JsDocTag getReturnTag() {
        final JsDocTag tag = getTag(RETURN);
        return tag != null ? tag : getTag(RETURNS);
    }

    public List<JsDocTag> getTags(Tag name) {
        return getTags(name.realName());
    }

    public JsDocedElement getElement() {
        return element;
    }

    @Override
    public Iterator<List<JsDocTag>> iterator() {
        return tags.values().iterator();
    }

    public int size() {
        return tags.size();
    }
}
