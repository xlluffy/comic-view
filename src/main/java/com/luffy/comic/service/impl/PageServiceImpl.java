package com.luffy.comic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luffy.comic.mapper.PageMapper;
import com.luffy.comic.model.Page;
import com.luffy.comic.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("pageService")
@Transactional
public class PageServiceImpl implements PageService {
    @Autowired
    private PageMapper pageMapper;


    @Override
    public Page findById(Integer id) {
        return pageMapper.findById(id);
    }

    @Override
    public Page findByTitle(String title) {
        return pageMapper.findByTitle(title);
    }

    @Override
    public List<Page> findAllByChapterId(Integer chapterId) {
        return pageMapper.findAllByChapterId(chapterId);
    }

    @Override
    public List<Page> findAllByChapterTitle(String chapterTitle) {
        return pageMapper.findAllByChapterTitle(chapterTitle);
    }

    @Override
    public PageInfo<Page> findByPageWithChapterId(Integer chapterId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageSize, pageNum);
        List<Page> pages = pageMapper.findAllByChapterId(chapterId);
        return new PageInfo<>(pages);
    }
}
