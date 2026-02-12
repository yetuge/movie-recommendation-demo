package com.movie.entity;

import java.util.Date;

/**
 * 电影评分实体类
 */
public class MovieScore {

    private int scoreId;            // 评分ID
    private int movieId;            // 电影ID
    private int userId;            // 用户ID
    private double score;           // 评分
    private Date publishTime;       // 发布时间

    public MovieScore() {
    }

    public MovieScore(int scoreId, int movieId, int userId, double score, Date publishTime) {
        this.scoreId = scoreId;
        this.movieId = movieId;
        this.userId = userId;
        this.score = score;
        this.publishTime = publishTime;
    }

    public int getScoreId() {
        return scoreId;
    }

    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    @Override
    public String toString() {
        return "MovieScore{" +
                "scoreId=" + scoreId +
                ", movieId=" + movieId +
                ", userId=" + userId +
                ", score=" + score +
                ", publishTime=" + publishTime +
                '}';
    }
}
