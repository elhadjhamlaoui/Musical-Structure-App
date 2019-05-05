package com.example.musicplayer;

public class Artist {
    private String name;
    private int cover, count;

    Artist(String name, int cover) {
        this.name = name;
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
