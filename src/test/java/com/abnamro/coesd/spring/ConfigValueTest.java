package com.abnamro.coesd.spring;

import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ConfigPropertyBean.class})
@ContextConfiguration(classes = {MPConfigProducer.class})
public class ConfigValueTest {

    @ConfigProperty(name = "tck.config.test.javaconfig.properties.key1")
    ConfigValue cfgValue;

    @ConfigProperty(name = "tck.config.test.javaconfig.properties.none")
    ConfigValue unKnownCfgValue;

    @ConfigProperty(name = "tck.config.test.javaconfig.properties.none", defaultValue = "value")
    ConfigValue defaultCfgValue;


    @Test
    public void verifyArray() {
        Assertions.assertEquals("Value1", cfgValue.getValue());
        Assertions.assertEquals("value", defaultCfgValue.getValue());
        Assertions.assertNotNull(unKnownCfgValue);
    }

}