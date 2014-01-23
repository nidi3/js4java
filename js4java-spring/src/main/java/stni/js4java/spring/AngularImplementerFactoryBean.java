package stni.js4java.spring;

import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import stni.js4java.runtime.AngularImplementer;
import stni.js4java.runtime.Implementer;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 *
 */
public class AngularImplementerFactoryBean extends AbstractImplementerFactoryBean<AngularImplementer> {
    private Map<String, String> modules;
    private Resource angular;

    @Override
    protected AngularImplementer doCreateImplementer() throws IOException, ScriptException {
        return new AngularImplementer(engine, javaToJsConverter, new InputStreamReader(this.angular.getInputStream(), "utf-8"));
    }

    @Override
    protected void beforeCode() throws ScriptException {
        for (Map.Entry<String, String> module : modules.entrySet()) {
            getImplementer().module(module.getKey(), StringUtils.tokenizeToStringArray(module.getValue(), ","));
        }
    }

    @Override
    protected void afterCode() throws ScriptException {
        getImplementer().bootstrapDefault(modules.keySet().toArray(new String[modules.size()]));
    }

    public void setAngular(Resource angular) {
        this.angular = angular;
    }

    public void setModules(Map<String, String> modules) {
        this.modules = modules;
    }
}
