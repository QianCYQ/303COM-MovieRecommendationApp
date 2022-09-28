package com.example.movierecommendation;

public class MovieCommentDB {
    private String Username, Comment;
    private double Rating;

    public MovieCommentDB(String username, String comment, double rating) {
        Username = username;
        Comment = comment;
        Rating = rating;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }
}
