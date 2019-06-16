package com.wongnai.interview.util;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.external.MovieData;

public class MovieUtils {

    public static Movie mapMovieDataToMovie(MovieData movieData) {
        return new Movie(movieData.getTitle(), movieData.getCast());
    }

    public static Movie mapMovieDataToMovieWithIndex(long id, MovieData movieData) {
        return new Movie(id, movieData.getTitle(), movieData.getCast());
    }

}
