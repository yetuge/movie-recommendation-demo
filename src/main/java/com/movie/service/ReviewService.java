package com.movie.service;

import com.movie.dao.CommentDao;
import com.movie.dao.ReviewDao;
import com.movie.dao.ScoreDao;
import com.movie.entity.MovieComment;
import com.movie.entity.MovieScore;
import com.movie.entity.Review;

import java.util.Date;
import java.util.List;

public class ReviewService {

    private final CommentDao commentDao;
    private final ScoreDao scoreDao;
    private final ReviewDao reviewDao;

    public ReviewService() {
        this.commentDao = new CommentDao();
        this.scoreDao = new ScoreDao();
        this.reviewDao = new ReviewDao();
    }

    public ServiceResult submitOrUpdateComment(int userId, int movieId, String content) {
        if (movieId <= 0) {
            return ServiceResult.failure("Invalid movieId");
        }
        if (content == null || content.trim().isEmpty()) {
            return ServiceResult.failure("Comment content cannot be empty");
        }

        String normalized = content.trim();
        Date now = new Date();
        MovieComment existing = commentDao.getUserComment(userId, movieId);
        boolean success;

        if (existing != null) {
            existing.setContent(normalized);
            existing.setPublishTime(now);
            success = commentDao.updateComment(existing);
            return success ? ServiceResult.success("Comment updated") : ServiceResult.failure("Comment update failed");
        }

        MovieComment comment = new MovieComment();
        comment.setMovieId(movieId);
        comment.setUserId(userId);
        comment.setContent(normalized);
        comment.setPublishTime(now);
        success = commentDao.addComment(comment);
        return success ? ServiceResult.success("Comment submitted") : ServiceResult.failure("Comment submit failed");
    }

    public ServiceResult submitOrUpdateScore(int userId, int movieId, double scoreValue) {
        if (movieId <= 0) {
            return ServiceResult.failure("Invalid movieId");
        }
        if (scoreValue < 0 || scoreValue > 10) {
            return ServiceResult.failure("Score must be in range 0-10");
        }

        Date now = new Date();
        MovieScore existing = scoreDao.getUserScore(userId, movieId);
        boolean success;

        if (existing != null) {
            existing.setScore(scoreValue);
            existing.setPublishTime(now);
            success = scoreDao.updateScore(existing);
            return success ? ServiceResult.success("Score updated") : ServiceResult.failure("Score update failed");
        }

        MovieScore score = new MovieScore();
        score.setMovieId(movieId);
        score.setUserId(userId);
        score.setScore(scoreValue);
        score.setPublishTime(now);
        success = scoreDao.addScore(score);
        return success ? ServiceResult.success("Score submitted") : ServiceResult.failure("Score submit failed");
    }

    public List<Review> getReviewsByMovieId(int movieId) {
        return reviewDao.findReviewsByMovieId(movieId);
    }

    public ServiceResult submitReviewByMailbox(int movieId, String userMailbox, String content, double score) {
        if (movieId <= 0 || userMailbox == null || userMailbox.trim().isEmpty() || content == null || content.trim().isEmpty()) {
            return ServiceResult.failure("Invalid parameters");
        }
        if (score < 0 || score > 10) {
            return ServiceResult.failure("Score must be in range 0-10");
        }

        boolean ok = reviewDao.addReview(movieId, userMailbox.trim(), content.trim(), score);
        return ok ? ServiceResult.success("Review submitted") : ServiceResult.failure("Review submit failed");
    }
}
