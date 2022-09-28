package com.example.movierecommendation;

import java.util.ArrayList;

public class WatchlistDB {

    private String WatchlistMovie;

    public WatchlistDB(String watchlistMovie) {
        WatchlistMovie = watchlistMovie;
    }

    public String getWatchlistMovie() {
        return WatchlistMovie;
    }

    public void setWatchlistMovie(String watchlistMovie) {
        WatchlistMovie = watchlistMovie;
    }
}
