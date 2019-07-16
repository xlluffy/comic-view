package com.luffy.comic.model;

import java.io.Serializable;

public class Page implements Serializable {

    private int id;
    private Character character;
    private String title;

    public Page() {
    }

    public Page(int id, Character character, String title) {
        this.id = id;
        this.character = character;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Page{" +
                "id=" + id +
                ", character=" + character +
                ", title='" + title + '\'' +
                '}';
    }
}
