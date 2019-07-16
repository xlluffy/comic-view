package com.luffy.comic.model;

import java.io.Serializable;


public class Comic implements Serializable {

    private int id;
    private String title;
    private String fullTitle;
    private String author;

    public Comic() {
    }

    public Comic(int id, String title, String fullTitle, String author) {
        this.id = id;
        this.title = title;
        this.fullTitle = fullTitle;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Comic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", full_title='" + fullTitle + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
