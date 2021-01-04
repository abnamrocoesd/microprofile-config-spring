package com.abnamro.coesd.spring;

import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Optional;
import java.util.OptionalLong;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ConfigPropertyBean.class)
@ContextConfiguration(classes = {MPConfigConfiguration.class})
public class MPConfigProcessorTest {

    @Autowired
    ConfigPropertyBean configPropertyBean;

    @Autowired
    Config config;

    @ConfigProperty(name = "test")
    private Optional<CustomObject> test;


    @ConfigProperty(name = "number")
    private OptionalLong test2;

    @BeforeClass
    public static void setup() {
        System.setProperty("before", "env");
        TomcatURLStreamHandlerFactory.getInstance();
    }

    @Test
    public void verifyConfigPropertyInjection() {
        Assertions.assertEquals("hallo", configPropertyBean.getGreeting());
    }

    @Test
    public void verifyManualConfig() throws IOException {
        config.getConfigSources().forEach(cs -> {
            System.out.println(cs.getName());
        });
        Assertions.assertEquals("hallo", config.getValue("greeting", String.class));
        Assertions.assertEquals("env", config.getValue("before", String.class));
        Assertions.assertEquals(1L, test2.getAsLong());
    }

}