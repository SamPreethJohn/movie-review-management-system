package com.sam.movie.moviereview.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sam.movie.moviereview.Entity.Watchlist;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    List<Watchlist> findByUser_Id(Long userId);

    boolean existsByUser_IdAndMovie_Id(Long userId, Long movieId);

    void deleteByUser_IdAndMovie_Id(Long userId, Long movieId);

    Optional<Watchlist> findByUser_IdAndMovie_Id(Long userId, Long movieId);

}
