package com.wongnai.interview.movie.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wongnai.interview.movie.exception.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

@Component
public class MovieDataServiceImpl implements MovieDataService {
	public static final String MOVIE_DATA_URL
			= "https://raw.githubusercontent.com/prust/wikipedia-movie-data/master/movies.json";

	@Autowired
	private RestOperations restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public MoviesResponse fetchAll() {
		//TODO:
		// Step 1 => Implement this method to download data from MOVIE_DATA_URL and fix any error you may found.
		// Please noted that you must only read data remotely and only from given source,
		// do not download and use local file or put the file anywhere else.

		MoviesResponse moviesResponse;
		try {
			ResponseEntity<String> result = restTemplate.getForEntity(MOVIE_DATA_URL, String.class);
			moviesResponse = objectMapper.readValue(result.getBody(), MoviesResponse.class);
		} catch (HttpClientErrorException e) {
			throw new InternalServerErrorException(e.getMessage(), MovieDataServiceImpl.class.getName(), "httpClientErrorException");
		} catch (Exception e) {
			throw new InternalServerErrorException(e.getMessage(), MovieDataServiceImpl.class.getName(), "exception");
		}

		return moviesResponse;
	}
}
