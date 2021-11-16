package com.example.readmusic.Model;

public class AudioModel {
    private String name;
    private String artist;
    private String path;
    private String duration;
    private String imgPath;

    public AudioModel(String name, String artist, String path, String duration, String imgPath) {
        this.name = name;
        this.artist = artist;
        this.path = path;
        this.duration = duration;
        this.imgPath = imgPath;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public AudioModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    @Override
    public String toString() {
        return "AudioModel{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", duration='" + duration + '\'' +
                ", imgPath='" + imgPath + '\'' +
                '}';
    }
}
