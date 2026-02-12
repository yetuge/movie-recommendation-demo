package com.movie.controller;

import com.movie.dao.MovieDao;
import com.movie.entity.MovieInformation;
import com.movie.entity.OrdinaryUser;
import com.movie.service.RecommendationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
* 首页控制器
*/
@WebServlet("/index")
public class IndexServlet extends HttpServlet {

    private MovieDao movieDao;
    private RecommendationService recommendationService;

    @Override
    public void init() {
        this.movieDao = new MovieDao();
        this.recommendationService = new RecommendationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 获取热门电影（前4部）
        List<MovieInformation> hotMovies = movieDao.findAllMovies();
        request.setAttribute("hotMovies", hotMovies);

        // 检查用户是否登录，如果登录则生成推荐
        HttpSession session = request.getSession();
        OrdinaryUser user = (OrdinaryUser) session.getAttribute("currentUser");

        if (user != null) {
            // 获取推荐电影（策略 A：基于内容，策略 B：热门推荐）
            List<MovieInformation> recommendations = recommendationService.getRecommendations(user.getUserId());
            request.setAttribute("recommendations", recommendations);
        }

        // 转发到 index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // POST 请求同样处理
        doGet(request, response);
    }
}
