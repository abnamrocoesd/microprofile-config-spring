package com.abnamro.coesd.spring;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Optional;

public class ConfigPropertyFieldCallback implements ReflectionUtils.FieldCallback {

    private static int AUTOWIRE_MODE = AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;

    private static String ERROR_ENTITY_VALUE_NOT_SAME = "@DataAccess(entity) value should have same type with injected generic type.";
    private static String WARN_NON_GENERIC_VALUE = "@DataAccess annotation assigned to raw (non-generic) declaration. This will make your code less type-safe.";
    private static String ERROR_CREATE_INSTANCE = "Cannot create instance of type '{}' or instance creation is failed because: {}";

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
        Type fieldGenericType = field.getGenericType();
        // In this example, get actual "GenericDAO' type.
        Class<?> generic = field.getType();
        String name = field.getDeclaredAnnotation(ConfigProperty.class).name();

        if (StringUtils.isEmpty(name)) {
            name = field.toString().substring(field.toString().lastIndexOf(" ") + 1);
        }
        if (generic.isAssignableFrom(String.class)) {
            Config config = ConfigProvider.getConfig();
            field.set(bean, config.getValue(name, String.class));
        }
        if (generic.isAssignableFrom(Optional.class)) {
            Config config = ConfigProvider.getConfig();
            field.set(bean, config.getOptionalValue(name, String.class));
        }


//        if (genericTypeIsValid(classValue, fieldGenericType)) {
//            String beanName = classValue.getSimpleName() + generic.getSimpleName();
//            Object beanInstance = getBeanInstance(beanName, generic, classValue);
//            field.set(bean, beanInstance);
//        } else {
//            throw new IllegalArgumentException(ERROR_ENTITY_VALUE_NOT_SAME);
//        }
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