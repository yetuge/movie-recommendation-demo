package com.movie.controller;

import com.movie.entity.OrdinaryUser;
import com.movie.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录控制器
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        this.userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 获取表单参数
        String account = request.getParameter("account");
        String password = request.getParameter("password");

        // 去除首尾空格
        if (account != null) {
            account = account.trim();
        }
        if (password != null) {
            password = password.trim();
        }

        // 打印调试信息
        System.out.println("【LoginServlet】处理后的账号: " + account);
        System.out.println("【LoginServlet】密码长度: " + (password != null ? password.length() : 0));

        // 调用 Service 层处理登录
        System.out.println("【LoginServlet】正在调用 Service...");
        OrdinaryUser user = userService.login(account, password);

        if (user != null) {
            // 登录成功，将用户对象存入 Session
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);

            // 跳转到首页
            response.sendRedirect("index.jsp");
        } else {
            // 登录失败，返回登录页面并带上错误信息
            request.setAttribute("errorMsg", "账号或密码错误");
            request.getRequestDispatcher("signin.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // GET 请求直接转发到登录页面
        response.sendRedirect("signin.jsp");
    }
}
