package stni.js4java.runtime;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 *
 */
public interface Implementer {
    ScriptEngine getEngine();

    <T> T implementBy(Class<T> clazz, String obj) throws ScriptException;
}
