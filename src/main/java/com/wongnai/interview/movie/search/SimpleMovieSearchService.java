package com.wongnai.interview.movie.search;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieSearchService;
import com.wongnai.interview.movie.external.MovieDataService;
import com.wongnai.interview.movie.external.MoviesResponse;
import com.wongnai.interview.util.MovieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component("simpleMovieSearchService")
public class SimpleMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieDataService movieDataService;

	@Override
	public List<Movie> search(String queryText) {
		//TODO: Step 2 => Implement this method by using data from MovieDataService
		// All test in SimpleMovieSearchServiceIntegrationTest must pass.
		// Please do not change @Component annotation on this class

		MoviesResponse moviesResponse = movieDataService.fetchAll();

		return moviesResponse
				.stream()
				.filter(movieData -> containQueryText(movieData.getTitle(), queryText))
				.map(MovieUtils::mapMovieDataToMovie)
				.collect(Collectors.toList());

	}

	private boolean containQueryText(String title, String queryText) {
		String[] splitTitle = title.split("\\s+");
		long count = Arrays.stream(splitTitle)
				.filter(item -> item.equalsIgnoreCase(queryText))
				.count();
		return count > 0;
	}

}
