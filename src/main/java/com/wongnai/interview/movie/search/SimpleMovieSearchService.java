package com.wongnai.interview.movie.search;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieSearchService;
import com.wongnai.interview.movie.external.MovieDataService;
import com.wongnai.interview.movie.external.MovieDataServiceImpl;
import com.wongnai.interview.movie.external.MoviesResponse;
import com.wongnai.interview.util.MovieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component("simpleMovieSearchService")
public class SimpleMovieSearchService implements MovieSearchService {

	private final Logger log = LoggerFactory.getLogger(SimpleMovieSearchService.class);

	@Autowired
	private MovieDataService movieDataService;

	@Override
	public List<Movie> search(String queryText) {
		//TODO: Step 2 => Implement this method by using data from MovieDataService
		// All test in SimpleMovieSearchServiceIntegrationTest must pass.
		// Please do not change @Component annotation on this class
		log.info("Search service with simple search");
		MoviesResponse moviesResponse = movieDataService.fetchAll();

		return IntStream.range(0, moviesResponse.size())
				.mapToObj(i -> MovieUtils.mapMovieDataToMovieWithIndex((long) (i + 1), moviesResponse.get(i)))
				.filter(movieData -> containQueryText(movieData.getName(), queryText))
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
