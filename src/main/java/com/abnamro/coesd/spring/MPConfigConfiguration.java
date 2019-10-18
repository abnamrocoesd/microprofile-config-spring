package com.abnamro.coesd.spring;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.abnamro.coesd.spring"})
@ConditionalOnClass(ConfigProvider.class)
public class MPConfigConfiguration {

    @Bean
    Config config() {
        return ConfigProvider.getConfig();
    }
}