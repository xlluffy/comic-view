package com.luffy.comic.service;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.model.Chapter;
import com.luffy.comic.model.Comic;
import com.luffy.comic.model.Record;

import java.util.List;
import java.util.Map;

public interface RecordService {
    List<Record> findAll();

    Map<Integer, Record> findAllByComics(List<Comic> comics);

    Map<Integer, String> findAllByChapters(List<Chapter> chapters);

    Record findByChapterId(Integer chapterId);

    PageInfo<Record> findByComicIdByPage(Integer comicId, Integer pageSize, Integer pageNum);

    List<Record> findByChapters(List<Chapter> chapters);

    Record findLastOne();

    Record findLastOneByComicId(Integer comicId);

    List<Record> findLastOneByComics(List<Comic> comics);

    boolean updateByChapterId(Record record);

    void insert(Record record);
}
