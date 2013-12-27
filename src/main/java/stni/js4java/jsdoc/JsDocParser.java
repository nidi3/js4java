package stni.js4java.jsdoc;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 *
 */
public interface JsDocParser {
    List<JsDoc> parseJsDoc(Reader in) throws IOException;
}
