package com.sam.movie.moviereview.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sam.movie.moviereview.Entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByTitleContainingIgnoreCase(String keyword);

    List<Movie> findByDirectorContainingIgnoreCase(String director);

    List<Movie> findByGenreContainingIgnoreCase(String genre);

    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.reviews WHERE m.id = :id")
    Optional<Movie> findByIdWithReviews(@Param("id") Long id);

}
