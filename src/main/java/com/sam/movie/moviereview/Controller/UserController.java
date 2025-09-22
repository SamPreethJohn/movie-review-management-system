package com.sam.movie.moviereview.Controller;

import java.util.List;

import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



import com.sam.movie.moviereview.Entity.ContactMessage;
import com.sam.movie.moviereview.Entity.Movie;
import com.sam.movie.moviereview.Entity.Review;
import com.sam.movie.moviereview.Entity.User;
import com.sam.movie.moviereview.Entity.Watchlist;
import com.sam.movie.moviereview.Service.ContactMessageService;
import com.sam.movie.moviereview.Service.MovieService;
import com.sam.movie.moviereview.Service.ReviewService;
import com.sam.movie.moviereview.Service.UserService;
import com.sam.movie.moviereview.Service.WatchlistService;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
public class UserController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private WatchlistService watchlistService;

    @Autowired
    private ContactMessageService contactMessageService;

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String userHomePage(Model model) {
        List<Movie> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        model.addAttribute("title", "User Home Page");
        return "index";
    }

    @GetMapping("/movies")
    public List<Movie> getAllMovies(@RequestParam(required = false) String search) {
        return (search != null) ? movieService.searchMovies(search) : movieService.getAllMovies();
    }

    @GetMapping("/proform")
    public String showProForm() {
        return "pro_form";
    }


    @PostMapping("/review")
    @ResponseBody
    public Review addReview(@RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @GetMapping("/{userId}/watchlist")
    public String getWatchlistPage(@PathVariable Long userId, Model model) {
        List<Watchlist> watchlist = watchlistService.getWatchlistByUserId(userId);
        model.addAttribute("watchlist", watchlist);
        model.addAttribute("userid", userId);
        return "watchlist";
    }


    @PostMapping("/{userId}/add/{movieId}")
    @ResponseBody
    public ResponseEntity<String> addToWatchlist(@PathVariable Long userId, @PathVariable Long movieId) {

        User user = userService.findByUserId(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Movie movie = movieService.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));
    Watchlist watchlist = new Watchlist();
    watchlist.setUser(user);
    watchlist.setMovie(movie);

    watchlistService.addToWatchlist(watchlist);
    return ResponseEntity.ok("Movie added to watchlist");
}

    @DeleteMapping("/{userId}/remove/{movieId}")
    @ResponseBody
    public ResponseEntity<String> removeFromWatchlist(@PathVariable Long userId, @PathVariable Long movieId) {
        watchlistService.removeMovieFromWatchlist(userId, movieId);
        return ResponseEntity.ok("Movie removed from watchlist");
    }

    @PostMapping("/contact")
    public ContactMessage sendMessage(@RequestBody ContactMessage message) {
        return contactMessageService.saveMessage(message);
    }

}
