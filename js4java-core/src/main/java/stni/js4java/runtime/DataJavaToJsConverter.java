package stni.js4java.runtime;

/**
 *
 */
public class DataJavaToJsConverter implements JavaToJsConverter{
    @Override
    public String converterFunction() {
        return Utils.loadResource("dataJavaToJsConverter.js");
    }
}
