package com.movie.controller;

import com.movie.dao.ScoreDao;
import com.movie.entity.MovieScore;
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
 * 添加/更新评分控制器
 */
@WebServlet("/addScore")
public class AddScoreServlet extends HttpServlet {

    private ScoreDao scoreDao;

    @Override
    public void init() {
        this.scoreDao = new ScoreDao();
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

        // 获取表单参数
        String movieIdStr = request.getParameter("movieId");
        String scoreStr = request.getParameter("score");

        // 验证参数
        if (movieIdStr == null || movieIdStr.isEmpty() || scoreStr == null || scoreStr.isEmpty()) {
            request.setAttribute("errorMsg", "参数不完整");
            request.getRequestDispatcher("DisplayMovieInformation.jsp").forward(request, response);
            return;
        }

        int movieId = Integer.parseInt(movieIdStr);
        int userId = user.getUserId();
        double score = Double.parseDouble(scoreStr);

        // 验证评分范围（假设评分范围为0-10）
        if (score < 0 || score > 10) {
            request.setAttribute("errorMsg", "评分范围为0-10分");
            request.getRequestDispatcher("DisplayMovieInformation.jsp").forward(request, response);
            return;
        }

        // 检查用户是否已评分过该电影
        if (scoreDao.checkUserScored(userId, movieId)) {
            // 已评分过，更新评分
            MovieScore existingScore = scoreDao.getUserScore(userId, movieId);
            if (existingScore != null) {
                existingScore.setScore(score);
                existingScore.setPublishTime(new Date());

                if (scoreDao.updateScore(existingScore)) {
                    // 更新成功
                    response.sendRedirect("movieDetail?movieId=" + movieId);
                    return;
                }
            }

            // 更新失败
            request.setAttribute("errorMsg", "更新评分失败");
        } else {
            // 未评分过，添加新评分
            MovieScore newScore = new MovieScore();
            newScore.setMovieId(movieId);
            newScore.setUserId(userId);
            newScore.setScore(score);
            newScore.setPublishTime(new Date());

            if (scoreDao.addScore(newScore)) {
                // 添加成功
                response.sendRedirect("movieDetail?movieId=" + movieId);
                return;
            }

            // 添加失败
            request.setAttribute("errorMsg", "添加评分失败");
        }

        // 操作失败，返回电影详情页
        request.getRequestDispatcher("DisplayMovieInformation.jsp").forward(request, response);
    }
}
