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
import java.text.SimpleDateFormat;

/**
 * 注册控制器
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        this.userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 设置请求编码
        request.setCharacterEncoding("UTF-8");

        // 获取表单参数
        String phoneNumber = request.getParameter("phoneNumber");
        String userMailbox = request.getParameter("userMailbox");
        String userPassword = request.getParameter("userPassword");
        String userName = request.getParameter("userName");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");

        // 构建用户对象
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

        // 调用 Service 层处理注册
        OrdinaryUser registeredUser = userService.register(user);

        if (registeredUser != null) {
            // 注册成功，将用户对象存入 Session 并跳转到首页
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", registeredUser);
            response.sendRedirect("index.jsp");
        } else {
            // 注册失败，返回注册页面并带上错误信息
            request.setAttribute("errorMsg", "注册失败，账号已存在或信息不完整");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // GET 请求直接转发到注册页面
        response.sendRedirect("signup.jsp");
    }
}
