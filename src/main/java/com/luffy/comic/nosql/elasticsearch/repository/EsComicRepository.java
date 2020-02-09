package com.luffy.comic.nosql.elasticsearch.repository;

import com.luffy.comic.nosql.elasticsearch.document.EsComic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EsComicRepository extends ElasticsearchRepository<EsComic, Long> {
    List<EsComic> findByTitleOrFullTile(String title, String fullTitle);

    List<EsComic> findByAuthor(String author);

    List<EsComic> findByTitleOrFullTileAndAuthor(String title, String fullTitle, String author);
}
