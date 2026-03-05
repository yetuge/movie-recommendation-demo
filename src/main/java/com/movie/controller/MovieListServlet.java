package com.movie.controller;

import com.movie.dao.MovieDao;
import com.movie.entity.MovieInformation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 电影列表控制器 - 转发到 movieList.jsp
 */
@WebServlet("/movieList")
public class MovieListServlet extends HttpServlet {

    private MovieDao movieDao;

    @Override
    public void init() {
        this.movieDao = new MovieDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 获取正在上映的电影列表（前台专用）
        List<MovieInformation> movieList = movieDao.findShowingMovies();

        // 存入 request 属性 (movieList.jsp 使用 "movies")
        request.setAttribute("movies", movieList);

        // 转发到 movieList.jsp
        request.getRequestDispatcher("movieList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // POST 请求同样处理
        doGet(request, response);
    }
}
