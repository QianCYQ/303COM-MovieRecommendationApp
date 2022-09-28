package com.example.movierecommendation;

public class CheckboxModel {
    private boolean isSelected;
    private String genre;

    public CheckboxModel(String genre, boolean isSelected) {
        this.isSelected = isSelected;
        this.genre = genre;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
