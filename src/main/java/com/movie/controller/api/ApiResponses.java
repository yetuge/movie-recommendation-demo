package com.movie.controller.api;

import com.movie.util.JsonUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

final class ApiResponses {

    private ApiResponses() {
    }

    static void setJsonHeaders(HttpServletResponse response, String methods) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", methods);
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
    }

    static void writeSuccess(HttpServletResponse response, String message, Object data) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", message);
        result.put("data", data);
        response.getWriter().print(JsonUtil.toJson(result));
    }

    static void writeError(HttpServletResponse response, String message) throws IOException {
        writeError(response, message, null);
    }

    static void writeError(HttpServletResponse response, String message, Object data) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        if (data != null) {
            result.put("data", data);
        }
        response.getWriter().print(JsonUtil.toJson(result));
    }
}
