package com.luffy.comic.service;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.model.Comic;

import java.util.List;

public interface ComicService {
    Comic findById(Integer id);

    Comic findByTitle(String title);

    List<Comic> findAll();

    PageInfo<Comic> findByPage(Integer pageNum, Integer pageSize);

    Comic findByChapterId(Integer chapterId);

    int count();

    void insert(Comic comic);

    void insertOrUpdate(Comic comic);

    void insertOrUpdateBatch(List<Comic> comics);

    void deleteById(Integer id);

    void deleteByTitle(String title);

    void update(Comic comic);

    void updateByTitle(Comic comic);
}
