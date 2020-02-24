package com.luffy.comic.service;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.model.Chapter;

import java.util.List;
import java.util.Set;

public interface ChapterService {
    Chapter findById(Integer id);

    Chapter findByTitle(String title);

    List<Chapter> findByComicId(Integer comicId);

    PageInfo<Chapter> findByComicIdByPage(Integer comicId, boolean asc, Integer pageNum, Integer pageSize);

    Set<String> findAddableLocalChapter(Integer comicId);

    Chapter findFirstByComicId(Integer comicId);

    List<String> findAllTitles();

    Integer findPrevIdById(Integer id);

    Integer findNextIdById(Integer id);

    Integer countByComicId(Integer comicId);

    void updatePagesByTitle(String title, int pages);

    void insert(Chapter chapter);

    void insertByLocalTitle(Integer id, String title);

    void insertOrUpdate(Chapter chapter);

    void insertOrUpdateBatch(List<Chapter> chapters);

    void deleteById(Integer id);

    void deleteByComicIdAndTitle(Integer comicId, String title);
}
