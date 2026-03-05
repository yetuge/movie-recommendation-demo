package com.movie.controller;

import com.movie.dao.MovieDao;
import com.movie.entity.OrdinaryUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 管理员电影管理 Servlet
 * 提供电影的增删改查功能
 */
@WebServlet("/admin/movies")
public class AdminMovieServlet extends HttpServlet {

    private static final String ADMIN_EMAIL = "test@test.com";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 检查管理员权限
     */
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        OrdinaryUser user = (OrdinaryUser) session.getAttribute("currentUser");
        return user != null && ADMIN_EMAIL.equals(user.getUserMailbox());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 权限校验
        if (!isAdmin(request)) {
            request.setAttribute("error", "无权限访问管理员后台");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        String action = request.getParameter("action");
        MovieDao dao = new MovieDao();

        // 默认显示列表
        if (action == null || "list".equals(action)) {
            List<?> movies = dao.findAllMovies();
            request.setAttribute("movies", movies);
            request.getRequestDispatcher("/admin.jsp").forward(request, response);
            return;
        }

        // 删除电影
        if ("delete".equals(action)) {
            String movieIdStr = request.getParameter("movieId");
            if (movieIdStr != null && !movieIdStr.isEmpty()) {
                try {
                    int movieId = Integer.parseInt(movieIdStr);
                    dao.deleteMovie(movieId);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "无效的电影ID");
                }
            }
            response.sendRedirect(request.getContextPath() + "/admin/movies");
            return;
        }

        // 切换状态（上下架）
        if ("toggleStatus".equals(action)) {
            String movieIdStr = request.getParameter("movieId");
            String currentStatusStr = request.getParameter("currentStatus");
            if (movieIdStr != null && !movieIdStr.isEmpty() && currentStatusStr != null) {
                try {
                    int movieId = Integer.parseInt(movieIdStr);
                    int currentStatus = Integer.parseInt(currentStatusStr);
                    int newStatus = (currentStatus == 1) ? 0 : 1;
                    dao.updateMovieStatus(movieId, newStatus);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "无效的参数");
                }
            }
            response.sendRedirect(request.getContextPath() + "/admin/movies");
            return;
        }

        // 未知的 action
        response.sendRedirect(request.getContextPath() + "/admin/movies");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 权限校验
        if (!isAdmin(request)) {
            request.setAttribute("error", "无权限访问管理员后台");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        MovieDao dao = new MovieDao();

        // SimpleDateFormat 不是线程安全的，每次创建新实例
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        // 添加电影
        if ("add".equals(action)) {
            try {
                String name = request.getParameter("name");
                String content = request.getParameter("content");
                String genre = request.getParameter("genre");
                String releaseTimeStr = request.getParameter("releaseTime");
                String director = request.getParameter("director");
                String actors = request.getParameter("actors");
                String scoreStr = request.getParameter("score");
                String durationStr = request.getParameter("duration");
                String country = request.getParameter("country");

                if (name == null || name.trim().isEmpty()) {
                    request.setAttribute("error", "电影名称不能为空");
                    response.sendRedirect(request.getContextPath() + "/admin/movies");
                    return;
                }

                Date releaseTime = null;
                if (releaseTimeStr != null && !releaseTimeStr.isEmpty()) {
                    releaseTime = sdf.parse(releaseTimeStr);
                } else {
                    // 默认今天
                    releaseTime = new Date();
                }

                double score = 0.0;
                if (scoreStr != null && !scoreStr.isEmpty()) {
                    score = Double.parseDouble(scoreStr);
                }

                int duration = 0;
                if (durationStr != null && !durationStr.isEmpty()) {
                    duration = Integer.parseInt(durationStr);
                }

                com.movie.entity.MovieInformation movie = new com.movie.entity.MovieInformation();
                movie.setName(name.trim());
                movie.setContent(content != null ? content.trim() : "");
                movie.setGenre(genre != null ? genre.trim() : "");
                movie.setReleaseTime(releaseTime);
                movie.setDirector(director != null ? director.trim() : "");
                movie.setActors(actors != null ? actors.trim() : "");
                movie.setScore(score);
                movie.setDuration(duration);
                movie.setCountry(country != null ? country.trim() : "");

                dao.addMovie(movie);
            } catch (ParseException e) {
                request.setAttribute("error", "日期格式错误");
            } catch (NumberFormatException e) {
                request.setAttribute("error", "数字格式错误");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "添加电影失败");
            }
            response.sendRedirect(request.getContextPath() + "/admin/movies");
            return;
        }

        // 更新电影
        if ("update".equals(action)) {
            try {
                String movieIdStr = request.getParameter("movieId");
                String name = request.getParameter("name");
                String content = request.getParameter("content");
                String genre = request.getParameter("genre");
                String releaseTimeStr = request.getParameter("releaseTime");
                String director = request.getParameter("director");
                String actors = request.getParameter("actors");
                String scoreStr = request.getParameter("score");
                String durationStr = request.getParameter("duration");
                String country = request.getParameter("country");

                if (movieIdStr == null || movieIdStr.isEmpty()) {
                    request.setAttribute("error", "电影ID不能为空");
                    response.sendRedirect(request.getContextPath() + "/admin/movies");
                    return;
                }

                if (name == null || name.trim().isEmpty()) {
                    request.setAttribute("error", "电影名称不能为空");
                    response.sendRedirect(request.getContextPath() + "/admin/movies");
                    return;
                }

                int movieId = Integer.parseInt(movieIdStr);
                Date releaseTime = null;
                if (releaseTimeStr != null && !releaseTimeStr.isEmpty()) {
                    releaseTime = sdf.parse(releaseTimeStr);
                } else {
                    releaseTime = new Date();
                }

                double score = 0.0;
                if (scoreStr != null && !scoreStr.isEmpty()) {
                    score = Double.parseDouble(scoreStr);
                }

                int duration = 0;
                if (durationStr != null && !durationStr.isEmpty()) {
                    duration = Integer.parseInt(durationStr);
                }

                com.movie.entity.MovieInformation movie = new com.movie.entity.MovieInformation();
                movie.setMovieId(movieId);
                movie.setName(name.trim());
                movie.setContent(content != null ? content.trim() : "");
                movie.setGenre(genre != null ? genre.trim() : "");
                movie.setReleaseTime(releaseTime);
                movie.setDirector(director != null ? director.trim() : "");
                movie.setActors(actors != null ? actors.trim() : "");
                movie.setScore(score);
                movie.setDuration(duration);
                movie.setCountry(country != null ? country.trim() : "");

                dao.updateMovie(movie);
            } catch (ParseException e) {
                request.setAttribute("error", "日期格式错误");
            } catch (NumberFormatException e) {
                request.setAttribute("error", "数字格式错误");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "更新电影失败");
            }
            response.sendRedirect(request.getContextPath() + "/admin/movies");
            return;
        }

        // 未知的 action
        response.sendRedirect(request.getContextPath() + "/admin/movies");
    }
}
