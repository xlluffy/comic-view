package com.luffy.comic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luffy.comic.mapper.ChapterMapper;
import com.luffy.comic.model.Chapter;
import com.luffy.comic.service.ChapterService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("chapterService")
@Transactional
public class ChapterServiceImpl implements ChapterService {

    private static final Log logger = LogFactory.getLog(ComicServiceImpl.class);

    @Autowired
    private ChapterMapper chapterMapper;

    @Override
    public Chapter findById(Integer id) {
        return chapterMapper.findById(id);
    }

    @Override
    public Chapter findByTitle(String title) {
        return chapterMapper.findByTitle(title);
    }

    @Override
    public PageInfo<Chapter> findByComicIdByPage(Integer comicId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Chapter> chapters = chapterMapper.findByComicId(comicId);
        return new PageInfo<>(chapters);
    }

    @Override
    public List<String> findAllTitles() {
        return chapterMapper.findAllTitles();
    }

    @Override
    public Integer findPrevIdById(Integer id) {
        return chapterMapper.findPrevIdById(id);
    }

    @Override
    public Integer findNextIdById(Integer id) {
        return chapterMapper.findNextIdById(id);
    }

    @Override
    public void updatePagesByTitle(String title, int pages) {
        chapterMapper.updatePagesByTitle(title, pages);
    }

    @Override
    public void insert(Chapter chapter) {
        chapterMapper.insert(chapter);
    }

    @Override
    public void insertOrUpdate(Chapter chapter) {
        chapterMapper.insertOrUpdate(chapter);
    }

    @Override
    public void insertOrUpdateBatch(List<Chapter> chapters) {
        chapterMapper.insertOrUpdateBatch(chapters);
    }

    @Override
    public void deleteByComicIdAndTitle(Integer comicId, String title) {
        chapterMapper.deleteByComicIdAndTitle(comicId, title);
    }
}
