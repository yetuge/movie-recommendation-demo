package com.movie.service;

import com.movie.dao.CollectDao;
import com.movie.dao.MovieDao;
import com.movie.dao.ScoreDao;
import com.movie.entity.MovieInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电影推荐服务
 */
public class RecommendationService {

    private MovieDao movieDao;
    private CollectDao collectDao;
    private ScoreDao scoreDao;

    public RecommendationService() {
        this.movieDao = new MovieDao();
        this.collectDao = new CollectDao();
        this.scoreDao = new ScoreDao();
    }

    /**
     * 策略 A：基于内容的推荐
     * 根据用户收藏或高分评价的电影，推荐同类型但未看过的电影
     * @param userId 用户ID
     * @return 推荐电影列表（最多10部）
     */
    public List<MovieInformation> recommendByContent(int userId) {
        // 1. 获取用户收藏的电影列表
        List<MovieInformation> collectedMovies = collectDao.getUserCollects(userId);

        // 2. 获取用户高评分的电影（评分 > 8分）
        List<MovieInformation> highScoreMovies = scoreDao.getHighScoreMovies(userId, 8.0);

        // 3. 合并并统计类型频率
        Map<String, Integer> genreCount = new HashMap<>();
        for (MovieInformation movie : collectedMovies) {
            countGenre(genreCount, movie.getGenre());
        }
        for (MovieInformation movie : highScoreMovies) {
            countGenre(genreCount, movie.getGenre());
        }

        // 4. 如果没有历史数据，返回热门推荐（策略 B）
        if (genreCount.isEmpty()) {
            return recommendHotMovies();
        }

        // 5. 找出最热门的类型
        String topGenre = findTopGenre(genreCount);

        // 6. 查询该类型下、用户未看过的电影
        List<MovieInformation> candidateMovies = movieDao.getMoviesByGenre(topGenre);

        // 7. 过滤掉用户已收藏或评分的电影
        List<MovieInformation> recommendations = new ArrayList<>();
        for (MovieInformation movie : candidateMovies) {
            int movieId = movie.getMovieId();
            // 检查用户是否收藏过
            if (!collectDao.checkUserCollected(userId, movieId)) {
                // 检查用户是否评分过
                if (!scoreDao.checkUserScored(userId, movieId)) {
                    recommendations.add(movie);
                    if (recommendations.size() >= 10) {
                        break;
                    }
                }
            }
        }

        // 8. 如果推荐不足10部，用热门推荐补充
        if (recommendations.size() < 10) {
            List<MovieInformation> hotMovies = recommendHotMovies();
            for (MovieInformation movie : hotMovies) {
                if (!recommendations.contains(movie) && recommendations.size() < 10) {
                    recommendations.add(movie);
                }
            }
        }

        return recommendations;
    }

    /**
     * 策略 B：热门推荐（冷启动备用）
     * 基于推荐平均分最高的电影
     * @return 热门电影列表（最多10部）
     */
    public List<MovieInformation> recommendHotMovies() {
        return scoreDao.getTopRatedMovies(10);
    }

    /**
     * 获取推荐电影（优先策略 A，回退到策略 B）
     * @param userId 用户ID
     * @return 推荐电影列表
     */
    public List<MovieInformation> getRecommendations(int userId) {
        try {
            List<MovieInformation> recommendations = recommendByContent(userId);
            if (recommendations.isEmpty()) {
                return recommendHotMovies();
            }
            return recommendations;
        } catch (Exception e) {
            e.printStackTrace();
            return recommendHotMovies();
        }
    }

    /**
     * 统计类型出现次数（支持逗号分隔的多类型）
     */
    private void countGenre(Map<String, Integer> genreCount, String genres) {
        if (genres == null || genres.trim().isEmpty()) {
            return;
        }
        // 假设类型可能用逗号分隔，如 "动作,科幻"
        String[] genreArray = genres.split("[,，]");
        for (String genre : genreArray) {
            genre = genre.trim();
            if (!genre.isEmpty()) {
                genreCount.put(genre, genreCount.getOrDefault(genre, 0) + 1);
            }
        }
    }

    /**
     * 找出出现频率最高的类型
     */
    private String findTopGenre(Map<String, Integer> genreCount) {
        String topGenre = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : genreCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                topGenre = entry.getKey();
            }
        }

        return topGenre;
    }
}
