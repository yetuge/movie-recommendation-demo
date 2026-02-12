package com.movie.controller;

import com.movie.dao.CollectDao;
import com.movie.dao.MovieDao;
import com.movie.dao.ScoreDao;
import com.movie.entity.MovieComment;
import com.movie.entity.MovieInformation;
import com.movie.entity.MovieScore;
import com.movie.entity.OrdinaryUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 电影详情控制器
 */
@WebServlet("/movieDetail")
public class MovieDetailServlet extends HttpServlet {

    private MovieDao movieDao;
    private CollectDao collectDao;
    private ScoreDao scoreDao;

    @Override
    public void init() {
        this.movieDao = new MovieDao();
        this.collectDao = new CollectDao();
        this.scoreDao = new ScoreDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 获取 movieId 参数
        String movieIdStr = request.getParameter("movieId");

        if (movieIdStr == null || movieIdStr.isEmpty()) {
            // 缺少 movieId 参数，返回列表页
            response.sendRedirect("movieList");
            return;
        }

        int movieId;
        try {
            movieId = Integer.parseInt(movieIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("movieList");
            return;
        }

        // 查询电影详情
        MovieInformation movie = movieDao.findMovieById(movieId);

        if (movie == null) {
            // 电影不存在，返回列表页
            response.sendRedirect("movieList");
            return;
        }

        // 查询关联的评论
        List<MovieComment> commentList = movieDao.getCommentsByMovieId(movieId);

        // 计算平均分
        double avgScore = movieDao.getAverageScore(movieId);

        // 存入 request 域
        request.setAttribute("movie", movie);
        request.setAttribute("commentList", commentList);
        request.setAttribute("avgScore", avgScore);
        request.setAttribute("movieId", movieId);

        // 检查用户登录状态
        HttpSession session = request.getSession();
        OrdinaryUser user = (OrdinaryUser) session.getAttribute("currentUser");

        if (user != null) {
            int userId = user.getUserId();

            // 检查用户是否已收藏该电影
            boolean isCollected = collectDao.checkUserCollected(userId, movieId);
            request.setAttribute("isCollected", isCollected);

            // 获取用户对该电影的评分（如果已评）
            MovieScore userScore = scoreDao.getUserScore(userId, movieId);
            request.setAttribute("userScore", userScore);
        } else {
            request.setAttribute("isCollected", false);
            request.setAttribute("userScore", null);
        }

        // 转发到电影详情页面
        request.getRequestDispatcher("movieDetail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // POST 请求同样处理
        doGet(request, response);
    }
}
