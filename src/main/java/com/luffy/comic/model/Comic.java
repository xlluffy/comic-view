package com.luffy.comic.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class Comic implements Serializable {
    private static final long serialVersionUID = 2120869894113412147L;
    private int id;
    private String title;
    private String fullTitle;
    private String author;
    private Date createTime;
    private Date lastUpdate;
    private List<Category> categories;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Comic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", fullTitle='" + fullTitle + '\'' +
                ", author='" + author + '\'' +
                ", createTime=" + createTime +
                ", lastUpdate=" + lastUpdate +
                ", categories=" + categories +
                '}';
    }
}
