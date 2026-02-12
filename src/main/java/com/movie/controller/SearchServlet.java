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
 * 电影搜索控制器
 */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    private MovieDao movieDao;

    @Override
    public void init() {
        this.movieDao = new MovieDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 获取搜索关键词
        String keyword = request.getParameter("keyword");

        // 验证关键词
        if (keyword == null || keyword.trim().isEmpty()) {
            // 关键词为空，跳转到电影列表
            response.sendRedirect("movieList");
            return;
        }

        // 调用 DAO 搜索电影
        List<MovieInformation> movies = movieDao.searchMovies(keyword.trim());

        // 存入 request 域
        request.setAttribute("movies", movies);
        request.setAttribute("searchKeyword", keyword.trim());

        // 转发到 movieList.jsp（复用相同的布局）
        request.getRequestDispatcher("movieList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // POST 请求同样处理
        doGet(request, response);
    }
}
