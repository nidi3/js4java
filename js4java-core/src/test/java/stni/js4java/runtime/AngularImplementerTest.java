package stni.js4java.runtime;

import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class AngularImplementerTest {
    interface Str {
        String sub(String s, int from, int to);
    }

    interface Types {
        int test(boolean b, int i, long l, float f, double d);

        Integer testBox(Boolean b, Integer i, Long l, Float f, Double d);

        int testObj(Holder h);
    }

    public static class Holder {
        public Boolean b;
        public Integer i;
        public Long l;
        public Float f;
        public Double d;

        public Holder(Boolean b, Integer i, Long l, Float f, Double d) {
            this.b = b;
            this.i = i;
            this.l = l;
            this.f = f;
            this.d = d;
        }

        @Override
        public String toString() {
            return "Holder{" +
                    "b=" + b +
                    ", i=" + i +
                    ", l=" + l +
                    ", f=" + f +
                    ", d=" + d +
                    '}';
        }
    }

    public static class IntHolder {
        public Integer value;

        public IntHolder(int value) {
            this.value = value;
        }
    }

    @Test
    public void simple() throws Exception {
        assertSimple(new InputStreamReader(getClass().getResourceAsStream("angular-1.2.6.js"), "utf-8"));
        assertSimple(new InputStreamReader(getClass().getResourceAsStream("angular-1.2.6.min.js"), "utf-8"));
    }

    @Test
    public void boxing() throws IOException, ScriptException {
        final AngularImplementer ai = start(new InputStreamReader(getClass().getResourceAsStream("angular-1.2.6.min.js"), "utf-8"));
        ai.module("util");
        ai.service("util", "types", "" +
                "this.test=function(b,i,l,f,d){a=(i+1)+(l+1)+(f+1)+(d+1); return !b ? a : 2*a;};" +
                "this.testBox=function(b,i,l,f,d){a=(i+1)+(l+1)+(f+1)+(d+1); return !b ? a : 2*a;};" +
                "this.testObj=function(h){println(h.i+1); a=(h.i+1)+(h.l+1)+(h.f+1)+(h.d+1); return !h.b ? a : 2*a;};");
        ai.bootstrapDefault("util");
        final Types types = ai.implementBy(Types.class, "types");
        assertEquals(28, types.test(true, 1, 2, 3, 4));
        assertEquals(14, types.test(false, 1, 2, 3, 4));
        assertEquals(Integer.valueOf(28), types.testBox(true, 1, 2L, 3F, 4D));
        assertEquals(Integer.valueOf(14), types.testBox(false, 1, 2L, 3F, 4D));
        assertEquals(28, types.testObj(new Holder(true, 1, 2L, 3F, 4D)));
        assertEquals(14, types.testObj(new Holder(false, 1, 2L, 3F, 4D)));
    }

    private void assertSimple(Reader angular) throws Exception {
        final AngularImplementer ai = start(angular);
        ai.module("util");
        ai.service("util", "str", "this.sub=function(s,a,b){return s.substring(a,b);};");
        ai.bootstrap("app", "util");
        ai.bootstrapDefault("util");
        final Str appDemo = ai.implementBy(Str.class, "app.str");
        final Str defaultDemo = ai.implementBy(Str.class, "str");
        assertEquals("23", appDemo.sub("1234", 1, 3));
        assertEquals("23", defaultDemo.sub("1234", 1, 3));
    }

    private AngularImplementer start(Reader angular) throws IOException, ScriptException {
        final ScriptEngineManager manager = new ScriptEngineManager();
        final ScriptEngine js = manager.getEngineByExtension("js");
        return new AngularImplementer(js,new DataJavaToJsConverter(), angular);
    }
}
