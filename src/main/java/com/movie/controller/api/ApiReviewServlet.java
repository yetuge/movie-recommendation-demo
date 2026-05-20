package com.movie.controller.api;

import com.google.gson.Gson;
import com.movie.entity.Review;
import com.movie.service.ReviewService;
import com.movie.service.ServiceResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/reviews")
public class ApiReviewServlet extends HttpServlet {

    private ReviewService reviewService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.reviewService = new ReviewService();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        setCorsHeaders(resp);
        resp.setContentType("application/json;charset=UTF-8");

        Integer movieId = parseInt(trim(req.getParameter("movieId")));
        if (movieId == null) {
            resp.getWriter().print(gson.toJson(new ArrayList<Review>()));
            return;
        }

        List<Review> reviews = reviewService.getReviewsByMovieId(movieId);
        resp.getWriter().print(gson.toJson(reviews));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        setCorsHeaders(resp);
        resp.setContentType("application/json;charset=UTF-8");

        String userMailbox = trim(req.getParameter("userMailbox"));
        String content = trim(req.getParameter("content"));
        Integer movieId = parseInt(trim(req.getParameter("movieId")));
        Double score = parseDouble(trim(req.getParameter("score")));

        Map<String, Object> result = new HashMap<>();

        if (movieId == null || score == null) {
            result.put("success", false);
            result.put("message", "\u53c2\u6570\u65e0\u6548");
            resp.getWriter().print(gson.toJson(result));
            return;
        }

        ServiceResult submitResult = reviewService.submitReviewByMailbox(movieId, userMailbox, content, score);
        result.put("success", submitResult.isSuccess());
        result.put("message", submitResult.getMessage());

        resp.getWriter().print(gson.toJson(result));
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        setCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void setCorsHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
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

    private Double parseDouble(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
