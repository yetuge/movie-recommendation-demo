package com.movie.controller;

import com.movie.dao.CommentDao;
import com.movie.entity.MovieComment;
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
 * 添加评论控制器
 */
@WebServlet("/addComment")
public class AddCommentServlet extends HttpServlet {

    private CommentDao commentDao;

    @Override
    public void init() {
        this.commentDao = new CommentDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 设置请求编码
        request.setCharacterEncoding("UTF-8");

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
        String content = request.getParameter("content");

        // 验证参数
        if (movieIdStr == null || movieIdStr.isEmpty() || content == null || content.trim().isEmpty()) {
            request.setAttribute("errorMsg", "参数不完整");
            request.getRequestDispatcher("MovieDetail.jsp").forward(request, response);
            return;
        }

        int movieId = Integer.parseInt(movieIdStr);
        int userId = user.getUserId();

        // 检查用户是否已评论过该电影
        if (commentDao.checkUserCommented(userId, movieId)) {
            // 已评论过，获取原评论
            MovieComment existingComment = commentDao.getUserComment(userId, movieId);
            if (existingComment != null) {
                // 更新评论
                existingComment.setContent(content.trim());
                existingComment.setPublishTime(new Date());

                if (commentDao.updateComment(existingComment)) {
                    // 更新成功
                    response.sendRedirect("movieDetail?movieId=" + movieId);
                    return;
                }
            }

            // 更新失败或获取失败
            request.setAttribute("errorMsg", "更新评论失败");
        } else {
            // 未评论过，添加新评论
            MovieComment comment = new MovieComment();
            comment.setMovieId(movieId);
            comment.setUserId(userId);
            comment.setContent(content.trim());
            comment.setPublishTime(new Date());

            if (commentDao.addComment(comment)) {
                // 添加成功
                response.sendRedirect("movieDetail?movieId=" + movieId);
                return;
            }

            // 添加失败
            request.setAttribute("errorMsg", "添加评论失败");
        }

        // 操作失败，返回电影详情页
        request.getRequestDispatcher("DisplayMovieInformation.jsp").forward(request, response);
    }
}
