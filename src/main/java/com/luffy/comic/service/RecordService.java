package com.luffy.comic.service;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.model.Chapter;
import com.luffy.comic.model.Comic;
import com.luffy.comic.model.Record;

import java.util.List;
import java.util.Map;

public interface RecordService {
    List<Record> findAll();

    Map<Integer, Record> findAllByComics(Integer userId, List<Comic> comics);

    Map<Integer, String> findAllByChapters(Integer userId, List<Chapter> chapters);

    Record findByChapterId(Integer userId, Integer chapterId);

    PageInfo<Record> findByComicIdByPage(Integer userId, Integer comicId, Integer pageNum, Integer pageSize);

    List<Record> findByChapters(Integer userId, List<Chapter> chapters);

    Record findLastOne(Integer userId);

    PageInfo<Record> findAllLastOneOfComicsByPage(Integer userId, Integer pageNum, Integer pageSize);

    Record findLastOneByComicId(Integer userId, Integer comicId);

    List<Record> findLastOneByComics(Integer userId, List<Comic> comics);

    boolean updateByChapterId(Record record);

    void insert(Record record);

    void deleteById(Integer id);
}
