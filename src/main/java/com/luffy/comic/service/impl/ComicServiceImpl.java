package com.luffy.comic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luffy.comic.mapper.ComicMapper;
import com.luffy.comic.model.Comic;
import com.luffy.comic.service.ComicService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("comicService")
@Transactional
public class ComicServiceImpl implements ComicService {
    private static final Log logger = LogFactory.getLog(ComicServiceImpl.class);

    @Autowired
    private ComicMapper comicMapper;

    @Override
    public Comic findById(Integer id) {
        return comicMapper.findById(id);
    }

    @Override
    public Comic findByTitle(String title) {
        return comicMapper.findByTitle(title);
    }

    @Override
    public List<Comic> findAll() {
        return comicMapper.findAll();
    }

    @Override
    public PageInfo<Comic> findByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comic> comics = comicMapper.findAll();
        return new PageInfo<>(comics);
    }

    @Override
    public int count() {
        return comicMapper.count();
    }

    @Override
    public void insert(Comic comic) {
        comicMapper.insert(comic);
    }

    @Override
    public void insertOrUpdate(Comic comic) {
        comicMapper.insertOrUpdate(comic);
        if (comic.getId() < 1) {
            comic.setId(comicMapper.findByTitle(comic.getTitle()).getId());
        }
    }

    @Override
    public void insertOrUpdateBatch(List<Comic> comics) {
        comicMapper.insertOrUpdateBatch(comics);
    }

    @Override
    public void deleteById(Integer id) {
        comicMapper.deleteById(id);
    }

    @Override
    public void deleteByTitle(String title) {
        comicMapper.deleteByTitle(title);
    }

    @Override
    public void update(Comic comic) {
        comicMapper.update(comic);
    }

    @Override
    public void updateByTitle(Comic comic) {
        comicMapper.updateByTitle(comic);
    }
}
