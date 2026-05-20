package com.movie.controller;

import com.movie.entity.MovieInformation;
import com.movie.entity.OrdinaryUser;
import com.movie.service.FavoriteService;
import com.movie.service.ServiceResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Favorite controller for web pages.
 */
@WebServlet("/collect")
public class CollectServlet extends HttpServlet {

    private FavoriteService favoriteService;

    @Override
    public void init() {
        this.favoriteService = new FavoriteService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        OrdinaryUser user = (OrdinaryUser) session.getAttribute("currentUser");
        if (user == null) {
            response.sendRedirect("signin.jsp");
            return;
        }

        List<MovieInformation> movieList = favoriteService.getFavoritesByUserId(user.getUserId());
        request.setAttribute("myCollections", movieList);
        request.getRequestDispatcher("collect.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        OrdinaryUser user = (OrdinaryUser) session.getAttribute("currentUser");
        if (user == null) {
            response.sendRedirect("signin.jsp");
            return;
        }

        Integer movieId = parseInt(request.getParameter("movieId"));
        String action = trim(request.getParameter("action"));

        if (movieId == null || action == null || action.isEmpty()) {
            request.setAttribute("errorMsg", "Invalid parameters");
            response.sendRedirect("movieList");
            return;
        }

        ServiceResult result;
        if ("add".equals(action)) {
            result = favoriteService.addFavorite(user.getUserId(), movieId);
        } else if ("remove".equals(action)) {
            result = favoriteService.removeFavorite(user.getUserId(), movieId);
        } else {
            request.setAttribute("errorMsg", "Unsupported action");
            response.sendRedirect("movieDetail?movieId=" + movieId);
            return;
        }

        if (result.isSuccess()) {
            response.sendRedirect("movieDetail?movieId=" + movieId);
            return;
        }

        request.setAttribute("errorMsg", result.getMessage());
        request.getRequestDispatcher("DisplayMovieInformation.jsp").forward(request, response);
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private Integer parseInt(String value) {
        String trimmed = trim(value);
        if (trimmed == null || trimmed.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(trimmed);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
