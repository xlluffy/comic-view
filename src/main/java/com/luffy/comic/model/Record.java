package com.luffy.comic.model;

import java.io.Serializable;
import java.util.Date;

public class Record implements Serializable {
    private static final long serialVersionUID = 2120869894112984147L;

    private int id;
    private User user;
    private Comic comic;
    private Chapter chapter;
    private String page;
    private String suffix;
    private Date lastUpdate;

    public Record() {
    }

    public Record(int id, User user, Comic comic, Chapter chapter, String page, String suffix, Date lastUpdate) {
        this.id = id;
        this.user = user;
        this.comic = comic;
        this.chapter = chapter;
        this.page = page;
        this.suffix = suffix;
        this.lastUpdate = lastUpdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comic getComic() {
        return comic;
    }

    public void setComic(Comic comic) {
        this.comic = comic;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = String.format("%03d", Integer.parseInt(page));
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", user=" + user +
                ", comic=" + comic +
                ", chapter=" + chapter +
                ", page=" + page +
                ", suffix='" + suffix + '\'' +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
