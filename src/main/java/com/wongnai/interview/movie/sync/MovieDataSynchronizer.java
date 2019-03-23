package com.wongnai.interview.movie.sync;

import javax.transaction.Transactional;

import com.wongnai.interview.movie.MovieConstant;
import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.external.MoviesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.external.MovieDataService;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieDataSynchronizer {
    @Autowired
    private MovieDataService movieDataService;

    @Autowired
    private MovieRepository movieRepository;

    @Transactional
    public void forceSync() {
        //TODO: implement this to sync movie into repository
        MoviesResponse moviesResponse = movieDataService.fetchAll();
        List<Movie> movieList = moviesResponse
                .stream()
                .map(m -> movieRepository.save(new Movie(m.getTitle(), m.getCast())))
                .collect(Collectors.toList());
        initialMapIndex(movieList);
    }

    private void initialMapIndex(List<Movie> movieList) {
        movieList
                .stream()
                .forEach(m -> {
                    List<String> key = Arrays.asList(m.getName().replaceAll("\\p{Punct}", "").split(" "));
                    key.stream().forEach(s -> putIndexToMap(s.toLowerCase(), m.getId()));
                });
    }

    private void putIndexToMap(String key, Long movieId) {
        List<Long> movieIdList = new LinkedList<>();
        if (MovieConstant.MAP_INDEX.containsKey(key)) {
            movieIdList = MovieConstant.MAP_INDEX.get(key);
        }
        movieIdList.add(movieId);
        MovieConstant.MAP_INDEX.put(key, movieIdList);
    }
}
