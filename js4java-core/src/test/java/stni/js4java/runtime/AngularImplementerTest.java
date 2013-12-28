package stni.js4java.runtime;

import org.junit.Test;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class AngularImplementerTest {
    interface Demo {
        String sub(String s, Integer from, int to);

        boolean test(Data d);
    }

   public static class Data {
       public Boolean value;

       public Data(boolean value) {
           this.value = value;
       }
   }

    @Test
    public void simple() throws Exception {
        assertSimple(new InputStreamReader(getClass().getResourceAsStream("angular-1.2.6.js"), "utf-8"));
        assertSimple(new InputStreamReader(getClass().getResourceAsStream("angular-1.2.6.min.js"), "utf-8"));
    }

    private void assertSimple(Reader angular) throws Exception {
        final ScriptEngineManager manager = new ScriptEngineManager();
        final ScriptEngine js = manager.getEngineByExtension("js");
        final AngularImplementer ai = new AngularImplementer(js, angular);
        ai.module("util");
        ai.service("util", "str", "this.sub=function(s,a,b){return s.substring(a,b);}; this.test=function(data){println(data.value);return !data.value;};");
        ai.bootstrap("app", "util");
        ai.bootstrapDefault("util");
        final ScriptContext context = js.getContext();
        final Demo appDemo = ai.implementBy(Demo.class, "app.str");
        final Demo defaultDemo = ai.implementBy(Demo.class, "str");
        System.out.println(appDemo.test(new Data(false)));
        assertEquals("23", appDemo.sub("1234", 1, 3));
        assertEquals("23", defaultDemo.sub("1234", 1, 3));
    }
}
