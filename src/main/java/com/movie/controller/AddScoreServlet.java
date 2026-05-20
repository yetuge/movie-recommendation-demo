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
 * Add/update score controller.
 */
@WebServlet("/addScore")
public class AddScoreServlet extends HttpServlet {

    private ReviewService reviewService;

    @Override
    public void init() {
        this.reviewService = new ReviewService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        OrdinaryUser user = (OrdinaryUser) session.getAttribute("currentUser");
        if (user == null) {
            response.sendRedirect("signin.jsp");
            return;
        }

        Integer movieId = parseInt(request.getParameter("movieId"));
        Double score = parseDouble(request.getParameter("score"));

        if (movieId == null || score == null) {
            request.setAttribute("errorMsg", "Invalid parameters");
            request.getRequestDispatcher("DisplayMovieInformation.jsp").forward(request, response);
            return;
        }

        ServiceResult result = reviewService.submitOrUpdateScore(user.getUserId(), movieId, score);
        if (result.isSuccess()) {
            response.sendRedirect("movieDetail?movieId=" + movieId);
            return;
        }

        request.setAttribute("errorMsg", result.getMessage());
        request.getRequestDispatcher("DisplayMovieInformation.jsp").forward(request, response);
    }

    private Integer parseInt(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
