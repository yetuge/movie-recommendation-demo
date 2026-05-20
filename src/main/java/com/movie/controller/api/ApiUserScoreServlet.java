package com.movie.controller.api;

import com.movie.dao.ScoreDao;
import com.movie.entity.MovieScore;
import com.movie.service.FavoriteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/user/score/check")
public class ApiUserScoreServlet extends HttpServlet {

    private FavoriteService favoriteService;
    private ScoreDao scoreDao;

    @Override
    public void init() throws ServletException {
        this.favoriteService = new FavoriteService();
        this.scoreDao = new ScoreDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        ApiResponses.setJsonHeaders(resp, "GET, OPTIONS");

        Integer movieId = parseInt(trim(req.getParameter("movieId")));
        Integer userId = favoriteService.resolveUserId(
                parseInt(trim(req.getParameter("userId"))),
                trim(req.getParameter("userMailbox"))
        );

        if (movieId == null || userId == null) {
            ApiResponses.writeError(resp, "movieId and userId or userMailbox are required");
            return;
        }

        MovieScore userScore = scoreDao.getUserScore(userId, movieId);
        Map<String, Object> data = new HashMap<>();
        data.put("scored", userScore != null);
        data.put("score", userScore);

        ApiResponses.writeSuccess(resp, "OK", data);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        ApiResponses.setJsonHeaders(resp, "GET, OPTIONS");
        resp.setStatus(HttpServletResponse.SC_OK);
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
