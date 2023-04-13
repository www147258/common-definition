package com.yundou.www.common.defintion.util;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yundou.www.common.defintion.exception.JacksonLocalException;

import java.util.Objects;

public class JacksonHelp {

    private static ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();

    static {
        // 赋值
        DEFAULT_OBJECT_MAPPER
                // 设置允许序列化空的实体类（否则会抛出异常）
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                // 设置 把java.util.Date, Calendar输出为数字（时间戳）
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                // 设置在遇到未知属性的时候不抛出异常
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                // 设置数字丢失精度问题
                .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                // 设置没有引号的字段名
                .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
                // 设置允许单引号
                .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
                // 设置接受只有一个元素的数组的反序列化
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }


    private JacksonHelp() {
        throw new UnsupportedOperationException();
    }

    public static <T> T parseObject(String str, Class<T> tClass) {

        return parseObject(DEFAULT_OBJECT_MAPPER , str , tClass);

    }

    public static <T> T parseObject(ObjectMapper objectMapper , String json, Class<T> tClass) {
        if (Objects.isNull(json) || json.length() == 0) {
            return null;
        }
        try {
            return objectMapper.readValue(json, tClass);
        } catch (Exception e) {
            String errMsg = "jackson json转对象失败, json为:" + json + "对象类型为:" + tClass;
            throw new JacksonLocalException(errMsg, e);
        }


    }


    public static String toJsonString(Object o) {
        return toJsonString(DEFAULT_OBJECT_MAPPER , o);
    }

    public static String toJsonString(ObjectMapper objectMapper , Object object) {
        if (Objects.isNull(object)) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new JacksonLocalException(e);
        }
    }

}
