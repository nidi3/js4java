package stni.js4java.runtime;

import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class SimpleImplementerTest {
    interface Demo {
        String sub(String s, int from, int to);
    }

    @Test
    public void simple() throws ScriptException {
        final ScriptEngineManager manager = new ScriptEngineManager();
        final ScriptEngine js = manager.getEngineByExtension("js");
        js.eval("var demo={sub:function(s,from,to){return s.substring(from,to);} };");

        final Implementer impl = new SimpleImplementer(js);
        final Demo demo = impl.implementBy(Demo.class, "demo");
        assertEquals("bc", demo.sub("abcd", 1, 3));
    }
}
