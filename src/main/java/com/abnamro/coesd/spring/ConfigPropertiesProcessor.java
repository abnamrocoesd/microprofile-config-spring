package com.abnamro.coesd.spring;

import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConfigPropertiesProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        Map<String, Object> ConfigPropertiesBeans = configurableListableBeanFactory.getBeansWithAnnotation(ConfigProperties.class);
        ConfigPropertiesBeans.forEach((k, v) -> {
            BeanDefinition beanDef = configurableListableBeanFactory.getBeanDefinition(k);
            ((DefaultListableBeanFactory) configurableListableBeanFactory)
                    .removeBeanDefinition(k);

            ((DefaultListableBeanFactory) configurableListableBeanFactory)
                    .registerBeanDefinition(k, beanDef);
        });
    }
}
