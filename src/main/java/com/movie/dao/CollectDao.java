package com.movie.dao;

import com.movie.entity.Collect;
import com.movie.entity.MovieInformation;
import com.movie.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

// 收藏数据访问层
public class CollectDao {

    
    // 添加收藏记录
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

    
    // 检查用户是否已收藏该电影
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

    
    // 取消收藏
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

    
    // 查询用户收藏列表（按用户ID）
    public List<MovieInformation> getUserCollects(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MovieInformation> movieList = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT m.movie_id, m.movie_name, m.plot, m.genre, m.release_time, m.director, m.main_actors, m.duration, m.country, m.score, m.image_url " +
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
                movie.setImageUrl(rs.getString("image_url"));
                movieList.add(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return movieList;
    }

    
    // 查询用户最近一次收藏的电影类型
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

    
    // 按邮箱检查电影是否已收藏
    public boolean checkIsFavorited(String userMailbox, int movieId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) " +
                    "FROM collect c " +
                    "JOIN ordinary_user u ON c.user_id = u.user_id " +
                    "WHERE u.user_mailbox = ? AND c.movie_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userMailbox);
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

    
    // 查询用户收藏列表（按邮箱）
    public List<MovieInformation> getUserCollectsByMailbox(String userMailbox) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MovieInformation> movieList = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT m.movie_id, m.movie_name, m.plot, m.genre, m.release_time, " +
                    "m.director, m.main_actors, m.duration, m.country, m.score, m.is_showing, m.image_url " +
                    "FROM collect c " +
                    "JOIN ordinary_user u ON c.user_id = u.user_id " +
                    "JOIN movie_information m ON c.movie_id = m.movie_id " +
                    "WHERE u.user_mailbox = ? " +
                    "ORDER BY c.collect_time DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userMailbox);
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
                movie.setIsShowing(rs.getInt("is_showing"));
                movie.setImageUrl(rs.getString("image_url"));
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