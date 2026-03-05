package com.movie.dao;

import com.movie.entity.MovieInformation;
import com.movie.entity.MovieScore;
import com.movie.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 评分数据访问层
 */
public class ScoreDao {

    /**
     * 添加评分
     * @param score 评分对象
     * @return 成功返回 true，失败返回 false
     */
    public boolean addScore(MovieScore score) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO movie_score (movie_id, user_id, score, publish_time) " +
                    "VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, score.getMovieId());
            pstmt.setInt(2, score.getUserId());
            pstmt.setDouble(3, score.getScore());
            pstmt.setTimestamp(4, new java.sql.Timestamp(score.getPublishTime().getTime()));

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 检查用户是否已评分过该电影
     * @param userId 用户ID
     * @param movieId 电影ID
     * @return 已评分返回 true，未评分返回 false
     */
    public boolean checkUserScored(int userId, int movieId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM movie_score " +
                    "WHERE user_id = ? AND movie_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, movieId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return false;
    }

    /**
     * 更新评分
     * @param score 评分对象（需包含 scoreId）
     * @return 成功返回 true，失败返回 false
     */
    public boolean updateScore(MovieScore score) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE movie_score " +
                    "SET score = ?, publish_time = ? " +
                    "WHERE score_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, score.getScore());
            pstmt.setTimestamp(2, new java.sql.Timestamp(score.getPublishTime().getTime()));
            pstmt.setInt(3, score.getScoreId());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 获取用户对某电影的评分
     * @param userId 用户ID
     * @param movieId 电影ID
     * @return 评分对象，未评分返回 null
     */
    public MovieScore getUserScore(int userId, int movieId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        MovieScore score = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT score_id, movie_id, user_id, score, publish_time " +
                    "FROM movie_score " +
                    "WHERE user_id = ? AND movie_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, movieId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                score = new MovieScore();
                score.setScoreId(rs.getInt("score_id"));
                score.setMovieId(rs.getInt("movie_id"));
                score.setUserId(rs.getInt("user_id"));
                score.setScore(rs.getDouble("score"));
                score.setPublishTime(rs.getTimestamp("publish_time"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return score;
    }

    /**
     * 获取用户高评分的电影（评分 >= minScore）
     * @param userId 用户ID
     * @param minScore 最低分数
     * @return 高评分电影列表
     */
    public List<MovieInformation> getHighScoreMovies(int userId, double minScore) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MovieInformation> movieList = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT m.movie_id, m.movie_name, m.plot, m.genre, m.release_time, m.director, m.main_actors, m.duration, m.country, m.score " +
                    "FROM movie_score s " +
                    "JOIN movie_information m ON s.movie_id = m.movie_id " +
                    "WHERE s.user_id = ? AND s.score >= ? AND m.is_showing = 1 " +
                    "ORDER BY s.score DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setDouble(2, minScore);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MovieInformation movie = new MovieInformation();
                movie.setMovieId(rs.getInt("movie_id"));
                movie.setName(rs.getString("movie_name"));
                movie.setContent(rs.getString("plot"));
                movie.setGenre(rs.getString("genre"));
                movie.setReleaseTime(rs.getDate("release_time"));
                movie.setDirector(rs.getString("director"));
                movie.setActors(rs.getString("main_actors"));
                movie.setDuration(rs.getInt("duration"));
                movie.setCountry(rs.getString("country"));
                movie.setScore(rs.getDouble("score"));
                movieList.add(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return movieList;
    }

    /**
     * 获取平均分最高的电影（热门推荐）
     * @param limit 返回数量
     * @return 热门电影列表
     */
    public List<MovieInformation> getTopRatedMovies(int limit) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MovieInformation> movieList = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT m.movie_id, m.movie_name, m.plot, m.genre, m.release_time, m.director, m.main_actors, m.duration, m.country, " +
                    "AVG(s.score) as avgScore " +
                    "FROM movie_score s " +
                    "JOIN movie_information m ON s.movie_id = m.movie_id " +
                    "GROUP BY s.movie_id " +
                    "HAVING COUNT(*) >= 2 AND m.is_showing = 1 " +
                    "ORDER BY avgScore DESC, COUNT(*) DESC " +
                    "LIMIT ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MovieInformation movie = new MovieInformation();
                movie.setMovieId(rs.getInt("movie_id"));
                movie.setName(rs.getString("movie_name"));
                movie.setContent(rs.getString("plot"));
                movie.setGenre(rs.getString("genre"));
                movie.setReleaseTime(rs.getDate("release_time"));
                movie.setDirector(rs.getString("director"));
                movie.setActors(rs.getString("main_actors"));
                movie.setDuration(rs.getInt("duration"));
                movie.setCountry(rs.getString("country"));
                movie.setScore(rs.getDouble("avgScore"));
                movieList.add(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return movieList;
    }
}
