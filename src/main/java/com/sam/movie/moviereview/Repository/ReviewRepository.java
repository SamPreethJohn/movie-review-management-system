package com.sam.movie.moviereview.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sam.movie.moviereview.Entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByMovieId(Long movieId);
    
    List<Review> findByUserId(Long userId);

    void deleteById(Long id);


}
