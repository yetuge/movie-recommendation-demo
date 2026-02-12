package com.movie.dao;

import com.movie.entity.MovieComment;
import com.movie.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 评论数据访问层
 */
public class CommentDao {

    /**
     * 添加评论
     * @param comment 评论对象
     * @return 成功返回 true，失败返回 false
     */
    public boolean addComment(MovieComment comment) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO movie_comment (movie_id, user_id, content, publish_time) " +
                    "VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, comment.getMovieId());
            pstmt.setInt(2, comment.getUserId());
            pstmt.setString(3, comment.getContent());
            pstmt.setTimestamp(4, new java.sql.Timestamp(comment.getPublishTime().getTime()));

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
     * 检查用户是否已评论过该电影
     * @param userId 用户ID
     * @param movieId 电影ID
     * @return 已评论返回 true，未评论返回 false
     */
    public boolean checkUserCommented(int userId, int movieId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM movie_comment " +
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
     * 更新评论
     * @param comment 评论对象（需包含 commentId）
     * @return 成功返回 true，失败返回 false
     */
    public boolean updateComment(MovieComment comment) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE movie_comment " +
                    "SET content = ?, publish_time = ? " +
                    "WHERE comment_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, comment.getContent());
            pstmt.setTimestamp(2, new java.sql.Timestamp(comment.getPublishTime().getTime()));
            pstmt.setInt(3, comment.getCommentId());

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
     * 获取用户对某电影的评论
     * @param userId 用户ID
     * @param movieId 电影ID
     * @return 评论对象，未评论返回 null
     */
    public MovieComment getUserComment(int userId, int movieId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        MovieComment comment = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT comment_id, movie_id, user_id, content, publish_time " +
                    "FROM movie_comment " +
                    "WHERE user_id = ? AND movie_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, movieId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                comment = new MovieComment();
                comment.setCommentId(rs.getInt("comment_id"));
                comment.setMovieId(rs.getInt("movie_id"));
                comment.setUserId(rs.getInt("user_id"));
                comment.setContent(rs.getString("content"));
                comment.setPublishTime(rs.getTimestamp("publish_time"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return comment;
    }
}
