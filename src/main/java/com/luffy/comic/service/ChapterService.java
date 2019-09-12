package com.luffy.comic.service;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.model.Chapter;

import java.util.List;

public interface ChapterService {
    Chapter findById(Integer id);

    Chapter findByTitle(String title);

    List<Chapter> findByComicId(Integer comicId);

    PageInfo<Chapter> findByComicIdByPage(Integer comicId, Integer pageNum, Integer pageSize);

    List<String> findAllTitles();

    Integer findPrevIdById(Integer id);

    Integer findNextIdById(Integer id);

    void updatePagesByTitle(String title, int pages);

    void insert(Chapter chapter);

    void insertByLocalTitle(Integer id, String title);

    void insertOrUpdate(Chapter chapter);

    void insertOrUpdateBatch(List<Chapter> chapters);

    void deleteById(Integer id);

    void deleteByComicIdAndTitle(Integer comicId, String title);
}
