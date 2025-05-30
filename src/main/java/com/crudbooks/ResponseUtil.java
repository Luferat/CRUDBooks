package com.crudbooks;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    public static Map<String, Object> success(int code, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("code", code);
        response.put("message", message);
        return response;
    }

    public static Map<String, Object> success(int code, String message, Object data) {
        Map<String, Object> response = success(code, message);
        response.put("data", data);
        return response;
    }

    public static Map<String, Object> error(int code, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("code", code);
        response.put("message", message);
        return response;
    }

    public static Map<String, Object> error(int code, String message, Object data) {
        Map<String, Object> response = error(code, message);
        response.put("data", data);
        return response;
    }
}
