package com.movie.controller.api;

import com.google.gson.Gson;
import com.movie.service.FavoriteService;
import com.movie.service.FavoriteService.FavoriteToggleResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/favorite")
public class ApiFavoriteServlet extends HttpServlet {

    private FavoriteService favoriteService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.favoriteService = new FavoriteService();
        this.gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        setCorsHeaders(resp);
        resp.setContentType("application/json;charset=UTF-8");

        Map<String, Object> result = new HashMap<>();

        Integer userId = parseInt(trim(req.getParameter("userId")));
        String userMailbox = trim(req.getParameter("userMailbox"));
        Integer movieId = parseInt(trim(req.getParameter("movieId")));

        Integer resolvedUserId = favoriteService.resolveUserId(userId, userMailbox);
        if (resolvedUserId == null || movieId == null) {
            result.put("success", false);
            result.put("message", "\u53c2\u6570\u65e0\u6548");
            resp.getWriter().print(gson.toJson(result));
            return;
        }

        FavoriteToggleResult toggleResult = favoriteService.toggleFavorite(resolvedUserId, movieId);
        result.put("success", toggleResult.isSuccess());
        result.put("message", toggleResult.getMessage());
        result.put("favorited", toggleResult.isFavorited());

        resp.getWriter().print(gson.toJson(result));
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        setCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void setCorsHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
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
}
