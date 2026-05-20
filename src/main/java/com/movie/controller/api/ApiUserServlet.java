package com.movie.controller.api;

import com.movie.entity.MovieInformation;
import com.movie.entity.OrdinaryUser;
import com.movie.service.RecommendationService;
import com.movie.service.UserService;
import com.movie.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

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
        request.setCharacterEncoding("UTF-8");
        ApiResponses.setJsonHeaders(response, "POST, OPTIONS");

        String action = request.getParameter("action");
        if ("login".equals(action)) {
            handleLogin(request, response);
            return;
        }
        if ("register".equals(action)) {
            handleRegister(request, response);
            return;
        }
        if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            ApiResponses.writeSuccess(response, "Logged out", null);
            return;
        }

        ApiResponses.writeError(response, "Unsupported action");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ApiResponses.setJsonHeaders(response, "GET, OPTIONS");

        String action = request.getParameter("action");
        if ("currentUser".equals(action)) {
            HttpSession session = request.getSession(false);
            OrdinaryUser user = session == null ? null : (OrdinaryUser) session.getAttribute("currentUser");
            if (user == null) {
                ApiResponses.writeError(response, "Not logged in");
                return;
            }

            ApiResponses.writeSuccess(response, "OK", userService.toSafeUser(user));
            return;
        }

        if ("recommend".equals(action)) {
            HttpSession session = request.getSession(false);
            OrdinaryUser user = session == null ? null : (OrdinaryUser) session.getAttribute("currentUser");
            if (user == null) {
                ApiResponses.writeError(response, "Not logged in");
                return;
            }

            List<MovieInformation> recommendations = recommendationService.getRecommendations(user.getUserId());
            ApiResponses.writeSuccess(response, "OK", recommendations);
            return;
        }

        ApiResponses.writeError(response, "Unsupported action");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        ApiResponses.setJsonHeaders(response, "GET, POST, OPTIONS");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String account = trim(request.getParameter("account"));
        String password = trim(request.getParameter("password"));

        OrdinaryUser user = userService.login(account, password);
        if (user == null) {
            ApiResponses.writeError(response, "Invalid account or password");
            return;
        }

        request.getSession().setAttribute("currentUser", user);
        ApiResponses.writeSuccess(response, "Login successful", userService.toSafeUser(user));
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OrdinaryUser user = new OrdinaryUser();
        user.setPhoneNumber(trim(request.getParameter("phoneNumber")));
        user.setUserMailbox(trim(request.getParameter("userMailbox")));
        user.setUserPassword(trim(request.getParameter("userPassword")));
        user.setUserName(trim(request.getParameter("userName")));
        user.setGender(trim(request.getParameter("gender")));

        String birthday = trim(request.getParameter("birthday"));
        if (birthday != null && !birthday.isEmpty()) {
            try {
                user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
            } catch (Exception e) {
                ApiResponses.writeError(response, "Invalid birthday format, expected yyyy-MM-dd");
                return;
            }
        }

        OrdinaryUser registeredUser = userService.register(user);
        if (registeredUser == null) {
            ApiResponses.writeError(response, "Register failed, account may already exist or required fields are missing");
            return;
        }

        request.getSession().setAttribute("currentUser", registeredUser);
        ApiResponses.writeSuccess(response, "Register successful", userService.toSafeUser(registeredUser));
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
