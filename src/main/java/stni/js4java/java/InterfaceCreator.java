package stni.js4java.java;

import stni.js4java.jsdoc.JsDoc;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 *
 */
public interface InterfaceCreator {
    void createInterface(List<JsDoc> jsDocs, String packge, Writer out) throws IOException;
}
