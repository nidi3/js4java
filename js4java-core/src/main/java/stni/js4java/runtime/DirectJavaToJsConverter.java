package stni.js4java.runtime;

/**
 *
 */
public class DirectJavaToJsConverter implements JavaToJsConverter{
    @Override
    public String converterFunction() {
        return "function(java){ return java; }";
    }
}
