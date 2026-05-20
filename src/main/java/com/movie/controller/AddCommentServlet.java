package com.movie.controller;

import com.movie.entity.OrdinaryUser;
import com.movie.service.ReviewService;
import com.movie.service.ServiceResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Add/update comment controller.
 */
@WebServlet("/addComment")
public class AddCommentServlet extends HttpServlet {

    private ReviewService reviewService;

    @Override
    public void init() {
        this.reviewService = new ReviewService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        OrdinaryUser user = (OrdinaryUser) session.getAttribute("currentUser");
        if (user == null) {
            response.sendRedirect("signin.jsp");
            return;
        }

        Integer movieId = parseInt(request.getParameter("movieId"));
        String content = trim(request.getParameter("content"));

        if (movieId == null || content == null || content.isEmpty()) {
            request.setAttribute("errorMsg", "Invalid parameters");
            request.getRequestDispatcher("DisplayMovieInformation.jsp").forward(request, response);
            return;
        }

        ServiceResult result = reviewService.submitOrUpdateComment(user.getUserId(), movieId, content);
        if (result.isSuccess()) {
            response.sendRedirect("movieDetail?movieId=" + movieId);
            return;
        }

        request.setAttribute("errorMsg", result.getMessage());
        request.getRequestDispatcher("DisplayMovieInformation.jsp").forward(request, response);
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private Integer parseInt(String value) {
        String trimmed = trim(value);
        if (trimmed == null || trimmed.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(trimmed);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
