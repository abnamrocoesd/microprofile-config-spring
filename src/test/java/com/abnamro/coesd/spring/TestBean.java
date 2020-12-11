package com.abnamro.coesd.spring;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public class TestBean {

    @ConfigProperty(name = "greeting")
    private String greeting;

    public String getGreeting() {
        return greeting;
    }

}
