package com.chen.intellectualproperty.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * JSON 工具类，基于 Jackson
 */
public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // 忽略未知字段
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许空对象
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 支持 Java 8 时间类型
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    // ==================== 对象 → JSON 字符串 ====================

    /**
     * 对象转 JSON 字符串
     */
    public static String toJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON serialization failed", e);
        }
    }

    /**
     * 对象转 JSON 字符串（格式化输出）
     */
    public static String toPrettyJson(Object obj) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON serialization failed", e);
        }
    }

    /**
     * 对象转 JSON 字符串，失败返回 null
     */
    public static String toJsonOrNull(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 对象转 JSON byte 数组
     */
    public static byte[] toJsonBytes(Object obj) {
        try {
            return MAPPER.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON serialization failed", e);
        }
    }

    // ==================== JSON 字符串 → 对象 ====================

    /**
     * JSON 字符串转对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON deserialization failed", e);
        }
    }

    /**
     * JSON 字符串转对象，失败返回 null
     */
    public static <T> T fromJsonOrNull(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * JSON byte 数组转对象
     */
    public static <T> T fromJson(byte[] jsonBytes, Class<T> clazz) {
        try {
            return MAPPER.readValue(jsonBytes, clazz);
        } catch (IOException e) {
            throw new RuntimeException("JSON deserialization failed", e);
        }
    }

    /**
     * JSON 输入流转对象
     */
    public static <T> T fromJson(InputStream inputStream, Class<T> clazz) {
        try {
            return MAPPER.readValue(inputStream, clazz);
        } catch (IOException e) {
            throw new RuntimeException("JSON deserialization failed", e);
        }
    }

    // ==================== JSON 字符串 → 泛型对象 ====================

    /**
     * JSON 字符串转泛型对象（如 List、Map 等）
     * <pre>
     *   List<User> list = JsonUtils.fromJson(json, new TypeReference&lt;List&lt;User&gt;&gt;(){});
     *   Map<String, User> map = JsonUtils.fromJson(json, new TypeReference&lt;Map&lt;String, User&gt;&gt;(){});
     * </pre>
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON deserialization failed", e);
        }
    }

    // ==================== JSON 字符串 → List ====================

    /**
     * JSON 字符串转 List
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(String json, Class<T> elementClass) {
        try {
            return (List<T>) MAPPER.readValue(json,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, elementClass));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON deserialization failed", e);
        }
    }

    // ==================== JSON 字符串 → Map ====================

    /**
     * JSON 字符串转 Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(String json) {
        try {
            return MAPPER.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON deserialization failed", e);
        }
    }

    // ==================== 对象转换 ====================

    /**
     * 对象类型转换（如 Map → VO、DTO → VO）
     */
    public static <T> T convert(Object from, Class<T> toClass) {
        return MAPPER.convertValue(from, toClass);
    }

    /**
     * 对象类型转换（泛型版本），如 Map → List&lt;UserVO&gt;
     */
    public static <T> T convert(Object from, TypeReference<T> typeReference) {
        return MAPPER.convertValue(from, typeReference);
    }

    // ==================== 校验 ====================

    /**
     * 判断字符串是否为合法 JSON
     */
    public static boolean isValidJson(String json) {
        if (StringTool.isEmpty(json)) {
            return false;
        }
        try {
            MAPPER.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    // ==================== 格式化 ====================

    /**
     * 格式化 JSON 字符串
     */
    public static String format(String json) {
        try {
            Object obj = MAPPER.readTree(json);
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format failed", e);
        }
    }
}
