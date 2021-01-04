package com.abnamro.coesd.spring;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ConfigPropertiesBean.class})
@ContextConfiguration(classes = {MPConfigConfiguration.class})
public class ConfigPropertiesTest {

    @Inject
    ConfigPropertiesBean configPropertiesBean;

    @Test
    public void verifyArray() {
        Assertions.assertEquals("off", configPropertiesBean.getEnabled());
        Assertions.assertEquals("myKey", configPropertiesBean.getKeyName());
        Assertions.assertEquals("doei", configPropertiesBean.getGreeting());
    }

}