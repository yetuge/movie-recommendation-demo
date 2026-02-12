package com.movie.entity;

import java.util.Date;

/**
 * 收藏实体类
 */
public class Collect {

    private Integer collectId;       // 收藏ID
    private Integer movieId;         // 电影ID
    private Integer userId;          // 用户ID
    private Date collectTime;        // 收藏时间

    public Collect() {
    }

    public Integer getCollectId() {
        return collectId;
    }

    public void setCollectId(Integer collectId) {
        this.collectId = collectId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    @Override
    public String toString() {
        return "Collect{" +
                "collectId=" + collectId +
                ", movieId=" + movieId +
                ", userId=" + userId +
                ", collectTime=" + collectTime +
                '}';
    }
}
