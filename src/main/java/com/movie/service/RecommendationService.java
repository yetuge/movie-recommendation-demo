package com.movie.service;

import com.movie.dao.CollectDao;
import com.movie.dao.MovieDao;
import com.movie.dao.ScoreDao;
import com.movie.entity.MovieInformation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecommendationService {

    private MovieDao movieDao;
    private CollectDao collectDao;
    private ScoreDao scoreDao;

    public RecommendationService() {
        this.movieDao = new MovieDao();
        this.collectDao = new CollectDao();
        this.scoreDao = new ScoreDao();
    }

    public List<MovieInformation> getRecommendationsForUser(Integer userId) {
        if (userId == null) {
            return recommendHotMovies();
        }
        return getRecommendations(userId.intValue());
    }

    public List<MovieInformation> getRecommendations(int userId) {
        try {
            List<MovieInformation> collected = collectDao.getUserCollects(userId);
            if (collected.isEmpty()) {
                return recommendHotMovies();
            }

            // 收集用户喜欢的类型
            Set<String> likedGenres = new HashSet<>();
            for (MovieInformation m : collected) {
                if (m.getGenre() != null && !m.getGenre().trim().isEmpty()) {
                    for (String g : m.getGenre().split("[,，]")) {
                        String genre = g.trim();
                        if (!genre.isEmpty()) likedGenres.add(genre);
                    }
                }
            }

            if (likedGenres.isEmpty()) {
                return recommendHotMovies();
            }

            // 已看过的电影ID
            Set<Integer> watchedIds = new HashSet<>();
            for (MovieInformation m : collected) watchedIds.add(m.getMovieId());
            for (MovieInformation m : scoreDao.getHighScoreMovies(userId, 0)) watchedIds.add(m.getMovieId());

            // 从喜欢的类型中推荐未看过的电影
            List<MovieInformation> result = new ArrayList<>();
            for (String genre : likedGenres) {
                for (MovieInformation m : movieDao.getMoviesByGenre(genre)) {
                    if (!watchedIds.contains(m.getMovieId()) && !containsMovie(result, m.getMovieId())) {
                        result.add(m);
                        if (result.size() >= 5) return result;
                    }
                }
            }

            // 不够5部，用热门补充
            if (result.size() < 5) {
                for (MovieInformation m : recommendHotMovies()) {
                    if (result.size() >= 5) break;
                    if (!watchedIds.contains(m.getMovieId()) && !containsMovie(result, m.getMovieId())) {
                        result.add(m);
                    }
                }
            }

            // 全部看过了，直接返回热门（限5部）
            if (result.isEmpty()) {
                return scoreDao.getTopRatedMovies(5);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return recommendHotMovies();
        }
    }

    private boolean containsMovie(List<MovieInformation> list, int movieId) {
        for (MovieInformation m : list) {
            if (m.getMovieId() == movieId) return true;
        }
        return false;
    }

    public List<MovieInformation> recommendHotMovies() {
        return scoreDao.getTopRatedMovies(10);
    }
}
