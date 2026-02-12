package com.movie.controller;

import com.movie.dao.CollectDao;
import com.movie.entity.Collect;
import com.movie.entity.MovieInformation;
import com.movie.entity.OrdinaryUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 收藏控制器
 * 支持：添加收藏、取消收藏、查看收藏列表
 */
@WebServlet("/collect")
public class CollectServlet extends HttpServlet {

    private CollectDao collectDao;

    @Override
    public void init() {
        this.collectDao = new CollectDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 检查用户是否登录
        HttpSession session = request.getSession();
        OrdinaryUser user = (OrdinaryUser) session.getAttribute("currentUser");

        if (user == null) {
            // 未登录，跳转到登录页面
            response.sendRedirect("signin.jsp");
            return;
        }

        // 查看收藏列表
        int userId = user.getUserId();
        List<MovieInformation> movieList = collectDao.getUserCollects(userId);
        request.setAttribute("myCollections", movieList);
        request.getRequestDispatcher("collect.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 检查用户是否登录
        HttpSession session = request.getSession();
        OrdinaryUser user = (OrdinaryUser) session.getAttribute("currentUser");

        if (user == null) {
            // 未登录，跳转到登录页面
            response.sendRedirect("signin.jsp");
            return;
        }

        // 获取参数
        String movieIdStr = request.getParameter("movieId");
        String action = request.getParameter("action"); // action: "add" 或 "remove"

        // 验证参数
        if (movieIdStr == null || movieIdStr.isEmpty() || action == null || action.isEmpty()) {
            request.setAttribute("errorMsg", "参数不完整");
            response.sendRedirect("movieList");
            return;
        }

        int movieId = Integer.parseInt(movieIdStr);
        int userId = user.getUserId();

        if ("add".equals(action)) {
            // 添加收藏
            if (collectDao.checkUserCollected(userId, movieId)) {
                // 已收藏
                request.setAttribute("errorMsg", "已收藏该电影");
                response.sendRedirect("movieDetail?movieId=" + movieId);
                return;
            }

            Collect collect = new Collect();
            collect.setMovieId(movieId);
            collect.setUserId(userId);
            collect.setCollectTime(new Date());

            if (collectDao.addCollect(collect)) {
                // 收藏成功
                response.sendRedirect("movieDetail?movieId=" + movieId);
                return;
            }

            // 收藏失败
            request.setAttribute("errorMsg", "收藏失败");
        } else if ("remove".equals(action)) {
            // 取消收藏
            if (!collectDao.checkUserCollected(userId, movieId)) {
                // 未收藏
                request.setAttribute("errorMsg", "未收藏该电影");
                response.sendRedirect("movieDetail?movieId=" + movieId);
                return;
            }

            if (collectDao.removeCollect(userId, movieId)) {
                // 取消收藏成功
                response.sendRedirect("movieDetail?movieId=" + movieId);
                return;
            }

            // 取消收藏失败
            request.setAttribute("errorMsg", "取消收藏失败");
        }

        // 操作失败，返回电影详情页
        request.getRequestDispatcher("DisplayMovieInformation.jsp").forward(request, response);
    }
}
