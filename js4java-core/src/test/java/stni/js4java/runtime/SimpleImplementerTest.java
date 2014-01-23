package stni.js4java.runtime;

import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class SimpleImplementerTest {
    public static class TestParam {
        public Boolean boolProp;
        private boolean boolGet;
        public int intValue;
        public Integer integerValue;
        private String s;
        public Date date;
        public TestParam self;

        public TestParam(Boolean boolProp, boolean boolGet, int intValue, Integer integerValue, String s, Date date) {
            this.boolProp = boolProp;
            this.boolGet = boolGet;
            this.intValue = intValue;
            this.integerValue = integerValue;
            this.s = s;
            this.date = date;
            self = this;
        }

        public boolean isBoolGet() {
            return boolGet;
        }

        public String getS() {
            return s;
        }

        public void method() {
        }
    }

    interface Simple {
        String sub(String s, int from, int to);
    }

    interface Input {
        String test(TestParam p);
    }

    interface Output {
        TestParam test(String s);
    }

    private final ScriptEngineManager manager = new ScriptEngineManager();
    private final ScriptEngine js = manager.getEngineByExtension("js");

    @Test
    public void noConverter() throws ScriptException {
        js.eval("var demo={sub:function(s,from,to){return s.substring(from,to);} };");

        final Implementer impl = new SimpleImplementer(js, null);
        final Simple demo = impl.implementBy(Simple.class, "demo");
        assertEquals("bc", demo.sub("abcd", 1, 3));
    }

    @Test
    public void directConverter() throws ScriptException {
        js.eval("var demo={sub:function(s,from,to){return s.substring(from,to);} };");

        final Implementer impl = new SimpleImplementer(js, new DirectJavaToJsConverter());
        final Simple demo = impl.implementBy(Simple.class, "demo");
        assertEquals("bc", demo.sub("abcd", 1, 3));
    }

    @Test
    public void dataConverter() throws ScriptException {
        js.eval("var demo={test:function(t){return t.method+' '+!!t.boolProp+' '+!!t.boolGet+' '+(t.intValue-1)+' '+(t.integerValue-1)+' '+t.s+' '+t.date.getTime();} };");

        final Implementer impl = new SimpleImplementer(js, new DataJavaToJsConverter());
        final Input demo = impl.implementBy(Input.class, "demo");
        final Date date = new Date(100, 2, 2);
        assertEquals("undefined true true 0 1 w " + date.getTime(), demo.test(new TestParam(true, true, 1, 2, "w", date)));
        assertEquals("undefined false true 0 1 w " + date.getTime(), demo.test(new TestParam(false, true, 1, 2, "w", date)));
    }
//    @Test
//    public void dataConverter() throws ScriptException {
//        js.eval("var demo={testReturn:function(testParam){return {s:testParam.method+' '+!!testParam.bool};}, sub:function(s,from,to){return s.substring(from,to);} };");
//
//        final Implementer impl = new SimpleImplementer(js, new DataJavaToJsConverter());
//        final Demo demo = impl.implementBy(Demo.class, "demo");
//        assertEquals("bc", demo.sub("abcd", 1, 3));
//        assertEquals("true", demo.test(new TestParam(true)));
//        assertEquals("false", demo.test(new TestParam(false)));
//    }
}
