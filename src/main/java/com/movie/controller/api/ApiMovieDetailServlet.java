package com.movie.controller.api;

import com.movie.dao.MovieDao;
import com.movie.dao.ScoreDao;
import com.movie.entity.MovieInformation;
import com.movie.entity.MovieScore;
import com.movie.entity.Review;
import com.movie.service.FavoriteService;
import com.movie.service.ReviewService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/movie/detailFull")
public class ApiMovieDetailServlet extends HttpServlet {

    private MovieDao movieDao;
    private ScoreDao scoreDao;
    private FavoriteService favoriteService;
    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        this.movieDao = new MovieDao();
        this.scoreDao = new ScoreDao();
        this.favoriteService = new FavoriteService();
        this.reviewService = new ReviewService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        ApiResponses.setJsonHeaders(resp, "GET, OPTIONS");

        Integer movieId = parseInt(trim(req.getParameter("movieId")));
        if (movieId == null) {
            ApiResponses.writeError(resp, "movieId is required");
            return;
        }

        MovieInformation movie = movieDao.findMovieById(movieId);
        if (movie == null) {
            ApiResponses.writeError(resp, "Movie not found");
            return;
        }

        Integer userId = favoriteService.resolveUserId(
                parseInt(trim(req.getParameter("userId"))),
                trim(req.getParameter("userMailbox"))
        );
        List<Review> reviews = reviewService.getReviewsByMovieId(movieId);
        MovieScore userScore = userId == null ? null : scoreDao.getUserScore(userId, movieId);
        boolean favorited = userId != null && favoriteService.isCollected(userId, movieId);

        Map<String, Object> data = new HashMap<>();
        data.put("movie", movie);
        data.put("avgScore", movieDao.getAverageScore(movieId));
        data.put("reviews", reviews);
        data.put("favorited", favorited);
        data.put("userScore", userScore);
        data.put("scored", userScore != null);

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
