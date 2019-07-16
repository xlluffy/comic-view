package com.luffy.comic.model;

import java.io.Serializable;
import java.util.Date;

public class Record implements Serializable {
    private static final long serialVersionUID = 2120869894112984147L;

    private int id;
    private Chapter chapter;
    private String page;
    private String suffix;
    private Date lastUpdate;

    public Record() {
    }

    public Record(int id, Chapter chapter, String page, String suffix, Date lastUpdate) {
        this.id = id;
        this.chapter = chapter;
        this.page = page;
        this.suffix = suffix;
        this.lastUpdate = lastUpdate;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                ", chapter=" + chapter +
                ", page=" + page +
                ", suffix='" + suffix + '\'' +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
