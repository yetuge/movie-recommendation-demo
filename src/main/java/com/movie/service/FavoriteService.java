package com.movie.service;

import com.movie.dao.CollectDao;
import com.movie.dao.UserDao;
import com.movie.entity.Collect;
import com.movie.entity.MovieInformation;
import com.movie.entity.OrdinaryUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FavoriteService {

    private final CollectDao collectDao;
    private final UserDao userDao;

    public FavoriteService() {
        this.collectDao = new CollectDao();
        this.userDao = new UserDao();
    }

    public Integer resolveUserId(Integer userId, String userMailbox) {
        if (userId != null) {
            return userId;
        }
        if (userMailbox == null || userMailbox.trim().isEmpty()) {
            return null;
        }
        OrdinaryUser user = userDao.findUserByAccount(userMailbox.trim());
        return user == null ? null : user.getUserId();
    }

    public boolean isCollected(int userId, int movieId) {
        return collectDao.checkUserCollected(userId, movieId);
    }

    public ServiceResult addFavorite(int userId, int movieId) {
        if (collectDao.checkUserCollected(userId, movieId)) {
            return ServiceResult.failure("Already favorited");
        }

        Collect collect = new Collect();
        collect.setUserId(userId);
        collect.setMovieId(movieId);
        collect.setCollectTime(new Date());
        return collectDao.addCollect(collect)
                ? ServiceResult.success("Favorited")
                : ServiceResult.failure("Favorite failed");
    }

    public ServiceResult removeFavorite(int userId, int movieId) {
        if (!collectDao.checkUserCollected(userId, movieId)) {
            return ServiceResult.failure("Favorite does not exist");
        }
        return collectDao.removeCollect(userId, movieId)
                ? ServiceResult.success("Unfavorited")
                : ServiceResult.failure("Unfavorite failed");
    }

    public FavoriteToggleResult toggleFavorite(int userId, int movieId) {
        if (collectDao.checkUserCollected(userId, movieId)) {
            boolean removed = collectDao.removeCollect(userId, movieId);
            return new FavoriteToggleResult(
                    removed,
                    false,
                    removed ? "Unfavorited" : "Unfavorite failed"
            );
        }

        Collect collect = new Collect();
        collect.setUserId(userId);
        collect.setMovieId(movieId);
        collect.setCollectTime(new Date());
        boolean added = collectDao.addCollect(collect);
        return new FavoriteToggleResult(
                added,
                added,
                added ? "Favorited" : "Favorite failed"
        );
    }

    public List<MovieInformation> getFavoritesByUserId(int userId) {
        return collectDao.getUserCollects(userId);
    }

    public List<MovieInformation> getFavoritesByMailbox(String userMailbox) {
        if (userMailbox == null || userMailbox.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return collectDao.getUserCollectsByMailbox(userMailbox.trim());
    }

    public boolean isFavoritedByMailbox(String userMailbox, int movieId) {
        if (userMailbox == null || userMailbox.trim().isEmpty()) {
            return false;
        }
        return collectDao.checkIsFavorited(userMailbox.trim(), movieId);
    }

    public static class FavoriteToggleResult {
        private final boolean success;
        private final boolean favorited;
        private final String message;

        public FavoriteToggleResult(boolean success, boolean favorited, String message) {
            this.success = success;
            this.favorited = favorited;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public boolean isFavorited() {
            return favorited;
        }

        public String getMessage() {
            return message;
        }
    }
}
