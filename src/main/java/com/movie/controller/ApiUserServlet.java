package com.movie.controller;

import com.movie.entity.MovieInformation;
import com.movie.entity.OrdinaryUser;
import com.movie.service.UserService;
import com.movie.service.RecommendationService;
import com.movie.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户 API Servlet
 * 支持登录、注册、获取推荐等功能
 */
@WebServlet("/api/user")
public class ApiUserServlet extends HttpServlet {

    private UserService userService;
    private RecommendationService recommendationService;

    @Override
    public void init() {
        this.userService = new UserService();
        this.recommendationService = new RecommendationService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 设置请求编码
        request.setCharacterEncoding("UTF-8");

        // 设置响应类型和编码
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 获取操作类型
        String action = request.getParameter("action");
        Map<String, Object> result = new HashMap<>();

        if ("login".equals(action)) {
            // 登录
            String account = request.getParameter("account");
            String password = request.getParameter("password");

            OrdinaryUser user = userService.login(account, password);
            if (user != null) {
                // 登录成功，存入 Session
                HttpSession session = request.getSession();
                session.setAttribute("currentUser", user);
                result.put("success", true);
                result.put("message", "登录成功");
                result.put("user", user);
            } else {
                result.put("success", false);
                result.put("message", "账号或密码错误");
            }
        } else if ("register".equals(action)) {
            // 注册
            String phoneNumber = request.getParameter("phoneNumber");
            String userMailbox = request.getParameter("userMailbox");
            String userPassword = request.getParameter("userPassword");
            String userName = request.getParameter("userName");
            String gender = request.getParameter("gender");
            String birthday = request.getParameter("birthday");

            OrdinaryUser user = new OrdinaryUser();
            user.setPhoneNumber(phoneNumber);
            user.setUserMailbox(userMailbox);
            user.setUserPassword(userPassword);
            user.setUserName(userName);
            user.setGender(gender);
            
            // 转换生日字符串为Date类型
            if (birthday != null && !birthday.isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    user.setBirthday(sdf.parse(birthday));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            OrdinaryUser registeredUser = userService.register(user);
            if (registeredUser != null) {
                // 注册成功，存入 Session
                HttpSession session = request.getSession();
                session.setAttribute("currentUser", registeredUser);
                result.put("success", true);
                result.put("message", "注册成功");
                result.put("user", registeredUser);
            } else {
                result.put("success", false);
                result.put("message", "注册失败，账号已存在");
            }
        } else if ("logout".equals(action)) {
            // 登出
            HttpSession session = request.getSession();
            session.invalidate();
            result.put("success", true);
            result.put("message", "登出成功");
        } else {
            result.put("success", false);
            result.put("message", "未知操作");
        }

        // 输出 JSON
        PrintWriter out = response.getWriter();
        out.print(JsonUtil.toJson(result));
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 设置响应类型和编码
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 获取操作类型
        String action = request.getParameter("action");
        Map<String, Object> result = new HashMap<>();

        if ("currentUser".equals(action)) {
            // 获取当前登录用户
            HttpSession session = request.getSession();
            OrdinaryUser user = (OrdinaryUser) session.getAttribute("currentUser");
            if (user != null) {
                result.put("success", true);
                result.put("user", user);
            } else {
                result.put("success", false);
                result.put("message", "未登录");
            }
        } else if ("recommend".equals(action)) {
            // 获取推荐电影
            HttpSession session = request.getSession();
            OrdinaryUser user = (OrdinaryUser) session.getAttribute("currentUser");
            if (user != null) {
                List<MovieInformation> recommendations = recommendationService.getRecommendations(user.getUserId());
                result.put("success", true);
                result.put("recommendations", recommendations);
            } else {
                result.put("success", false);
                result.put("message", "未登录");
            }
        } else {
            result.put("success", false);
            result.put("message", "未知操作");
        }

        // 输出 JSON
        PrintWriter out = response.getWriter();
        out.print(JsonUtil.toJson(result));
        out.flush();
    }
}
