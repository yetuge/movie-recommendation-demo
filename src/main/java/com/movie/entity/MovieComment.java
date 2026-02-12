package com.movie.entity;

import java.util.Date;

/**
 * 电影评论实体类
 */
public class MovieComment {

    private Integer commentId;       // 评论ID
    private Integer movieId;         // 电影ID
    private Integer userId;          // 用户ID
    private String userName;         // 用户名
    private String content;          // 评论内容
    private Date publishTime;       // 发布时间

    public MovieComment() {
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    @Override
    public String toString() {
        return "MovieComment{" +
                "commentId=" + commentId +
                ", movieId=" + movieId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", publishTime=" + publishTime +
                '}';
    }
}
