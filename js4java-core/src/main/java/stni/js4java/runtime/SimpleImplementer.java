package stni.js4java.runtime;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 *
 */
public class SimpleImplementer extends AbstractImplementer {

    public SimpleImplementer(ScriptEngine engine, JavaToJsConverter javaToJsConverter) throws ScriptException {
        super(engine, javaToJsConverter);
    }

    @Override
    public <T> T implementBy(Class<T> clazz, String obj) throws ScriptException {
        final Invocable invocable = (Invocable) engine;
        return invocable.getInterface(engine.eval(proxied(obj)), clazz);
    }
}
