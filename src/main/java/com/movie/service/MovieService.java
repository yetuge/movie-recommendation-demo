package com.movie.service;

import com.movie.dao.MovieDao;
import com.movie.entity.MovieInformation;

import java.util.Collections;
import java.util.List;

public class MovieService {

    private final MovieDao movieDao;

    public MovieService() {
        this.movieDao = new MovieDao();
    }

    public List<MovieInformation> getShowingMovies() {
        return movieDao.findShowingMovies();
    }

    public List<MovieInformation> searchMovies(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return movieDao.searchMovies(keyword.trim());
    }

    public MovieInformation getMovieById(int movieId) {
        return movieDao.findMovieById(movieId);
    }

    public List<MovieInformation> getMoviesByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return movieDao.getMoviesByGenre(genre.trim());
    }
}
