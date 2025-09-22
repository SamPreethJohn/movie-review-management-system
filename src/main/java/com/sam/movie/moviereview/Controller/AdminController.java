package com.sam.movie.moviereview.Controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sam.movie.moviereview.Entity.ContactMessage;
import com.sam.movie.moviereview.Entity.Movie;
import com.sam.movie.moviereview.Entity.Review;
import com.sam.movie.moviereview.Entity.User;
import com.sam.movie.moviereview.Repository.MovieRepository;
import com.sam.movie.moviereview.Service.ContactMessageService;
import com.sam.movie.moviereview.Service.MovieService;
import com.sam.movie.moviereview.Service.ReviewService;
import com.sam.movie.moviereview.Service.UserService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    
    private final MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ContactMessageService contactMessageService;

    AdminController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin_dashboard"; 
    }

    @GetMapping("/add_movie")
    public String showAddMovie(Model model) {
    model.addAttribute("movie", new Movie());
    model.addAttribute("genres", List.of("Action", "Adventure", "Comedy", "Drama", "Fantasy", "Horror", "Mystery", "Romance", "Sci-Fi", "Thriller", "Western", "Animation", "Documentary", "Biography", "Crime", "Family"));
    return "admin_addmovie"; 
    }

    @PostMapping("/add_movie")
	public String addMovie(@Validated Movie movie, @RequestParam("poster") MultipartFile poster, RedirectAttributes redirectAttributes, Principal principal) throws IOException {
        if (principal != null) {
            User admin = userService.getUserByEmail(principal.getName());
            movie.setCreatedBy(admin);
        }
         
		if (!poster.isEmpty() && poster.getContentType().startsWith("image/")) {
			String filepath = movieService.imageUrl(poster);
		    movie.setImageUrl(filepath);
		} else {
			redirectAttributes.addFlashAttribute("error", "Invalid image file. Please upload a valid image.");
			return "redirect:admin_addmovie";
		}

		movieService.saveMovie(movie);
		redirectAttributes.addFlashAttribute("success", "Movie added successfully!");
		return "redirect:/admin/admin_viewmovie"; 
	}

    @GetMapping("/admin_contact")
    public String viewContactMessages(Model model) {
        List<ContactMessage> messages = contactMessageService.getAllMessages();
        model.addAttribute("messages", messages);
        return "admin_contact";
    }

     @GetMapping("/admin_viewmovie")
    public String listMovies(Model model) {
        List<Movie> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        return "admin_viewmovie";
    }

    @GetMapping("/admin_movieDetails")
    public String viewMovieDetails(@RequestParam("movieId") Long movieId, Model model) {
        Movie movie = movieRepository.findByIdWithReviews(movieId).orElseThrow(() -> new IllegalArgumentException("Movie not found"));
        model.addAttribute("movie", movie);
        return "admin_movieDetails";
    }

    @GetMapping("/editMovie/{id}")
    public String editMovie(@PathVariable Long id, Model model) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie not found"));
        model.addAttribute("movie", movie);
        return "admin_editmovie";
    }

    @PostMapping("/admin_editmovie/{id}")
    public String updateMovie(@PathVariable Long id, @ModelAttribute("movie") Movie updatedMovie, @RequestParam(value = "poster", required = false) MultipartFile poster, RedirectAttributes redirectAttributes) {
        try {

            Movie existing = movieService.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie not found"));

            existing.setTitle(updatedMovie.getTitle());
            existing.setDescription(updatedMovie.getDescription());
            existing.setGenre(updatedMovie.getGenre());
            existing.setDirector(updatedMovie.getDirector());
            existing.setReleaseYear(updatedMovie.getReleaseYear());
            existing.setDuration(updatedMovie.getDuration());
            existing.setRating(updatedMovie.getRating());
            existing.setCast(updatedMovie.getCast());

            if (poster != null && !poster.isEmpty() && poster.getContentType().startsWith("image/")) {
                String imageUrl = movieService.imageUrl(poster);
                existing.setImageUrl(imageUrl);
            } 

            movieService.saveMovie(existing);
            
            redirectAttributes.addFlashAttribute("success", "Movie updated successfully!");
            return "redirect:/admin/admin_viewmovie";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update movies: " + e.getMessage());
            return "redirect:/admin_editmovie/" + id;
        }
    }

    @GetMapping("/deleteMovie/{id}")
    public String deleteMovie(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        movieRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("Success", "Movie deleted successfully!");
        return "redirect:/admin/admin_viewmovie";
    }

   

}
