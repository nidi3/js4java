package stni.js4java.spring;

import stni.js4java.runtime.Implementer;

import javax.script.ScriptException;
import java.io.IOException;


/**
 *
 */
public class JsBeanCreator extends AbstractJsBeanCreator {
    private Implementer implementer;

    @Override
    protected Implementer createImplementer() throws ScriptException, IOException {
        return implementer;
    }

    public void setImplementer(Implementer implementer) {
        this.implementer = implementer;
    }
}
