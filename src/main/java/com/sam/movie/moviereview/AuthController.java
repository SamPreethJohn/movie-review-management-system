package com.sam.movie.moviereview;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.sam.movie.moviereview.Entity.Movie;
import com.sam.movie.moviereview.Entity.Role;
import com.sam.movie.moviereview.Entity.User;
import com.sam.movie.moviereview.Repository.UserRepository;
import com.sam.movie.moviereview.Service.MovieService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MovieService movieService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        System.out.println("Register Page Loaded");
        model.addAttribute("user", new User());
        return "register"; // HTML form page
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        model.addAttribute("message", "Registration successful! Please login.");
        return "redirect:/login";
    }

    @GetMapping("/login")
public String showLoginPage() {
    return "login"; 
}

@GetMapping("/homedum")
    public String showHomePage(Model model) {
		List<Movie> movies = (List<Movie>) movieService.getAllMovies();
        model.addAttribute("title", "Home Page");
        model.addAttribute("message", "Welcome to the Movie Review Application!");
		model.addAttribute("movies", movies);
        return "indexdum";
}
    
}
