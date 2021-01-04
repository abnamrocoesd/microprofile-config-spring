package com.abnamro.coesd.spring;

import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Named;

@Named
@ConfigProperties(prefix = "com.abnamro")
public class ConfigPropertiesBean {

    private String greeting;

    @ConfigProperty(name = "key")
    private String keyName;

    @ConfigProperty(defaultValue = "off")
    private String enabled;

    public String getGreeting() {
        return greeting;
    }

    public String getKeyName() {
        return keyName;
    }

    public String getEnabled() {
        return enabled;
    }
}
