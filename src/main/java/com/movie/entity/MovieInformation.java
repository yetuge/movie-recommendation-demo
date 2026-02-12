package com.movie.entity;

import java.util.Date;

/**
 * 电影信息实体类
 */
public class MovieInformation {

    private int movieId;            // 电影ID
    private String name;            // 电影名称
    private String content;         // 简介/剧情
    private String genre;           // 类型
    private Date releaseTime;       // 上映时间
    private String director;        // 导演
    private String actors;          // 演员
    private int isShowing;          // 1为显示，0为下架
    private double score;          // 评分
    private int duration;          // 时长(分钟)
    private String country;         // 国家

    public MovieInformation() {
    }

    public MovieInformation(int movieId, String name, String content, String genre,
                           Date releaseTime, String director, String actors, int isShowing) {
        this.movieId = movieId;
        this.name = name;
        this.content = content;
        this.genre = genre;
        this.releaseTime = releaseTime;
        this.director = director;
        this.actors = actors;
        this.isShowing = isShowing;
    }

    // Getter/Setter: movieId
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    // Getter/Setter: name (也提供 getMovieName() 用于兼容)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMovieName() {
        return name;
    }

    // Getter/Setter: content (也提供 getPlot() 用于兼容)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPlot() {
        return content;
    }

    public void setPlot(String plot) {
        this.content = plot;
    }

    // Getter/Setter: genre
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    // Getter/Setter: releaseTime
    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    // Getter/Setter: director
    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    // Getter/Setter: actors (也提供 getMainActors() 用于兼容)
    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getMainActors() {
        return actors;
    }

    public void setMainActors(String mainActors) {
        this.actors = mainActors;
    }

    // Getter/Setter: isShowing
    public int getIsShowing() {
        return isShowing;
    }

    public void setIsShowing(int isShowing) {
        this.isShowing = isShowing;
    }

    // Getter/Setter: score
    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    // Getter/Setter: duration
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    // Getter/Setter: country
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "MovieInformation{" +
                "movieId=" + movieId +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", genre='" + genre + '\'' +
                ", releaseTime=" + releaseTime +
                ", director='" + director + '\'' +
                ", actors='" + actors + '\'' +
                ", isShowing=" + isShowing +
                ", score=" + score +
                ", duration=" + duration +
                ", country='" + country + '\'' +
                '}';
    }
}
