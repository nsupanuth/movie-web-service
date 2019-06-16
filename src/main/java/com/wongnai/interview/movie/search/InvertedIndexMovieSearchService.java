package com.wongnai.interview.movie.search;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.MovieSearchService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("invertedIndexMovieSearchService")
@DependsOn("movieDatabaseInitializer")
public class InvertedIndexMovieSearchService implements MovieSearchService, InitializingBean {

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

        Map<String, HashSet<Long>> moviesInvertTableMap = getInvertedIndexHashSetMap(movies);

        String[] queryTexts = queryText.split("\\s+");

        boolean isOneSearchKeyNotFound = !moviesInvertTableMap.containsKey(queryText.toLowerCase()) && queryTexts.length == 1;

        if (isOneSearchKeyNotFound) {
            return new ArrayList<>();
        }

        HashSet<Long> indexResult = getIntersectionSetResult(queryText, moviesInvertTableMap, queryTexts);

        List<Movie> searchResult = new ArrayList<>();
        indexResult.forEach(i -> {
            Optional<Movie> movieResultOptional = movies.stream().filter(movie -> movie.getId().equals(i)).findFirst();
            movieResultOptional.ifPresent(searchResult::add);
        });

        return searchResult;
    }

    private HashSet<Long> getIntersectionSetResult(String queryText, Map<String, HashSet<Long>> moviesInvertTableMap, String[] queryTexts) {
        HashSet<Long> indexResult = new HashSet<>();
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

    private Map<String, HashSet<Long>> getInvertedIndexHashSetMap(List<Movie> movies) {
        Map<String, HashSet<Long>> moviesInvertTableMap = new HashMap<>();
        movies.forEach(movie -> {
            String[] splitMovieNames = movie.getName().split("\\s+");
            Arrays.stream(splitMovieNames).map(String::toLowerCase).forEach(lowerSplitMovieName -> {
                if (moviesInvertTableMap.containsKey(lowerSplitMovieName)) {
                    HashSet<Long> existingMovieSet = moviesInvertTableMap.get(lowerSplitMovieName);
                    existingMovieSet.add(movie.getId());
                    moviesInvertTableMap.put(lowerSplitMovieName, existingMovieSet);
                } else {
                    HashSet<Long> newHashSet = new HashSet<>();
                    newHashSet.add(movie.getId());
                    moviesInvertTableMap.put(lowerSplitMovieName, newHashSet);
                }
            });
        });
        return moviesInvertTableMap;
    }

}
