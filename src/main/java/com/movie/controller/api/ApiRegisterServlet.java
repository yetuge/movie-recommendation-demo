package com.movie.controller.api;

import com.movie.entity.OrdinaryUser;
import com.movie.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

@WebServlet("/api/register")
public class ApiRegisterServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        ApiResponses.setJsonHeaders(resp, "POST, OPTIONS");

        String phoneNumber = trim(req.getParameter("phoneNumber"));
        String userMailbox = trim(req.getParameter("userMailbox"));
        String userPassword = trim(req.getParameter("userPassword"));
        String userName = trim(req.getParameter("userName"));

        if (isBlank(userMailbox) || isBlank(userPassword) || isBlank(userName)) {
            ApiResponses.writeError(resp, "userMailbox, userPassword and userName are required");
            return;
        }

        OrdinaryUser user = new OrdinaryUser();
        user.setPhoneNumber(phoneNumber);
        user.setUserMailbox(userMailbox);
        user.setUserPassword(userPassword);
        user.setUserName(userName);
        user.setGender(trim(req.getParameter("gender")));

        String birthday = trim(req.getParameter("birthday"));
        if (!isBlank(birthday)) {
            try {
                user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
            } catch (Exception e) {
                ApiResponses.writeError(resp, "Invalid birthday format, expected yyyy-MM-dd");
                return;
            }
        }

        OrdinaryUser registeredUser = userService.register(user);
        if (registeredUser == null) {
            ApiResponses.writeError(resp, "Register failed, account may already exist");
            return;
        }

        ApiResponses.writeSuccess(resp, "Register successful", userService.toSafeUser(registeredUser));
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        ApiResponses.setJsonHeaders(resp, "POST, OPTIONS");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }
}
