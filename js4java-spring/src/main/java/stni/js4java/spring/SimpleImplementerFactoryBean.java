package stni.js4java.spring;

import stni.js4java.runtime.SimpleImplementer;

import javax.script.ScriptException;

/**
 *
 */
public class SimpleImplementerFactoryBean extends AbstractImplementerFactoryBean<SimpleImplementer> {
    @Override
    protected SimpleImplementer doCreateImplementer() throws ScriptException {
        return new SimpleImplementer(engine, javaToJsConverter);
    }
}
