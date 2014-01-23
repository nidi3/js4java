package stni.js4java.spring;

import stni.js4java.runtime.Implementer;
import stni.js4java.runtime.JavaToJsConverter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;

/**
 *
 */
public abstract class AbstractImplementerFactoryBean<T extends Implementer> extends AbstractJsFactoryBean {
    protected ScriptEngine engine;
    protected JavaToJsConverter javaToJsConverter;
    private T implementer;

    protected abstract T doCreateImplementer() throws IOException, ScriptException;

    protected T createImplementer() throws IOException, ScriptException {
        if (engine == null) {
            engine = new ScriptEngineManager().getEngineByExtension("js");
        }
        implementer = doCreateImplementer();
        return implementer;
    }

    public T getImplementer() {
        return implementer;
    }

    public void setEngine(ScriptEngine engine) {
        this.engine = engine;
    }

    public void setJavaToJsConverter(JavaToJsConverter javaToJsConverter) {
        this.javaToJsConverter = javaToJsConverter;
    }
}
