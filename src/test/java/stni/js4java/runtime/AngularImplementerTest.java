package stni.js4java.runtime;

import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class AngularImplementerTest {
    interface Demo {
        String sub(String s, int from, int to);
    }

    @Test
    public void simple() throws Exception {
        final ScriptEngineManager manager = new ScriptEngineManager();
        final ScriptEngine js = manager.getEngineByExtension("js");
        final AngularImplementer ai = new AngularImplementer(js, new InputStreamReader(getClass().getResourceAsStream("angular-1.2.6.js"), "utf-8"));
        ai.module("util");
        ai.service("util", "str", "this.sub=function(s,a,b){return s.substring(a,b);}");
        ai.bootstrap("app", "util");
        ai.bootstrapDefault("util");
        final Demo appDemo = ai.implementBy(Demo.class, "app.str");
        final Demo defaultDemo = ai.implementBy(Demo.class, "str");
        assertEquals("23", appDemo.sub("1234", 1, 3));
        assertEquals("23", defaultDemo.sub("1234", 1, 3));
    }
}
