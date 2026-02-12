package com.movie.controller;

import com.movie.dao.CollectDao;
import com.movie.dao.MovieDao;
import com.movie.entity.OrdinaryUser;
import com.movie.entity.MovieInformation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 推荐控制器 - 根据用户行为推荐电影
 * @deprecated 此类已被 RecommendServlet 取代，保留仅作参考
 */
@WebServlet("/recommendLegacy")
public class RecommendationServlet extends HttpServlet {

    private MovieDao movieDao;
    private CollectDao collectDao;

    @Override
    public void init() {
        this.movieDao = new MovieDao();
        this.collectDao = new CollectDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 获取当前登录用户
        HttpSession session = request.getSession();
        OrdinaryUser currentUser = (OrdinaryUser) session.getAttribute("currentUser");

        List<MovieInformation> recommendations = null;

        if (currentUser == null) {
            // 情况 A：未登录，使用热门推荐
            System.out.println("【推荐系统】用户未登录，使用热门推荐");
            recommendations = movieDao.findTopRatedMovies(8);
        } else {
            // 情况 B：已登录，基于内容推荐
            System.out.println("【推荐系统】用户已登录，用户ID: " + currentUser.getUserId());

            // 查询用户最近收藏的电影类型
            String preferredGenre = collectDao.getUserRecentCollectGenre(currentUser.getUserId());

            if (preferredGenre != null && !preferredGenre.isEmpty()) {
                // 有收藏记录，推荐同类电影
                System.out.println("【推荐系统】用户偏好类型: " + preferredGenre);
                recommendations = movieDao.findMoviesByGenre(preferredGenre, 8);
            } else {
                // 无收藏记录，降级为热门推荐
                System.out.println("【推荐系统】用户无收藏记录，使用热门推荐");
                recommendations = movieDao.findTopRatedMovies(8);
            }
        }

        // 存入 request 属性
        request.setAttribute("recommendations", recommendations);

        // 转发到 recommendation.jsp
        request.getRequestDispatcher("recommendation.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // POST 请求同样处理
        doGet(request, response);
    }
}
