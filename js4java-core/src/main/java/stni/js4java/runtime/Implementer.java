package stni.js4java.runtime;

import javax.script.ScriptException;

/**
 *
 */
public interface Implementer {
    <T> T implementBy(Class<T> clazz, String obj) throws ScriptException;
}
