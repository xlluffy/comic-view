package com.luffy.comic.model;

import java.io.Serializable;

public class Chapter implements Serializable {
    private static final long serialVersionUID = 2120869894112984147L;

    private Integer id;
    private Comic comic;
    private String title;
    private int pages;
    private String suffix;


    public Chapter() {
    }

    public Chapter(Integer id, Comic comic, String title, int pages, String suffix) {
        this.id = id;
        this.comic = comic;
        this.title = title;
        this.pages = pages;
        this.suffix = suffix;
    }

    public Chapter(Comic comic, String title, int pages, String suffix) {
        this.comic = comic;
        this.title = title;
        this.pages = pages;
        this.suffix = suffix;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public Comic getComic() {
        return comic;
    }

    public void setComic(Comic comic) {
        this.comic = comic;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "id=" + id +
                ", comic=" + comic +
                ", title='" + title + '\'' +
                ", pages=" + pages +
                ", suffix='" + suffix + '\'' +
                '}';
    }
}
