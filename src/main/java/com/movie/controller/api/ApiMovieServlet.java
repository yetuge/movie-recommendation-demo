package com.movie.controller.api;

import com.movie.dao.MovieDao;
import com.movie.entity.MovieInformation;
import com.movie.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/movie")
public class ApiMovieServlet extends HttpServlet {

    private MovieDao movieDao;

    @Override
    public void init() {
        this.movieDao = new MovieDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleApiRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleApiRequest(request, response);
    }

    private void handleApiRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String json;

        if ("list".equals(action)) {
            List<MovieInformation> movieList = movieDao.findShowingMovies();
            json = JsonUtil.toJson(movieList);
        } else if ("detail".equals(action)) {
            String movieIdStr = request.getParameter("movieId");
            if (movieIdStr != null && !movieIdStr.isEmpty()) {
                int movieId = Integer.parseInt(movieIdStr);
                MovieInformation movie = movieDao.findMovieById(movieId);
                json = JsonUtil.toJson(movie);
            } else {
                json = "{\"error\":\"参数错误，缺少 movieId\"}";
            }
        } else if ("search".equals(action)) {
            String keyword = request.getParameter("keyword");
            if (keyword != null && !keyword.isEmpty()) {
                List<MovieInformation> movieList = movieDao.searchMovies(keyword);
                json = JsonUtil.toJson(movieList);
            } else {
                json = "{\"error\":\"参数错误，缺少 keyword\"}";
            }
        } else if ("genre".equals(action)) {
            String genre = request.getParameter("genre");
            if (genre != null && !genre.isEmpty()) {
                List<MovieInformation> movieList = movieDao.getMoviesByGenre(genre);
                json = JsonUtil.toJson(movieList);
            } else {
                json = "{\"error\":\"参数错误，缺少 genre\"}";
            }
        } else {
            List<MovieInformation> movieList = movieDao.findAllMovies();
            json = JsonUtil.toJson(movieList);
        }

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}