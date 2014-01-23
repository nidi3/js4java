package stni.js4java.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;
import stni.js4java.runtime.Implementer;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;


/**
 *
 */
public abstract class AbstractJsBeanCreator implements BeanFactoryPostProcessor {
    private Map<Class<?>, Resource> services;
    private String encoding = "utf-8";

    protected abstract Implementer createImplementer() throws ScriptException, IOException;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            final Implementer implementer = createImplementer();
            beforeCode();
            for (Resource impl : services.values()) {
                implementer.getEngine().eval(new InputStreamReader(impl.getInputStream(), encoding));
            }
            afterCode();
            for (Class<?> iface : services.keySet()) {
                beanFactory.registerSingleton(iface.getName(), implementer.implementBy(iface, iface.getSimpleName()));
            }
        } catch (ScriptException | IOException e) {
            throw new BeanCreationException("Problem instantiating js beans", e);
        }
    }

    protected void beforeCode() throws ScriptException {
    }

    protected void afterCode() throws ScriptException {
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setServices(Map<Class<?>, Resource> services) {
        this.services = services;
    }
}
