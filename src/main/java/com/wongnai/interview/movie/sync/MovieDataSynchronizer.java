package com.wongnai.interview.movie.sync;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.external.MovieDataService;
import com.wongnai.interview.movie.external.MoviesResponse;
import com.wongnai.interview.util.MovieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieDataSynchronizer {
    @Autowired
    private MovieDataService movieDataService;

    @Autowired
    private MovieRepository movieRepository;

    @Transactional
    public void forceSync() throws IOException {
        //TODO: implement this to sync movie into repository
        if (movieRepository.findAll().size() == 0) {
            MoviesResponse moviesResponse = movieDataService.fetchAll();
            List<Movie> movies = moviesResponse
                    .stream()
                    .map(MovieUtils::mapMovieDataToMovie)
                    .collect(Collectors.toList());

            movieRepository.saveAll(movies);
        }
    }
}
