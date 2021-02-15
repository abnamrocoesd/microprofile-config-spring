package com.abnamro.coesd.spring;

import com.google.common.base.MoreObjects;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.config.spi.Converter;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.ResolvableType;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Provider;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Supplier;

public class ConfigPropertyFieldCallback implements ReflectionUtils.FieldCallback {

    private ConfigurableListableBeanFactory configurableBeanFactory;
    private Object bean;

    public ConfigPropertyFieldCallback(ConfigurableListableBeanFactory bf, Object bean) {
        configurableBeanFactory = bf;
        this.bean = bean;
    }

    @Override
    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {

        String prefix = "";
        if (bean.getClass().isAnnotationPresent(ConfigProperties.class)){
            prefix = bean.getClass().getAnnotation(ConfigProperties.class).prefix() + ".";
        } else if (!field.isAnnotationPresent(ConfigProperty.class)) {
            return;
        }

        ReflectionUtils.makeAccessible(field);

        Class<?> clazz = field.getType();
        final ConfigProperty cf = MoreObjects.firstNonNull(field.getDeclaredAnnotation(ConfigProperty.class),
            new ConfigPropertyLiteral() {
                @Override
                public String name() {
                    return null;
                }

                @Override
                public String defaultValue() {
                    return null;
                }
            });

        String name = cf.name();

        if (StringUtils.isEmpty(name)) {
            if (prefix == null) {
                name = field.toString().substring(field.toString().lastIndexOf(" ") + 1);
            } else {
                name = field.getName();
            }
        }
        final String name2 = prefix + name;

        Config config = ConfigProvider.getConfig();

//        Type type = ResolvableType.forField(field).getType();
//        if(type instanceof Class && ConfigValue.class.isAssignableFrom((Class<?>) type)
//            || type instanceof Class && OptionalInt.class.isAssignableFrom((Class<?>) type)
//            || type instanceof ParameterizedType && (Optional.class.isAssignableFrom((Class<?>) ((ParameterizedType) type).getRawType()))) {
//
//        }

        if (clazz.isAssignableFrom(Optional.class)) {
            ResolvableType resolvableType = ResolvableType.forField(field);
            ResolvableType generic = resolvableType.getGeneric(0);
            Optional<?> mpValue = config.getOptionalValue(name, generic.toClass());
            field.set(bean, mpValue);
            return;
        }

        if (clazz.isAssignableFrom(Supplier.class)) {
            ResolvableType resolvableType = ResolvableType.forField(field);
            ResolvableType generic = resolvableType.getGeneric(0);
            Optional<?> mpValue = config.getOptionalValue(name, generic.toClass());
            field.set(bean, (Supplier<Object>) () -> mpValue.get());
            return;
        }
        if (clazz.isAssignableFrom(Provider.class)) {
            ResolvableType resolvableType = ResolvableType.forField(field);
            ResolvableType generic = resolvableType.getGeneric(0);
            Optional<?> mpValue = config.getOptionalValue(name, generic.toClass());
            field.set(bean, (Provider<Object>) () -> mpValue.get());
            return;
        }

        if (clazz.isAssignableFrom(List.class)) {
            ResolvableType resolvableType = ResolvableType.forField(field);
            ResolvableType generic = resolvableType.getGeneric(0);
            Optional<? extends List<?>> mpValue = config.getOptionalValues(name, generic.toClass());
            if (mpValue.isPresent()){
                field.set(bean, mpValue.get());
            } else {
                field.set(bean, Collections.EMPTY_LIST);
            }
            return;
        }
        if (clazz.isAssignableFrom(Set.class)) {
            ResolvableType resolvableType = ResolvableType.forField(field);
            ResolvableType generic = resolvableType.getGeneric(0);
            Optional<? extends List<?>> mpValue = config.getOptionalValues(name, generic.toClass());
            if (mpValue.isPresent()){
                field.set(bean, new HashSet<>(mpValue.get()) );
            } else {
                field.set(bean, Collections.EMPTY_SET);
            }
            return;
        }

        if (clazz.isAssignableFrom(ConfigValue.class)) {
            ConfigValue configValue = config.getConfigValue(name2);
            if (configValue.getValue() == null) {
                configValue = new ConfigValue(){
                    @Override
                    public String getName() {
                        return name2;
                    }

                    @Override
                    public String getValue() {
                        return cf.defaultValue();
                    }

                    @Override
                    public String getRawValue() {
                        return null;
                    }

                    @Override
                    public String getSourceName() {
                        return null;
                    }

                    @Override
                    public int getSourceOrdinal() {
                        return 0;
                    }
                };
            }
            field.set(bean, configValue);
            return;
        }

        Object value;
        Optional<?> mpValue = config.getOptionalValue(name2, clazz);

        if (mpValue.isPresent()) {
            value = mpValue.get();
        } else {
            if (!cf.defaultValue().equals(ConfigProperty.UNCONFIGURED_VALUE)) {
                Optional<? extends Converter<?>> converter = ConfigProvider.getConfig().getConverter(clazz);
                value = converter.get().convert(cf.defaultValue());
            } else {
                throw new IllegalArgumentException("Could not resolve property '" + cf.name() + "'");
            }
        }

        field.set(bean, value);
        return;
    }

    public abstract class ConfigPropertyLiteral extends AnnotationLiteral<ConfigProperty> implements ConfigProperty {

    }
}