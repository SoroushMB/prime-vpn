package com.prime.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class JsonUtil {
    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    /**
     * Converts an object to JSON string.
     */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * Converts JSON string to object of specified class.
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, classOfT);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts JSON string to object of specified type.
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        try {
            return gson.fromJson(json, typeOfT);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts JSON string to array of specified class.
     */
    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        try {
            return gson.fromJson(json, typeToken.getType());
        } catch (Exception e) {
            return null;
        }
    }
}
