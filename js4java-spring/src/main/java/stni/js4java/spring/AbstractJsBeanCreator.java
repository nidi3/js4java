package stni.js4java.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import stni.js4java.runtime.Implementer;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 *
 */
public abstract class AbstractJsBeanCreator implements BeanFactoryPostProcessor {
    private Map<Class<?>, Resource> services;
    private String serviceBasePackage;
    private String encoding = "utf-8";

    protected abstract Implementer createImplementer() throws ScriptException, IOException;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            if (services == null && serviceBasePackage == null) {
                throw new BeanCreationException("services and serivceBasePackage cannot be both null");
            }
            if (serviceBasePackage != null) {
                if (services == null) {
                    services = new HashMap<>();
                }
                findServices();
            }
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

    private void findServices() throws IOException {
        final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        final String basePath = serviceBasePackage.replace(".", "/");
        for (Resource classFile : resolver.getResources("classpath:" + basePath + "/**/*.class")) {
            final String absolutePath = classFile.getFile().getAbsolutePath();
            final int pos = absolutePath.indexOf(basePath);
            try {
                final String serviceClassName = absolutePath.substring(pos, absolutePath.length() - 6);
                final Resource js = resolver.getResource("classpath:" + serviceClassName + ".js");
                if (js.exists()) {
                    final Class<?> serviceClass = Class.forName(serviceClassName.replace('/', '.'));
                    if (serviceClass.isInterface()) {
                        services.put(serviceClass, js);
                    }
                }
            } catch (ClassNotFoundException e) {
                //ignore
            }
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

    public void setServiceBasePackage(String serviceBasePackage) {
        this.serviceBasePackage = serviceBasePackage;
    }
}
