package com.office.oa.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.time.LocalDateTime;

public class JacksonConfig {

    // 创建 ObjectMapper 实例并注册自定义序列化和反序列化器
    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 注册自定义序列化和反序列化器
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        objectMapper.registerModule(module);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 禁用时间戳格式
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // 忽略值为 null 的属性

        return objectMapper;
    }
}
