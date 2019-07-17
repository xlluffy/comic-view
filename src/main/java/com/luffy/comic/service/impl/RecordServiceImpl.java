package com.luffy.comic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luffy.comic.mapper.RecordMapper;
import com.luffy.comic.model.Chapter;
import com.luffy.comic.model.Comic;
import com.luffy.comic.model.Record;
import com.luffy.comic.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("recordService")
@Transactional
public class RecordServiceImpl implements RecordService {
    @Autowired
    private RecordMapper recordMapper;

    @Override
    public List<Record> findAll() {
        return recordMapper.findAll();
    }

    @Override
    public Record findByChapterId(Integer chapterId) {
        return recordMapper.findByChapterId(chapterId);
    }

    @Override
    public PageInfo<Record> findByComicIdByPage(Integer comicId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageSize, pageNum);
        List<Record> records = recordMapper.findByComicId(comicId);
        return new PageInfo<>(records);
    }

    @Override
    public List<Record> findByChapters(List<Chapter> chapters) {
        return recordMapper.findByChapters(chapters);
    }

    @Override
    public Record findLastOne() {
        return recordMapper.findLastOne();
    }

    @Override
    public Record findLastOneByComicId(Integer comicId) {
        return recordMapper.findLastOneByComicId(comicId);
    }

    @Override
    public List<Record> findLastOneByComics(List<Comic> comics) {
        List<Record> records = new ArrayList<>();
        comics.forEach(e -> records.add(recordMapper.findLastOneByComicId(e.getId())));
        return records;
    }

    @Override
    public boolean updateByChapterId(Record record) {
        return recordMapper.updateByChapterId(record);
    }

    @Override
    public void insert(Record record) {
        recordMapper.insert(record);
    }
}
