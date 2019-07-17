package com.luffy.comic.service;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.model.Chapter;
import com.luffy.comic.model.Comic;
import com.luffy.comic.model.Record;

import java.util.List;

public interface RecordService {
    List<Record> findAll();

    Record findByChapterId(Integer chapterId);

    PageInfo<Record> findByComicIdByPage(Integer comicId, Integer pageSize, Integer pageNum);

    List<Record> findByChapters(List<Chapter> chapters);

    Record findLastOne();

    Record findLastOneByComicId(Integer comicId);

    List<Record> findLastOneByComics(List<Comic> comics);

    boolean updateByChapterId(Record record);

    void insert(Record record);
}
