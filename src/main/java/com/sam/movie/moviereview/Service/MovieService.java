package com.sam.movie.moviereview.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sam.movie.moviereview.Entity.Movie;
import com.sam.movie.moviereview.Repository.MovieRepository;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, Movie updatedMovie) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
        movie.setTitle(updatedMovie.getTitle());
        movie.setDescription(updatedMovie.getDescription());
        movie.setGenre(updatedMovie.getGenre());
        movie.setDirector(updatedMovie.getDirector());
        movie.setReleaseYear(updatedMovie.getReleaseYear());
        movie.setDuration(updatedMovie.getDuration());
        movie.setRating(updatedMovie.getRating());
        movie.setCast(updatedMovie.getCast());
        movie.setImageUrl(updatedMovie.getImageUrl());

        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public List<Movie> searchMovies(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    public String imageUrl(MultipartFile poster) throws IOException {
        String folderPath = new File("src/main/resources/static/images").getAbsolutePath();
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filename = poster.getOriginalFilename().replaceAll("\\s+", "_");
        File saveFile = new File(directory,  filename);
        poster.transferTo(saveFile);

        return "/images/" + filename;
    }

    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    public List<Movie> searchByDirector(String director) {
        return movieRepository.findByDirectorContainingIgnoreCase(director);
    }

    public List<Movie> searchByGenre(String genre) {
        return movieRepository.findByGenreContainingIgnoreCase(genre);
    }

}
