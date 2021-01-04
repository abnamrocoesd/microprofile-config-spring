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

import javax.inject.Provider;
import java.util.function.Supplier;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ConfigPropertyBean.class)
@ContextConfiguration(classes = {MPConfigConfiguration.class})
public class ProviderTest {

    @Autowired
    Config config;

    @ConfigProperty(name="tck.config.test.javaconfig.configvalue.long", defaultValue="100")
    private Provider<Long> timeout;


    @ConfigProperty(name="tck.config.test.javaconfig.configvalue.long", defaultValue="100")
    private Supplier<Long> timeout2;

    @Test
    public void verifyProviderSupplier() {
        Assertions.assertEquals(1234567890123456L, timeout.get());
        Assertions.assertEquals(1234567890123456L, timeout2.get());
    }

}