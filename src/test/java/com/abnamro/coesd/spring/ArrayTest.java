package com.abnamro.coesd.spring;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;
import java.util.List;
import java.util.Set;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ConfigPropertyBean.class)
@ContextConfiguration(classes = {MPConfigProducer.class})
public class ArrayTest {

    @Autowired
    Config config;

    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalues")
    private URL[] urls;

    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalues")
    private List<URL> urlList;

    @ConfigProperty(name = "tck.config.test.javaconfig.converter.none")
    private List<URL> emptyList;

    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalues")
    private Set<URL> urlSet;

    @Test
    public void verifyArray() {
        Assertions.assertEquals(3, urls.length);
        Assertions.assertEquals(3, urlList.size());
        Assertions.assertEquals(2, urlSet.size());
        Assertions.assertEquals(0, emptyList.size());
    }

}