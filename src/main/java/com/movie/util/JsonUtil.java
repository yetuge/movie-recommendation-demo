package com.movie.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * JSON 工具类
 */
public class JsonUtil {

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    /**
     * 将对象转换为 JSON 字符串
     */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * 将 JSON 字符串转换为指定类型的对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
