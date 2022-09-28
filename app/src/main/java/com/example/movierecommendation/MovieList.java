package com.example.movierecommendation;


public class MovieList {
    private int mReleaseYear;
    private String mMovieImage, mMovieTitle,  mMetaScore, mMovieGenre;
    private double mIMDBRate;

    private String mCertificate, mOverview, mDirector, mActor1, mActor2, mActor3, mActor4, mRuntime;



    public MovieList(String movieImage, int releaseYear, String movieTitle, double IMDBRate, String metaScore, String MovieGenre,
                     String Certificate, String Overview, String Director, String Actor1, String Actor2, String Actor3, String Actor4, String Runtime) {
        mMovieImage = movieImage;
        mReleaseYear = releaseYear;
        mMovieTitle = movieTitle;
        mIMDBRate = IMDBRate;
        mMetaScore = metaScore;
        mMovieGenre = MovieGenre;

        mCertificate = Certificate;
        mOverview = Overview;
        mDirector = Director;
        mActor1 = Actor1;
        mActor2 = Actor2;
        mActor3 = Actor3;
        mActor4 = Actor4;
        mRuntime = Runtime;
    }

    public MovieList() {
    }


    public String getMovieImage() {
        return mMovieImage;
    }

    public void setMovieImage(String movieImage) {
        mMovieImage = movieImage;
    }

    public int getReleaseYear() {
        return mReleaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        mReleaseYear = releaseYear;
    }

    public String getMovieTitle() {
        return mMovieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        mMovieTitle = movieTitle;
    }

    public double getIMDBRate() {
        return mIMDBRate;
    }

    public void setIMDBRate(double IMDBRate) { mIMDBRate = IMDBRate; }

    public String getMetaScore() {
        return mMetaScore;
    }

    public void setMetaScore(String metaScore) {
        mMetaScore = metaScore;
    }

    public String getMovieGenre() { return mMovieGenre; }

    public void setMovieGenre(String movieGenre) { mMovieGenre = movieGenre; }

    public String getCertificate() {
        return mCertificate;
    }

    public void setCertificate(String certificate) {
        mCertificate = certificate;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getDirector() {
        return mDirector;
    }

    public void setDirector(String director) {
        mDirector = director;
    }

    public String getActor1() {
        return mActor1;
    }

    public void setActor1(String actor1) {
        mActor1 = actor1;
    }

    public String getActor2() {
        return mActor2;
    }

    public void setActor2(String actor2) {
        mActor2 = actor2;
    }

    public String getActor3() {
        return mActor3;
    }

    public void setActor3(String actor3) {
        mActor3 = actor3;
    }

    public String getActor4() {
        return mActor4;
    }

    public void setActor4(String actor4) {
        mActor4 = actor4;
    }

    public String getRuntime() {
        return mRuntime;
    }

    public void setRuntime(String runtime) {
        mRuntime = runtime;
    }
}



