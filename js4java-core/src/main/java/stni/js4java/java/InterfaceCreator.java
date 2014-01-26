package stni.js4java.java;

import stni.js4java.jsdoc.JsDoc;

import java.util.List;

/**
 *
 */
public interface InterfaceCreator {
    List<InterfaceDescriptor> createInterfaces(List<JsDoc> jsDocs, String sourceName, String targetPackage);
}
