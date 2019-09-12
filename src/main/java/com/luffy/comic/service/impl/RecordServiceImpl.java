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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<Integer, Record> findAllByComics(List<Comic> comics) {
        Map<Integer, Record> allRecords = new HashMap<>();
        if (comics != null && comics.size() > 0) {
            List<Record> records = this.findLastOneByComics(comics);
            for (int i = 0; i < comics.size(); ++i) {
                allRecords.put(comics.get(i).getId(), records.get(i));
            }
        }
        return allRecords;
    }

    @Override
    public Map<Integer, String> findAllByChapters(List<Chapter> chapters) {
        Map<Integer, String> allRecords = new HashMap<>();
        if (chapters != null && chapters.size() > 0) {
            for (Record record : recordMapper.findByChapters(chapters)) {
                allRecords.put(record.getChapter().getId(), record.getPage());
            }
        }
        return allRecords;
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
