package com.movie.controller;

import com.movie.dao.CollectDao;
import com.movie.entity.Collect;
import com.movie.entity.OrdinaryUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * 收藏/取消收藏控制器
 */
@WebServlet("/api/toggleCollect")
public class ToggleCollectServlet extends HttpServlet {

    private CollectDao collectDao;

    @Override
    public void init() {
        this.collectDao = new CollectDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 检查用户是否登录
        HttpSession session = request.getSession();
        OrdinaryUser user = (OrdinaryUser) session.getAttribute("currentUser");

        if (user == null) {
            // 未登录，跳转到登录页面
            response.sendRedirect("../signin.jsp");
            return;
        }

        // 获取参数
        String movieIdStr = request.getParameter("movieId");

        // 验证参数
        if (movieIdStr == null || movieIdStr.isEmpty()) {
            response.sendRedirect("../movieList");
            return;
        }

        int movieId;
        try {
            movieId = Integer.parseInt(movieIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("../movieList");
            return;
        }

        int userId = user.getUserId();

        // 检查是否已收藏
        if (collectDao.checkUserCollected(userId, movieId)) {
            // 已收藏，执行取消收藏操作
            if (collectDao.removeCollect(userId, movieId)) {
                // 取消收藏成功
                response.sendRedirect("../movieDetail?movieId=" + movieId);
                return;
            }
        } else {
            // 未收藏，执行收藏操作
            Collect collect = new Collect();
            collect.setMovieId(movieId);
            collect.setUserId(userId);
            collect.setCollectTime(new Date());

            if (collectDao.addCollect(collect)) {
                // 收藏成功
                response.sendRedirect("../movieDetail?movieId=" + movieId);
                return;
            }
        }

        // 操作失败，返回电影详情页
        response.sendRedirect("../movieDetail?movieId=" + movieId);
    }
}
