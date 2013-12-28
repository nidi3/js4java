package stni.js4java.runtime;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 *
 */
public class AngularImplementer implements Implementer {
    private static final String DEFAULT_MODULE = "$default";
    private final ScriptEngine engine;

    public AngularImplementer(ScriptEngine engine, Reader angular) throws IOException, ScriptException {
        engine.eval("window=(function(){return this;}());" +
                "document={" +
                "  addEventListener:function(){}," +
                "  createElement:function(){" +
                "    return {" +
                "      setAttribute:function(){}," +
                "      pathname:'xxx'," +
                "      removeChild:function(){}" +
                "    };" +
                "  }," +
                "  getElementsByTagName:function(){}" +
                "};" +
                "addEventListener=function(){};" +
                "location={href:'http://my.com/path',protocol:'http'};" +
                "setTimeout=function(){};" +
                "navigator={};");
        engine.eval(fixIncompatabilities(angular));
        this.engine = engine;
    }

    private String fixIncompatabilities(Reader angular) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(angular)) {
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line.replaceAll("(\\W)int\\(", "$1toInt(").replace("short:", "'short':")).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public <T> T implementBy(Class<T> clazz, String appAndService) throws ScriptException {
        final Invocable invocable = (Invocable) engine;
        final int pos = appAndService.indexOf('.');
        String app, service;
        if (pos < 0) {
            app = DEFAULT_MODULE;
            service = appAndService;
        } else {
            app = appAndService.substring(0, pos);
            service = appAndService.substring(pos + 1);
        }
        return invocable.getInterface(engine.eval(app + ".get('" + service + "')"), clazz);
    }

    public void bootstrapDefault(String... modules) throws ScriptException {
        bootstrap(DEFAULT_MODULE, modules);
    }

    public void bootstrap(String target, String... modules) throws ScriptException {
        engine.eval(target + "=angular.bootstrap({addEventListener:function(){}},[" + join(modules) + "]);");
    }

    public void module(String name, String... dependencies) throws ScriptException {
        engine.eval("angular.module('" + name + "',[" + join(dependencies) + "]);");
    }

    public void service(String module, String name, String code) throws ScriptException {
        engine.eval("angular.module('" + module + "').service('" + name + "', function(){" + code + "});");
    }

    private String join(String... ss) {
        String res = "";
        boolean first = true;
        for (String s : ss) {
            if (first) {
                first = false;
            } else {
                res += ",";
            }
            res += "'" + s + "'";
        }
        return res;
    }
}
