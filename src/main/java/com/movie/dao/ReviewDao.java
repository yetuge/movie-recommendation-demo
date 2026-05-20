package com.movie.dao;

import com.movie.entity.Review;
import com.movie.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 影评数据访问层
public class ReviewDao {

    // 查询电影影评列表（评论+评分）
    public List<Review> findReviewsByMovieId(int movieId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Review> reviews = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT c.movie_id, c.user_id, u.user_name, u.user_mailbox, c.content, s.score, c.publish_time " +
                    "FROM movie_comment c " +
                    "JOIN ordinary_user u ON c.user_id = u.user_id " +
                    "LEFT JOIN movie_score s ON s.movie_id = c.movie_id AND s.user_id = c.user_id " +
                    "WHERE c.movie_id = ? " +
                    "ORDER BY c.publish_time DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, movieId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Review review = new Review();
                review.setMovieId(rs.getInt("movie_id"));
                review.setUserId(rs.getInt("user_id"));
                review.setUserName(rs.getString("user_name"));
                review.setUserMailbox(rs.getString("user_mailbox"));
                review.setContent(rs.getString("content"));
                double scoreValue = rs.getDouble("score");
                review.setScore(rs.wasNull() ? null : scoreValue);
                review.setCreateTime(rs.getTimestamp("publish_time"));
                reviews.add(review);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return reviews;
    }

    // 发布或更新影评与评分
    public boolean addReview(int movieId, String userMailbox, String content, double score) {
        Connection conn = null;
        PreparedStatement findUserStmt = null;
        PreparedStatement findCommentStmt = null;
        PreparedStatement insertCommentStmt = null;
        PreparedStatement updateCommentStmt = null;
        PreparedStatement findScoreStmt = null;
        PreparedStatement insertScoreStmt = null;
        PreparedStatement updateScoreStmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            if (conn == null) {
                return false;
            }

            conn.setAutoCommit(false);

            String findUserSql = "SELECT user_id FROM ordinary_user WHERE user_mailbox = ?";
            findUserStmt = conn.prepareStatement(findUserSql);
            findUserStmt.setString(1, userMailbox);
            rs = findUserStmt.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                return false;
            }

            int userId = rs.getInt("user_id");
            rs.close();
            rs = null;

            Timestamp now = new Timestamp(new Date().getTime());

            String findCommentSql = "SELECT comment_id FROM movie_comment WHERE movie_id = ? AND user_id = ?";
            findCommentStmt = conn.prepareStatement(findCommentSql);
            findCommentStmt.setInt(1, movieId);
            findCommentStmt.setInt(2, userId);
            rs = findCommentStmt.executeQuery();

            if (rs.next()) {
                int commentId = rs.getInt("comment_id");
                rs.close();
                rs = null;

                String updateCommentSql = "UPDATE movie_comment SET content = ?, publish_time = ? WHERE comment_id = ?";
                updateCommentStmt = conn.prepareStatement(updateCommentSql);
                updateCommentStmt.setString(1, content);
                updateCommentStmt.setTimestamp(2, now);
                updateCommentStmt.setInt(3, commentId);
                updateCommentStmt.executeUpdate();
            } else {
                rs.close();
                rs = null;

                String insertCommentSql = "INSERT INTO movie_comment (movie_id, user_id, content, publish_time) VALUES (?, ?, ?, ?)";
                insertCommentStmt = conn.prepareStatement(insertCommentSql);
                insertCommentStmt.setInt(1, movieId);
                insertCommentStmt.setInt(2, userId);
                insertCommentStmt.setString(3, content);
                insertCommentStmt.setTimestamp(4, now);
                insertCommentStmt.executeUpdate();
            }

            String findScoreSql = "SELECT score_id FROM movie_score WHERE movie_id = ? AND user_id = ?";
            findScoreStmt = conn.prepareStatement(findScoreSql);
            findScoreStmt.setInt(1, movieId);
            findScoreStmt.setInt(2, userId);
            rs = findScoreStmt.executeQuery();

            if (rs.next()) {
                int scoreId = rs.getInt("score_id");
                rs.close();
                rs = null;

                String updateScoreSql = "UPDATE movie_score SET score = ?, publish_time = ? WHERE score_id = ?";
                updateScoreStmt = conn.prepareStatement(updateScoreSql);
                updateScoreStmt.setDouble(1, score);
                updateScoreStmt.setTimestamp(2, now);
                updateScoreStmt.setInt(3, scoreId);
                updateScoreStmt.executeUpdate();
            } else {
                rs.close();
                rs = null;

                String insertScoreSql = "INSERT INTO movie_score (movie_id, user_id, score, publish_time) VALUES (?, ?, ?, ?)";
                insertScoreStmt = conn.prepareStatement(insertScoreSql);
                insertScoreStmt.setInt(1, movieId);
                insertScoreStmt.setInt(2, userId);
                insertScoreStmt.setDouble(3, score);
                insertScoreStmt.setTimestamp(4, now);
                insertScoreStmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ignore) {
                }
            }
            tryClose(findUserStmt);
            tryClose(findCommentStmt);
            tryClose(insertCommentStmt);
            tryClose(updateCommentStmt);
            tryClose(findScoreStmt);
            tryClose(insertScoreStmt);
            tryClose(updateScoreStmt);
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (Exception ignore) {
                }
                DBUtil.close(conn);
            }
        }
    }

    private void tryClose(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (Exception ignore) {
            }
        }
    }
}
