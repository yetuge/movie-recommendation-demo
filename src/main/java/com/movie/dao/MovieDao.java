package com.movie.dao;

import com.movie.entity.MovieComment;
import com.movie.entity.MovieInformation;
import com.movie.entity.MovieScore;
import com.movie.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电影数据访问层
 */
public class MovieDao {

    /**
     * 获取所有正在上映的电影列表（包含计算的平均分）
     * @return 电影列表
     */
    public List<MovieInformation> findAllMovies() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MovieInformation> movieList = new ArrayList<>();
        Map<Integer, Double> scoreMap = new HashMap<>();

        try {
            conn = DBUtil.getConnection();

            // 首先获取所有电影的平均分
            String scoreSql = "SELECT movie_id, AVG(score) as avgScore " +
                    "FROM movie_score " +
                    "GROUP BY movie_id";
            pstmt = conn.prepareStatement(scoreSql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                scoreMap.put(rs.getInt("movie_id"), rs.getDouble("avgScore"));
            }
            DBUtil.close(null, pstmt, rs);

            // 获取所有电影信息，按上映时间倒序排列
            String sql = "SELECT movie_id, movie_name, plot, genre, release_time, director, main_actors, duration, country, score, is_showing, image_url " +
                    "FROM movie_information " +
                    "ORDER BY release_time DESC";
            pstmt = conn.prepareStatement(sql);
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

                // 设置平均分（如果没有评分，设为 0）
                Integer movieId = movie.getMovieId();
                movie.setScore(scoreMap.containsKey(movieId) ? scoreMap.get(movieId) : 0.0);

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
     * 获取正在上映的电影列表（前台专用，包含计算的平均分）
     * @return 电影列表
     */
    public List<MovieInformation> findShowingMovies() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MovieInformation> movieList = new ArrayList<>();
        Map<Integer, Double> scoreMap = new HashMap<>();

        try {
            conn = DBUtil.getConnection();

            // 首先获取所有电影的平均分
            String scoreSql = "SELECT movie_id, AVG(score) as avgScore " +
                    "FROM movie_score " +
                    "GROUP BY movie_id";
            pstmt = conn.prepareStatement(scoreSql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                scoreMap.put(rs.getInt("movie_id"), rs.getDouble("avgScore"));
            }
            DBUtil.close(null, pstmt, rs);

            // 获取正在上映的电影信息，按上映时间倒序排列
            String sql = "SELECT movie_id, movie_name, plot, genre, release_time, director, main_actors, duration, country, score, is_showing, image_url " +
                    "FROM movie_information " +
                    "WHERE is_showing = 1 " +
                    "ORDER BY release_time DESC";
            pstmt = conn.prepareStatement(sql);
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

                // 设置平均分（如果没有评分，设为 0）
                Integer movieId = movie.getMovieId();
                movie.setScore(scoreMap.containsKey(movieId) ? scoreMap.get(movieId) : 0.0);

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
     * 获取特定电影详情（包含计算的平均分）
     * @param id 电影ID
     * @return 电影详情，不存在返回 null
     */
    public MovieInformation findMovieById(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        MovieInformation movie = null;

        try {
            conn = DBUtil.getConnection();

            // 获取电影信息
            String sql = "SELECT movie_id, movie_name, plot, genre, release_time, director, main_actors, duration, country, score, is_showing, image_url " +
                    "FROM movie_information " +
                    "WHERE movie_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                movie = new MovieInformation();
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
            }
            DBUtil.close(null, pstmt, rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return movie;
    }

    /**
     * 根据电影名或导演模糊查询（包含计算的平均分）
     * @param keyword 关键词
     * @return 匹配的电影列表
     */
    public List<MovieInformation> searchMovies(String keyword) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MovieInformation> movieList = new ArrayList<>();
        Map<Integer, Double> scoreMap = new HashMap<>();

        try {
            conn = DBUtil.getConnection();

            // 首先获取所有电影的平均分
            String scoreSql = "SELECT movie_id, AVG(score) as avgScore " +
                    "FROM movie_score " +
                    "GROUP BY movie_id";
            pstmt = conn.prepareStatement(scoreSql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                scoreMap.put(rs.getInt("movie_id"), rs.getDouble("avgScore"));
            }
            DBUtil.close(null, pstmt, rs);

            // 搜索电影（仅显示正在上映的电影）
            String sql = "SELECT movie_id, movie_name, plot, genre, release_time, director, main_actors, duration, country, score, image_url " +
                    "FROM movie_information " +
                    "WHERE (movie_name LIKE ? OR director LIKE ?) AND is_showing = 1 " +
                    "ORDER BY movie_id";
            pstmt = conn.prepareStatement(sql);
            String pattern = "%" + keyword + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
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

                // 设置平均分
                Integer movieId = movie.getMovieId();
                movie.setScore(scoreMap.containsKey(movieId) ? scoreMap.get(movieId) : 0.0);

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
     * 根据类型筛选电影（包含计算的平均分）
     * @param genre 电影类型
     * @return 该类型的电影列表
     */
    public List<MovieInformation> getMoviesByGenre(String genre) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MovieInformation> movieList = new ArrayList<>();
        Map<Integer, Double> scoreMap = new HashMap<>();

        try {
            conn = DBUtil.getConnection();

            // 首先获取所有电影的平均分
            String scoreSql = "SELECT movie_id, AVG(score) as avgScore " +
                    "FROM movie_score " +
                    "GROUP BY movie_id";
            pstmt = conn.prepareStatement(scoreSql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                scoreMap.put(rs.getInt("movie_id"), rs.getDouble("avgScore"));
            }
            DBUtil.close(null, pstmt, rs);

            // 按类型筛选（仅显示正在上映的电影）
            String sql = "SELECT movie_id, movie_name, plot, genre, release_time, director, main_actors, duration, country, score, image_url " +
                    "FROM movie_information " +
                    "WHERE genre = ? AND is_showing = 1 " +
                    "ORDER BY movie_id";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, genre);
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

                // 设置平均分
                Integer movieId = movie.getMovieId();
                movie.setScore(scoreMap.containsKey(movieId) ? scoreMap.get(movieId) : 0.0);

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
     * 获取电影的所有评论（带用户名）
     * @param movieId 电影ID
     * @return 评论列表
     */
    public List<MovieComment> getCommentsByMovieId(int movieId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MovieComment> commentList = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT c.comment_id, c.movie_id, c.user_id, c.content, c.publish_time, u.user_name " +
                    "FROM movie_comment c " +
                    "LEFT JOIN ordinary_user u ON c.user_id = u.user_id " +
                    "WHERE c.movie_id = ? " +
                    "ORDER BY c.publish_time DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, movieId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MovieComment comment = new MovieComment();
                comment.setCommentId(rs.getInt("comment_id"));
                comment.setMovieId(rs.getInt("movie_id"));
                comment.setUserId(rs.getInt("user_id"));
                comment.setContent(rs.getString("content"));
                comment.setPublishTime(rs.getTimestamp("publish_time"));
                comment.setUserName(rs.getString("user_name"));
                commentList.add(comment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return commentList;
    }

    /**
     * 获取电影的所有评分
     * @param movieId 电影ID
     * @return 评分列表
     */
    public List<MovieScore> getScoresByMovieId(int movieId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MovieScore> scoreList = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT score_id, movie_id, user_id, score, publish_time " +
                    "FROM movie_score " +
                    "WHERE movie_id = ? " +
                    "ORDER BY publish_time DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, movieId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MovieScore score = new MovieScore();
                score.setScoreId(rs.getInt("score_id"));
                score.setMovieId(rs.getInt("movie_id"));
                score.setUserId(rs.getInt("user_id"));
                score.setScore(rs.getDouble("score"));
                score.setPublishTime(rs.getTimestamp("publish_time"));
                scoreList.add(score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return scoreList;
    }

    /**
     * 获取电影的平均分
     * @param movieId 电影ID
     * @return 平均分，如果没有评分返回 0.0
     */
    public double getAverageScore(int movieId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        double avgScore = 0.0;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT AVG(score) as avgScore FROM movie_score WHERE movie_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, movieId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Double score = rs.getDouble("avgScore");
                avgScore = score != null ? score : 0.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return avgScore;
    }

    /**
     * 获取评分最高的前 N 部电影（热门推荐）
     * @param limit 返回数量
     * @return 电影列表
     */
    public List<MovieInformation> findTopRatedMovies(int limit) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MovieInformation> movieList = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT m.movie_id, m.movie_name, m.plot, m.genre, m.release_time, " +
                    "m.director, m.main_actors, m.duration, m.country, m.image_url, " +
                    "AVG(s.score) as avg_score " +
                    "FROM movie_information m " +
                    "JOIN movie_score s ON m.movie_id = s.movie_id " +
                    "WHERE m.is_showing = 1 " +
                    "GROUP BY m.movie_id " +
                    "ORDER BY avg_score DESC " +
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
                movie.setImageUrl(rs.getString("image_url"));
                movie.setScore(rs.getDouble("avg_score"));
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
     * 根据类型查找电影（基于内容推荐）
     * @param genre 电影类型
     * @param limit 返回数量
     * @return 电影列表
     */
    public List<MovieInformation> findMoviesByGenre(String genre, int limit) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MovieInformation> movieList = new ArrayList<>();
        Map<Integer, Double> scoreMap = new HashMap<>();

        try {
            conn = DBUtil.getConnection();

            // 获取所有电影的平均分
            String scoreSql = "SELECT movie_id, AVG(score) as avgScore " +
                    "FROM movie_score " +
                    "GROUP BY movie_id";
            pstmt = conn.prepareStatement(scoreSql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                scoreMap.put(rs.getInt("movie_id"), rs.getDouble("avgScore"));
            }
            DBUtil.close(null, pstmt, rs);

            // 按类型筛选
            String sql = "SELECT movie_id, movie_name, plot, genre, release_time, director, main_actors, duration, country, score, image_url " +
                    "FROM movie_information " +
                    "WHERE genre = ? " +
                    "ORDER BY release_time DESC " +
                    "LIMIT ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, genre);
            pstmt.setInt(2, limit);
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

                // 设置平均分
                Integer movieId = movie.getMovieId();
                movie.setScore(scoreMap.containsKey(movieId) ? scoreMap.get(movieId) : 0.0);

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
     * 添加新电影
     * @param movie 电影信息对象
     * @return 成功返回 true，失败返回 false
     */
    public boolean addMovie(MovieInformation movie) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO movie_information (movie_name, plot, genre, release_time, director, main_actors, score, duration, country) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, movie.getName());
            pstmt.setString(2, movie.getContent());
            pstmt.setString(3, movie.getGenre());
            pstmt.setDate(4, new java.sql.Date(movie.getReleaseTime().getTime()));
            pstmt.setString(5, movie.getDirector());
            pstmt.setString(6, movie.getActors());
            pstmt.setDouble(7, movie.getScore());
            pstmt.setInt(8, movie.getDuration());
            pstmt.setString(9, movie.getCountry());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
    }

    /**
     * 更新电影信息
     * @param movie 电影信息对象
     * @return 成功返回 true，失败返回 false
     */
    public boolean updateMovie(MovieInformation movie) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE movie_information SET " +
                    "movie_name = ?, plot = ?, genre = ?, release_time = ?, " +
                    "director = ?, main_actors = ?, score = ?, duration = ?, country = ? " +
                    "WHERE movie_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, movie.getName());
            pstmt.setString(2, movie.getContent());
            pstmt.setString(3, movie.getGenre());
            pstmt.setDate(4, new java.sql.Date(movie.getReleaseTime().getTime()));
            pstmt.setString(5, movie.getDirector());
            pstmt.setString(6, movie.getActors());
            pstmt.setDouble(7, movie.getScore());
            pstmt.setInt(8, movie.getDuration());
            pstmt.setString(9, movie.getCountry());
            pstmt.setInt(10, movie.getMovieId());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
    }

    /**
     * 删除电影（包含关联的评分、评论、收藏）
     * @param movieId 电影ID
     * @return 成功返回 true，失败返回 false
     */
    public boolean deleteMovie(int movieId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // 删除关联的评分
            String deleteScores = "DELETE FROM movie_score WHERE movie_id = ?";
            pstmt = conn.prepareStatement(deleteScores);
            pstmt.setInt(1, movieId);
            pstmt.executeUpdate();
            DBUtil.close(null, pstmt, null);

            // 删除关联的评论
            String deleteComments = "DELETE FROM movie_comment WHERE movie_id = ?";
            pstmt = conn.prepareStatement(deleteComments);
            pstmt.setInt(1, movieId);
            pstmt.executeUpdate();
            DBUtil.close(null, pstmt, null);

            // 删除关联的收藏
            String deleteCollects = "DELETE FROM collect WHERE movie_id = ?";
            pstmt = conn.prepareStatement(deleteCollects);
            pstmt.setInt(1, movieId);
            pstmt.executeUpdate();
            DBUtil.close(null, pstmt, null);

            // 删除电影
            String deleteMovie = "DELETE FROM movie_information WHERE movie_id = ?";
            pstmt = conn.prepareStatement(deleteMovie);
            pstmt.setInt(1, movieId);
            int rows = pstmt.executeUpdate();

            conn.commit();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            DBUtil.close(conn, pstmt, null);
        }
    }

    /**
     * 更新电影上下架状态
     * @param movieId 电影ID
     * @param newStatus 新状态（1=上架，0=下架）
     * @return 成功返回 true，失败返回 false
     */
    public boolean updateMovieStatus(int movieId, int newStatus) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE movie_information SET is_showing = ? WHERE movie_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, newStatus);
            pstmt.setInt(2, movieId);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
    }
}
