package com.movie.controller;

import com.movie.dao.MovieDao;
import com.movie.entity.MovieInformation;
import com.movie.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 电影 API Servlet
 * 支持 Web (JSP) 和 Android (JSON) 两种请求方式
 */
@WebServlet("/api/movie")
public class ApiMovieServlet extends HttpServlet {

    private MovieDao movieDao;

    @Override
    public void init() {
        this.movieDao = new MovieDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 检查是否为 API 请求（通过 format 参数 或 Accept 头）
        if (isApiRequest(request)) {
            handleApiRequest(request, response);
        } else {
            handleWebRequest(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // POST 请求同样处理
        doGet(request, response);
    }

    /**
     * 判断是否为 API 请求
     */
    private boolean isApiRequest(HttpServletRequest request) {
        // 方式1: 检查 URL 参数 format=json
        String format = request.getParameter("format");
        if ("json".equalsIgnoreCase(format)) {
            return true;
        }

        // 方式2: 检查 Accept 头
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            return true;
        }

        // 方式3: 检查 User-Agent 是否包含 Android
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null && userAgent.contains("Android")) {
            return true;
        }

        return false;
    }

    /**
     * 处理 API 请求 - 返回 JSON
     */
    private void handleApiRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // 设置响应类型和编码
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 获取操作类型
        String action = request.getParameter("action");
        String json = "";

        if ("list".equals(action)) {
            // 获取正在上映的电影列表（前台专用）
            List<MovieInformation> movieList = movieDao.findShowingMovies();
            json = JsonUtil.toJson(movieList);
        } else if ("detail".equals(action)) {
            // 获取电影详情
            String movieIdStr = request.getParameter("movieId");
            if (movieIdStr != null && !movieIdStr.isEmpty()) {
                int movieId = Integer.parseInt(movieIdStr);
                MovieInformation movie = movieDao.findMovieById(movieId);
                json = JsonUtil.toJson(movie);
            } else {
                json = "{\"error\":\"参数错误，缺少 movieId\"}";
            }
        } else if ("search".equals(action)) {
            // 搜索电影
            String keyword = request.getParameter("keyword");
            if (keyword != null && !keyword.isEmpty()) {
                List<MovieInformation> movieList = movieDao.searchMovies(keyword);
                json = JsonUtil.toJson(movieList);
            } else {
                json = "{\"error\":\"参数错误，缺少 keyword\"}";
            }
        } else if ("genre".equals(action)) {
            // 按类型筛选
            String genre = request.getParameter("genre");
            if (genre != null && !genre.isEmpty()) {
                List<MovieInformation> movieList = movieDao.getMoviesByGenre(genre);
                json = JsonUtil.toJson(movieList);
            } else {
                json = "{\"error\":\"参数错误，缺少 genre\"}";
            }
        } else {
            // 默认返回电影列表
            List<MovieInformation> movieList = movieDao.findAllMovies();
            json = JsonUtil.toJson(movieList);
        }

        // 输出 JSON
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    /**
     * 处理 Web 请求 - 转发到 JSP
     */
    private void handleWebRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 获取电影列表
        List<MovieInformation> movieList = movieDao.findAllMovies();

        // 存入 request 域
        request.setAttribute("movieList", movieList);

        // 转发到 JSP
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
