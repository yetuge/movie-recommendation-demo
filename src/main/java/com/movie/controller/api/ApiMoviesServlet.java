package com.movie.controller.api;

import com.google.gson.Gson;
import com.movie.entity.MovieInformation;
import com.movie.service.MovieService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/movies")
public class ApiMoviesServlet extends HttpServlet {

    private MovieService movieService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.movieService = new MovieService();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<MovieInformation> movies = movieService.getShowingMovies();
        String json = gson.toJson(movies);

        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().print(json);
    }
}