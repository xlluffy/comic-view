package com.luffy.comic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luffy.comic.mapper.RecordMapper;
import com.luffy.comic.model.Chapter;
import com.luffy.comic.model.Comic;
import com.luffy.comic.model.Record;
import com.luffy.comic.service.RecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("recordService")
@Transactional
public class RecordServiceImpl implements RecordService {
    private RecordMapper recordMapper;

    public RecordServiceImpl(RecordMapper recordMapper) {
        this.recordMapper = recordMapper;
    }

    @Override
    public List<Record> findAll() {
        return recordMapper.findAll();
    }

    @Override
    public Map<Integer, Record> findAllByComics(Integer userId, List<Comic> comics) {
        Map<Integer, Record> allRecords = new HashMap<>();
        if (comics != null && comics.size() > 0) {
            List<Record> records = this.findLastOneByComics(userId, comics);
            for (int i = 0; i < comics.size(); ++i) {
                allRecords.put(comics.get(i).getId(), records.get(i));
            }
        }
        return allRecords;
    }

    @Override
    public Map<Integer, String> findAllByChapters(Integer userId, List<Chapter> chapters) {
        Map<Integer, String> allRecords = new HashMap<>();
        if (chapters != null && chapters.size() > 0) {
            for (Record record : recordMapper.findByChapters(userId, chapters)) {
                allRecords.put(record.getChapter().getId(), record.getPage());
            }
        }
        return allRecords;
    }

    @Override
    public Record findByChapterId(Integer userId, Integer chapterId) {
        return recordMapper.findByChapterId(userId, chapterId);
    }

    @Override
    public PageInfo<Record> findByComicIdByPage(Integer userId, Integer comicId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Record> records = recordMapper.findByComicId(userId, comicId);
        return new PageInfo<>(records);
    }

    @Override
    public List<Record> findByChapters(Integer userId, List<Chapter> chapters) {
        return recordMapper.findByChapters(userId, chapters);
    }

    @Override
    public Record findLastOne(Integer userId) {
        return recordMapper.findLastOne(userId);
    }

    @Override
    public PageInfo<Record> findAllLastOneOfComicsByPage(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Record> records = recordMapper.findAllLastOneOfComics(userId);
        return new PageInfo<>(records);
    }

    @Override
    public Record findLastOneByComicId(Integer userId, Integer comicId) {
        return recordMapper.findLastOneByComicId(userId, comicId);
    }

    @Override
    public List<Record> findLastOneByComics(Integer userId, List<Comic> comics) {
        List<Record> records = new ArrayList<>();
        comics.forEach(e -> records.add(recordMapper.findLastOneByComicId(userId, e.getId())));
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

    @Override
    public void deleteById(Integer id) {
        recordMapper.deleteById(id);
    }
}
