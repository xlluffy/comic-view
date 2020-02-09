package com.luffy.comic.nosql.elasticsearch.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Document(indexName = "comic", type = "comic", shards = 1,replicas = 0)
public class EsComic implements Serializable {
    private static final long serialVersionUID = -1L;

    @Id
    private Long id;

    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String title;

    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String fullTile;

    @Field(type = FieldType.Keyword)
    private String author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullTile() {
        return fullTile;
    }

    public void setFullTile(String fullTile) {
        this.fullTile = fullTile;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
