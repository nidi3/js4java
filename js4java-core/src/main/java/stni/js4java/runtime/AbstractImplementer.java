package stni.js4java.runtime;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 *
 */
public abstract class AbstractImplementer implements Implementer {
    protected final ScriptEngine engine;
    protected final JavaToJsConverter javaToJsConverter;

    protected AbstractImplementer(ScriptEngine engine, JavaToJsConverter javaToJsConverter) throws ScriptException {
        this.engine = engine;
        this.javaToJsConverter = javaToJsConverter;
        String proxyFunc = "";
        if (javaToJsConverter != null) {
            proxyFunc = "var convert=" + javaToJsConverter.converterFunction() + ";\n" + Utils.loadResource("javaToJsProxy.js");
        }
        engine.eval("var $addProxies=function(obj){" + proxyFunc + " return obj; };");
    }


    protected String proxied(String jsObj) {
        return "$addProxies(" + jsObj + ")";
    }
}
