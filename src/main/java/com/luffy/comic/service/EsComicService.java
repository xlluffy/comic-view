package com.luffy.comic.service;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.nosql.elasticsearch.document.EsComic;

import java.util.List;

public interface EsComicService {
    /**
     * 从数据库导入数据到ES中
     */
    int importAll();

    void delete(Long id);

    EsComic create(Long id);

    void delete(List<Long> ids);

    PageInfo<EsComic> searchByTitle(String keyword, Integer pageNum, Integer pageSize);

    PageInfo<EsComic> searchByAuthor(String keyword, Integer pageNum, Integer pageSize);

    PageInfo<EsComic> searchByKeywordAndAuthor(String keyword, String author, Integer pageNum, Integer pageSize);
}
