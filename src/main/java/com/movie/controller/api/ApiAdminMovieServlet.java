package com.movie.controller.api;

import com.movie.dao.MovieDao;
import com.movie.entity.MovieInformation;
import com.movie.entity.OrdinaryUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/admin/movie")
public class ApiAdminMovieServlet extends HttpServlet {

    private static final String ADMIN_EMAIL = "test@test.com";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private MovieDao movieDao;

    @Override
    public void init() throws ServletException {
        this.movieDao = new MovieDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        ApiResponses.setJsonHeaders(resp, "GET, OPTIONS");

        if (!isAdmin(req)) {
            ApiResponses.writeError(resp, "Admin permission required");
            return;
        }

        String action = trim(req.getParameter("action"));
        if (action == null || action.isEmpty() || "list".equals(action)) {
            List<MovieInformation> movies = movieDao.findAllMovies();
            ApiResponses.writeSuccess(resp, "OK", movies);
            return;
        }

        ApiResponses.writeError(resp, "Unsupported action");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        ApiResponses.setJsonHeaders(resp, "POST, OPTIONS");

        if (!isAdmin(req)) {
            ApiResponses.writeError(resp, "Admin permission required");
            return;
        }

        String action = trim(req.getParameter("action"));
        if ("add".equals(action)) {
            addMovie(req, resp);
            return;
        }
        if ("update".equals(action)) {
            updateMovie(req, resp);
            return;
        }
        if ("delete".equals(action)) {
            deleteMovie(req, resp);
            return;
        }
        if ("toggleStatus".equals(action)) {
            toggleStatus(req, resp);
            return;
        }

        ApiResponses.writeError(resp, "Unsupported action");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        ApiResponses.setJsonHeaders(resp, "GET, POST, OPTIONS");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void addMovie(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        MovieInformation movie;
        try {
            movie = parseMovie(req, false);
        } catch (IOException e) {
            ApiResponses.writeError(resp, e.getMessage());
            return;
        }
        if (isBlank(movie.getName())) {
            ApiResponses.writeError(resp, "name is required");
            return;
        }

        boolean ok = movieDao.addMovie(movie);
        if (ok) {
            ApiResponses.writeSuccess(resp, "Movie added", operationResult(true));
        } else {
            ApiResponses.writeError(resp, "Add movie failed", operationResult(false));
        }
    }

    private void updateMovie(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        MovieInformation movie;
        try {
            movie = parseMovie(req, true);
        } catch (IOException e) {
            ApiResponses.writeError(resp, e.getMessage());
            return;
        }
        if (movie.getMovieId() <= 0) {
            ApiResponses.writeError(resp, "movieId is required");
            return;
        }
        if (isBlank(movie.getName())) {
            ApiResponses.writeError(resp, "name is required");
            return;
        }

        boolean ok = movieDao.updateMovie(movie);
        if (ok) {
            ApiResponses.writeSuccess(resp, "Movie updated", operationResult(true));
        } else {
            ApiResponses.writeError(resp, "Update movie failed", operationResult(false));
        }
    }

    private void deleteMovie(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer movieId = parseInt(trim(req.getParameter("movieId")));
        if (movieId == null) {
            ApiResponses.writeError(resp, "movieId is required");
            return;
        }

        boolean ok = movieDao.deleteMovie(movieId);
        if (ok) {
            ApiResponses.writeSuccess(resp, "Movie deleted", operationResult(true));
        } else {
            ApiResponses.writeError(resp, "Delete movie failed", operationResult(false));
        }
    }

    private void toggleStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer movieId = parseInt(trim(req.getParameter("movieId")));
        Integer currentStatus = parseInt(trim(req.getParameter("currentStatus")));
        Integer targetStatus = parseInt(trim(req.getParameter("targetStatus")));
        if (movieId == null) {
            ApiResponses.writeError(resp, "movieId is required");
            return;
        }

        int newStatus;
        if (targetStatus != null) {
            newStatus = targetStatus == 1 ? 1 : 0;
        } else if (currentStatus != null) {
            newStatus = currentStatus == 1 ? 0 : 1;
        } else {
            ApiResponses.writeError(resp, "currentStatus or targetStatus is required");
            return;
        }

        boolean ok = movieDao.updateMovieStatus(movieId, newStatus);
        Map<String, Object> data = operationResult(ok);
        data.put("isShowing", newStatus);
        if (ok) {
            ApiResponses.writeSuccess(resp, "Movie status updated", data);
        } else {
            ApiResponses.writeError(resp, "Update status failed", data);
        }
    }

    private MovieInformation parseMovie(HttpServletRequest req, boolean requireMovieId) throws IOException {
        MovieInformation movie = new MovieInformation();
        if (requireMovieId) {
            Integer movieId = parseInt(trim(req.getParameter("movieId")));
            movie.setMovieId(movieId == null ? 0 : movieId);
        }

        movie.setName(trim(req.getParameter("name")));
        movie.setContent(defaultString(trim(req.getParameter("content"))));
        movie.setGenre(defaultString(trim(req.getParameter("genre"))));
        movie.setDirector(defaultString(trim(req.getParameter("director"))));
        movie.setActors(defaultString(trim(req.getParameter("actors"))));
        movie.setCountry(defaultString(trim(req.getParameter("country"))));

        String releaseTime = trim(req.getParameter("releaseTime"));
        if (!isBlank(releaseTime)) {
            try {
                movie.setReleaseTime(new SimpleDateFormat(DATE_FORMAT).parse(releaseTime));
            } catch (Exception e) {
                throw new IOException("Invalid releaseTime format, expected yyyy-MM-dd", e);
            }
        } else {
            movie.setReleaseTime(new Date());
        }

        Double score = parseDouble(trim(req.getParameter("score")));
        Integer duration = parseInt(trim(req.getParameter("duration")));
        movie.setScore(score == null ? 0.0 : score);
        movie.setDuration(duration == null ? 0 : duration);
        return movie;
    }

    private boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        OrdinaryUser sessionUser = session == null ? null : (OrdinaryUser) session.getAttribute("currentUser");
        if (sessionUser != null && ADMIN_EMAIL.equals(sessionUser.getUserMailbox())) {
            return true;
        }

        String adminMailbox = trim(req.getParameter("adminMailbox"));
        return ADMIN_EMAIL.equals(adminMailbox);
    }

    private Map<String, Object> operationResult(boolean success) {
        Map<String, Object> data = new HashMap<>();
        data.put("affected", success);
        return data;
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private String defaultString(String value) {
        return value == null ? "" : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.isEmpty();
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
