package com.movie.controller.api;

import com.movie.entity.MovieInformation;
import com.movie.service.FavoriteService;
import com.movie.service.RecommendationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/recommend")
public class ApiRecommendServlet extends HttpServlet {

    private FavoriteService favoriteService;
    private RecommendationService recommendationService;

    @Override
    public void init() throws ServletException {
        this.favoriteService = new FavoriteService();
        this.recommendationService = new RecommendationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        ApiResponses.setJsonHeaders(resp, "GET, OPTIONS");

        Integer userId = resolveUserId(req);
        List<MovieInformation> recommendations = recommendationService.getRecommendationsForUser(userId);
        ApiResponses.writeSuccess(resp, "OK", recommendations);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        ApiResponses.setJsonHeaders(resp, "GET, OPTIONS");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private Integer resolveUserId(HttpServletRequest req) {
        Integer userId = parseInt(trim(req.getParameter("userId")));
        String userMailbox = trim(req.getParameter("userMailbox"));
        return favoriteService.resolveUserId(userId, userMailbox);
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private Integer parseInt(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
