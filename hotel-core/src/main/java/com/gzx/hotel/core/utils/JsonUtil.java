package com.gzx.hotel.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil {

    private ObjectMapper objectMapper;

    @Autowired
    public JsonUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public <T> T fromJson(String str, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(str, clazz);
    }
}
