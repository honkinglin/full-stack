package com.fullstack.reader.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperConfig implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ObjectMapper) {
            ObjectMapper mapper = (ObjectMapper) bean;
            JavaTimeModule module = new JavaTimeModule();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTimeSerializer serializer = new LocalDateTimeSerializer(formatter);
            module.addSerializer(LocalDateTime.class, serializer);
            mapper.registerModule(module);
        }
        return bean;
    }
}
