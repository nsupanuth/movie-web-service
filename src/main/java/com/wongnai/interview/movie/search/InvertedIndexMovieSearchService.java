package com.wongnai.interview.movie.search;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.MovieSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("invertedIndexMovieSearchService")
@DependsOn("movieDatabaseInitializer")
public class InvertedIndexMovieSearchService implements MovieSearchService, InitializingBean {

    private final Logger log = LoggerFactory.getLogger(InvertedIndexMovieSearchService.class);

    @Autowired
    private MovieRepository movieRepository;

    private List<Movie> movies = new ArrayList<>();

    @Override
    public void afterPropertiesSet() {
        if (movies.isEmpty()) {
            movies = movieRepository.findAll();
        }
    }

    @Override
    public List<Movie> search(String queryText) {

        //TODO: Step 4 => Please implement in-memory inverted index to search movie by keyword.
        // You must find a way to build inverted index before you do an actual search.
        // Inverted index would looks like this:
        // -------------------------------
        // |  Term      | Movie Ids      |
        // -------------------------------
        // |  Star      |  5, 8, 1       |
        // |  War       |  5, 2          |
        // |  Trek      |  1, 8          |
        // -------------------------------
        // When you search with keyword "Star", you will know immediately, by looking at Term column, and see that
        // there are 3 movie ids contains this word -- 1,5,8. Then, you can use these ids to find full movie object from repository.
        // Another case is when you find with keyword "Star War", there are 2 terms, Star and War, then you lookup
        // from inverted index for Star and for War so that you get movie ids 1,5,8 for Star and 2,5 for War. The result that
        // you have to return can be union or intersection of those 2 sets of ids.
        // By the way, in this assignment, you must use intersection so that it left for just movie id 5.

        Map<String, TreeSet<Long>> moviesInvertTableMap = getInvertedIndexHashSetMap(movies);

        String[] queryTexts = queryText.split("\\s+");
        log.debug("Number of query text is : {}", queryTexts.length);
        boolean isOneSearchKeyNotFound = !moviesInvertTableMap.containsKey(queryText.toLowerCase()) && queryTexts.length == 1;
        log.debug("isOneSearchKeyNotFound : {}", isOneSearchKeyNotFound);
        if (isOneSearchKeyNotFound) {
            return new ArrayList<>();
        }

        TreeSet<Long> indexResult = getIntersectionSetResult(queryText, moviesInvertTableMap, queryTexts);

        List<Movie> searchResult = new ArrayList<>();
        indexResult.forEach(i -> {
            Optional<Movie> movieResultOptional = movies.stream().filter(movie -> movie.getId().equals(i)).findFirst();
            movieResultOptional.ifPresent(searchResult::add);
        });

        return searchResult;
    }

    private TreeSet<Long> getIntersectionSetResult(String queryText, Map<String, TreeSet<Long>> moviesInvertTableMap, String[] queryTexts) {
        TreeSet<Long> indexResult = new TreeSet<>();
        if (queryTexts.length > 1) {
            for (String text : queryTexts) {
                String textLower = text.toLowerCase();
                if (indexResult.isEmpty() && moviesInvertTableMap.containsKey(textLower)) {
                    indexResult = moviesInvertTableMap.get(textLower);
                } else {
                    indexResult.retainAll(moviesInvertTableMap.containsKey(textLower) ? moviesInvertTableMap.get(textLower) : Collections.EMPTY_SET);
                }
            }
        } else {
            indexResult = moviesInvertTableMap.get(queryText.toLowerCase());
        }
        return indexResult;
    }

    private Map<String, TreeSet<Long>> getInvertedIndexHashSetMap(List<Movie> movies) {
        Map<String, TreeSet<Long>> moviesInvertTableMap = new HashMap<>();
        movies.forEach(movie -> {
            String[] splitMovieNames = movie.getName().split("\\s+");
            Arrays.stream(splitMovieNames).map(String::toLowerCase).forEach(lowerSplitMovieName -> {
                if (moviesInvertTableMap.containsKey(lowerSplitMovieName)) {
                    TreeSet<Long> existingMovieSet = moviesInvertTableMap.get(lowerSplitMovieName);
                    existingMovieSet.add(movie.getId());
                    moviesInvertTableMap.put(lowerSplitMovieName, existingMovieSet);
                } else {
                    TreeSet<Long> newTreeSet = new TreeSet<>();
                    newTreeSet.add(movie.getId());
                    moviesInvertTableMap.put(lowerSplitMovieName, newTreeSet);
                }
            });
        });
        return moviesInvertTableMap;
    }

}
