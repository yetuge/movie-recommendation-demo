package com.movie.controller.api;

import com.google.gson.Gson;
import com.movie.entity.OrdinaryUser;
import com.movie.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/login")
public class ApiLoginServlet extends HttpServlet {

    private UserService userService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.userService = new UserService();
        this.gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        setCorsHeaders(resp);
        resp.setContentType("application/json;charset=UTF-8");

        String userMailbox = req.getParameter("userMailbox");
        String userPassword = req.getParameter("userPassword");
        Map<String, Object> result = new HashMap<>();

        if (userMailbox != null) {
            userMailbox = userMailbox.trim();
        }
        if (userPassword != null) {
            userPassword = userPassword.trim();
        }

        if (isBlank(userMailbox) || isBlank(userPassword)) {
            result.put("success", false);
            result.put("message", "\u90ae\u7bb1\u548c\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a");
            resp.getWriter().print(gson.toJson(result));
            return;
        }

        OrdinaryUser user = userService.login(userMailbox, userPassword);

        if (user != null) {
            result.put("success", true);
            result.put("message", "\u767b\u5f55\u6210\u529f");
            result.put("user", userService.toSafeUser(user));
        } else {
            result.put("success", false);
            result.put("message", "\u90ae\u7bb1\u6216\u5bc6\u7801\u9519\u8bef");
        }

        resp.getWriter().print(gson.toJson(result));
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        setCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void setCorsHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }
}
