package com.movie.dao;

import com.movie.entity.Collect;
import com.movie.entity.MovieInformation;
import com.movie.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏数据访问层
 */
public class CollectDao {

    /**
     * 添加收藏
     * @param collect 收藏对象
     * @return 成功返回 true，失败返回 false
     */
    public boolean addCollect(Collect collect) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO collect (movie_id, user_id, collect_time) " +
                    "VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, collect.getMovieId());
            pstmt.setInt(2, collect.getUserId());
            pstmt.setTimestamp(3, new java.sql.Timestamp(collect.getCollectTime().getTime()));

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
     * 检查用户是否已收藏该电影
     * @param userId 用户ID
     * @param movieId 电影ID
     * @return 已收藏返回 true，未收藏返回 false
     */
    public boolean checkUserCollected(int userId, int movieId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM collect " +
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
     * 取消收藏
     * @param userId 用户ID
     * @param movieId 电影ID
     * @return 成功返回 true，失败返回 false
     */
    public boolean removeCollect(int userId, int movieId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM collect " +
                    "WHERE user_id = ? AND movie_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, movieId);

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
     * 获取用户的收藏列表（包含电影信息）
     * @param userId 用户ID
     * @return 收藏的电影列表
     */
    public List<MovieInformation> getUserCollects(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MovieInformation> movieList = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT m.movie_id, m.movie_name, m.plot, m.genre, m.release_time, m.director, m.main_actors, m.duration, m.country, m.score " +
                    "FROM collect c " +
                    "JOIN movie_information m ON c.movie_id = m.movie_id " +
                    "WHERE c.user_id = ? AND m.is_showing = 1 " +
                    "ORDER BY c.collect_time DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
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
     * 获取用户最近收藏的一部电影的类型（用于推荐）
     * @param userId 用户ID
     * @return 电影类型，如果无收藏返回 null
     */
    public String getUserRecentCollectGenre(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String genre = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT m.genre " +
                    "FROM collect c " +
                    "JOIN movie_information m ON c.movie_id = m.movie_id " +
                    "WHERE c.user_id = ? " +
                    "ORDER BY c.collect_time DESC " +
                    "LIMIT 1";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                genre = rs.getString("genre");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return genre;
    }
}
