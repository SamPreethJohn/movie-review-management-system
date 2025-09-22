package com.sam.movie.moviereview.Entity;


import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Entity
@Table(
       name = "watchlist", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "movie_id"})
       )
public class Watchlist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

   
    @jakarta.persistence.Transient
    private Review review;

    public Watchlist() {}

    public Watchlist(User user, Movie movie) {
        this.user = user;
        this.movie = movie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Watchlist)) return false;
        Watchlist that = (Watchlist) o;
        return Objects.equals(user != null ? user.getId() : null, that.user != null ? that.user.getId() : null) &&
                Objects.equals(movie != null ? movie.getId() : null, that.movie != null ? that.movie.getId() : null);
}

@Override
    public int hashCode() {
        return Objects.hash(user != null ? user.getId() : null,
                            movie != null ? movie.getId() : null);
    }
    
}
