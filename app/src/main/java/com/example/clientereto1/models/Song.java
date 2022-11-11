package com.example.clientereto1.models;

import java.io.Serializable;

public class Song implements Serializable {

    private static final long serialVersionUID = -578858462965845200L;

    private int id;

    private String author;

    private String title;

    private String url;

    public Song() {

    }

    public Song(int id, String author, String title, String url) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.url = url;
    }

    public Song(String author, String title, String url) {
        this.author = author;
        this.title = title;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
