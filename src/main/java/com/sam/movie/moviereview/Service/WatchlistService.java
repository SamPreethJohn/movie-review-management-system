package com.sam.movie.moviereview.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sam.movie.moviereview.Entity.Watchlist;
import com.sam.movie.moviereview.Repository.WatchlistRepository;

@Service
public class WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    // Add to watchlist
    public Watchlist addToWatchlist(Watchlist watchlist) {
        Long userId = watchlist.getUser().getId();
        Long movieId = watchlist.getMovie().getId();
        if (!watchlistRepository.existsByUser_IdAndMovie_Id(userId, movieId)) {
                  return watchlistRepository.save(watchlist);
        }

        return watchlist;
    }

    // Remove from watchlist
    public void removeMovieFromWatchlist(Long userId, Long movieId) {
        Watchlist watchlist = watchlistRepository.findByUser_IdAndMovie_Id(userId, movieId)
           .orElseThrow(() -> new RuntimeException("Movie not found in watchlist"));
        watchlistRepository.delete(watchlist);
    }

    // Get watchlist by user ID
    public List<Watchlist> getWatchlistByUserId(Long userId) {
          List<Watchlist> watchlist = watchlistRepository.findByUser_Id(userId);

          // Add user's review for each movie
    for (Watchlist wl : watchlist) {
        wl.setReview(
            wl.getMovie().getReviews().stream()
                .filter(r -> r.getUser().getId().equals(wl.getUser().getId()))
                .findFirst()
                .orElse(null)
        );
    }

    return watchlist;

   }

}
