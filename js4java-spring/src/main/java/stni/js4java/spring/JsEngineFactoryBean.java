package stni.js4java.spring;

import org.springframework.beans.factory.FactoryBean;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 *
 */
public class JsEngineFactoryBean implements FactoryBean<ScriptEngine> {
    private final ScriptEngineManager manager = new ScriptEngineManager();
    private final ScriptEngine js = manager.getEngineByExtension("js");

    @Override
    public ScriptEngine getObject() throws Exception {
        return js;
    }

    @Override
    public Class<? extends ScriptEngine> getObjectType() {
        return ScriptEngine.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
