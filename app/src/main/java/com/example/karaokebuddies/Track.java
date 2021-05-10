package com.example.karaokebuddies;

public class Track {

    private String title;
    private String artist;
    private String imageURL;
    private String trackURL;

    public Track(String title, String artist, String imageURL, String trackURL) {
        this.title = title;
        this.artist = artist;
        this.imageURL = imageURL;
        this.trackURL = trackURL;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTrackURL() {
        return trackURL;
    }

    public void setTrackURL(String trackURL) {
        this.trackURL = trackURL;
    }
}
