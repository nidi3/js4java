package stni.js4java.runtime;

import javax.script.Invocable;
import javax.script.ScriptEngine;

/**
 *
 */
public class SimpleImplementer implements Implementer {
    private final ScriptEngine engine;

    public SimpleImplementer(ScriptEngine engine) {
        this.engine = engine;
    }

    @Override
    public <T> T implementBy(Class<T> clazz, String obj) {
        final Invocable invocable = (Invocable) engine;
        return invocable.getInterface(engine.get(obj), clazz);
    }
}
