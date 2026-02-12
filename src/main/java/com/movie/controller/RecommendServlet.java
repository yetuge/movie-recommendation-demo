package com.movie.controller;

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
 * 推荐控制器
 * 生成"猜你喜欢"电影推荐
 */
@WebServlet("/recommend")
public class RecommendServlet extends HttpServlet {

    private RecommendationService recommendationService;

    @Override
    public void init() {
        this.recommendationService = new RecommendationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 检查用户是否登录
        HttpSession session = request.getSession();
        OrdinaryUser user = (OrdinaryUser) session.getAttribute("currentUser");

        if (user == null) {
            // 未登录，显示热门推荐（策略 B：冷启动）
            List<MovieInformation> hotMovies = recommendationService.recommendHotMovies();
            request.setAttribute("recommendations", hotMovies);
            request.setAttribute("recommendationType", "热门推荐");
        } else {
            // 已登录，生成个性化推荐
            List<MovieInformation> recommendations = recommendationService.getRecommendations(user.getUserId());
            request.setAttribute("recommendations", recommendations);
            request.setAttribute("recommendationType", "猜你喜欢");
        }

        // 转发到推荐页面
        request.getRequestDispatcher("recommendation.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // POST 请求同样处理
        doGet(request, response);
    }
}
