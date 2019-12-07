package com.luffy.comic.service;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.model.Comic;

import java.util.List;
import java.util.Map;

public interface ComicService {
    Comic findById(Integer id);

    Comic findByTitle(String title);

    List<Comic> findAll();

    Comic findNextComic(Integer id);

    PageInfo<Comic> findByPage(Integer pageNum, Integer pageSize);

    Map<String, Integer> findAddableLocalComics();

    Comic findByChapterId(Integer chapterId);

    int count();

    void insert(Comic comic);

    void insertByLocalTitle(String title);

    void insertOrUpdate(Comic comic);

    void insertOrUpdateBatch(List<Comic> comics);

    void deleteById(Integer id);

    void deleteByTitle(String title);

    void update(Comic comic);

    void updateLocalAll();

    void updateByTitle(Comic comic);
}
