package com.wongnai.interview.movie.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wongnai.interview.movie.MoviesController;
import com.wongnai.interview.movie.exception.BadRequestAlertException;
import com.wongnai.interview.movie.exception.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

@Component
public class MovieDataServiceImpl implements MovieDataService {

    private final Logger log = LoggerFactory.getLogger(MovieDataServiceImpl.class);

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
            log.info("Fetching data from movie datasource...");
            ResponseEntity<String> result = restTemplate.getForEntity(MOVIE_DATA_URL, String.class);
            moviesResponse = objectMapper.readValue(result.getBody(), MoviesResponse.class);
        } catch (HttpClientErrorException e) {
            log.debug("Http status code : {}", e.getStatusCode());
            boolean isBadRequest = e.getStatusCode() == HttpStatus.valueOf(400);
            if (isBadRequest) {
                log.error("Http error with bad request : {}", e.getMessage());
                throw new BadRequestAlertException("Bad Request from movie datasource", MovieDataServiceImpl.class.getName(), "datasourceError");
            }
            log.error("Http error : {}", e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), MovieDataServiceImpl.class.getName(), "httpClientErrorException");
        } catch (Exception e) {
            log.error("Fetch data error with message : {}", e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), MovieDataServiceImpl.class.getName(), "exception");
        }

        log.info("Finish fetching data from movie datasource with total element : {}", moviesResponse.size());
        return moviesResponse;
    }
}
