package com.movie.controller;

import com.movie.entity.MovieInformation;
import com.movie.entity.OrdinaryUser;
import com.movie.service.RecommendationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/recommend")
public class RecommendServlet extends HttpServlet {

    private RecommendationService recommendationService;

    @Override
    public void init() {
        this.recommendationService = new RecommendationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        OrdinaryUser user = (OrdinaryUser) session.getAttribute("currentUser");
        Integer userId = user == null ? null : user.getUserId();

        List<MovieInformation> recommendMovies = recommendationService.getRecommendationsForUser(userId);

        request.setAttribute("recommendList", recommendMovies);
        request.getRequestDispatcher("recommendation.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
