package com.abnamro.coesd.spring;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.config.spi.Converter;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.enterprise.inject.Instance;
import javax.inject.Provider;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Optional;

public class ConfigPropertyFieldCallback implements ReflectionUtils.FieldCallback {

    private ConfigurableListableBeanFactory configurableBeanFactory;
    private Object bean;

    public ConfigPropertyFieldCallback(ConfigurableListableBeanFactory bf, Object bean) {
        configurableBeanFactory = bf;
        this.bean = bean;
    }

    @Override
    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        if (!field.isAnnotationPresent(ConfigProperty.class)) {
            return;
        }
        ReflectionUtils.makeAccessible(field);
//        Type fieldGenericType = field.getGenericType();
        Class<?> generic = field.getType();
        ConfigProperty cf = field.getDeclaredAnnotation(ConfigProperty.class);
        String name = cf.name();

        if (StringUtils.isEmpty(name)) {
            name = field.toString().substring(field.toString().lastIndexOf(" ") + 1);
        }
        final String name2 = name;

        if (generic.isAssignableFrom(String.class)) {
            Config config = ConfigProvider.getConfig();
            field.set(bean, config.getOptionalValue(name, String.class)
                    .orElse(cf.defaultValue()));
        }
        if (generic.isAssignableFrom(long.class) || generic.isAssignableFrom(Long.class)) {
            Config config = ConfigProvider.getConfig();
            field.set(bean, config.getOptionalValue(name, Long.class)
                    .orElse(Long.getLong(cf.defaultValue())));
        }
        if (generic.isAssignableFrom(Optional.class)) {
            Config config = ConfigProvider.getConfig();
            field.set(bean, config.getOptionalValue(name, String.class));
            //defaultValue is not honoured
        }
        if (generic.isAssignableFrom(Provider.class)) {
            Config config = ConfigProvider.getConfig();
            field.set(bean, (Provider) () -> config.getOptionalValue(name2, String.class)
                        .orElse(cf.defaultValue())
                );
        }



//        if (genericTypeIsValid(classValue, fieldGenericType)) {
//            String beanName = classValue.getSimpleName() + generic.getSimpleName();
//            Object beanInstance = getBeanInstance(beanName, generic, classValue);
//            field.set(bean, beanInstance);
//        } else {
//            throw new IllegalArgumentException(ERROR_ENTITY_VALUE_NOT_SAME);
//        }
    }

    <T> Converter<T> resolveConverter(Class<T> clazz) {
        return null;
    }

//    public boolean genericTypeIsValid(Class<?> clazz, Type field) {
//        if (field instanceof ParameterizedType) {
//            ParameterizedType parameterizedType = (ParameterizedType) field;
//            Type type = parameterizedType.getActualTypeArguments()[0];
//
//            return type.equals(clazz);
//        } else {
//            logger.warn(WARN_NON_GENERIC_VALUE);
//            return true;
//        }
//    }
//
//    public Object getBeanInstance(            String beanName, Class<?> genericClass, Class<?> paramClass) {
//        Object daoInstance = null;
//        if (!configurableBeanFactory.containsBean(beanName)) {
//            logger.info("Creating new DataAccess bean named '{}'.", beanName);
//
//            Object toRegister = null;
//            try {
//                Constructor<?> ctr = genericClass.getConstructor(Class.class);
//                toRegister = ctr.newInstance(paramClass);
//            } catch (Exception e) {
//                logger.error(ERROR_CREATE_INSTANCE, genericClass.getTypeName(), e);
//                throw new RuntimeException(e);
//            }
//
//            daoInstance = configurableBeanFactory.initializeBean(toRegister, beanName);
//            configurableBeanFactory.autowireBeanProperties(daoInstance, AUTOWIRE_MODE, true);
//            configurableBeanFactory.registerSingleton(beanName, daoInstance);
//            logger.info("Bean named '{}' created successfully.", beanName);
//        } else {
//            daoInstance = configurableBeanFactory.getBean(beanName);
//            logger.info(
//                    "Bean named '{}' already exists used as current bean reference.", beanName);
//        }
//        return daoInstance;
//    }
}