package stni.js4java.spring;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleJsFactoryBeanTest {
    @Autowired
    private AddService addService;

    @Test
    public void runService() {
        Assert.assertEquals(42, addService.add(23, 19));
    }
}
