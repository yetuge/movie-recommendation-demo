package com.movie.controller.api;

import com.google.gson.Gson;
import com.movie.service.FavoriteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/favorite/check")
public class ApiCheckFavoriteServlet extends HttpServlet {

    private FavoriteService favoriteService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.favoriteService = new FavoriteService();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        setCorsHeaders(resp);
        resp.setContentType("application/json;charset=UTF-8");

        String userMailbox = trim(req.getParameter("userMailbox"));
        Integer movieId = parseInt(trim(req.getParameter("movieId")));

        Map<String, Object> result = new HashMap<>();
        boolean favorited = movieId != null && favoriteService.isFavoritedByMailbox(userMailbox, movieId);

        result.put("favorited", favorited);
        resp.getWriter().print(gson.toJson(result));
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        setCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void setCorsHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
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
