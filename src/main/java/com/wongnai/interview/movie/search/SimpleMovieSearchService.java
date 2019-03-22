package com.wongnai.interview.movie.search;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieSearchService;
import com.wongnai.interview.movie.external.MovieDataService;
import com.wongnai.interview.movie.external.MoviesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
                .filter(m -> {
                    Pattern pattern = Pattern.compile("\\b(" + queryText.toLowerCase() + ")\\b");
                    Matcher matcher = pattern.matcher(m.getTitle().toLowerCase());
                    return matcher.find();
                })
                .map(movieData -> new Movie(movieData.getTitle(), movieData.getCast()))
                .collect(Collectors.toList());
    }
}
