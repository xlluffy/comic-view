package com.luffy.comic.service;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.model.Page;

import java.util.List;

public interface PageService {

    Page findById(Integer id);

    Page findByTitle(String title);

    List<Page> findAllByChapterId(Integer chapterId);

    List<Page> findAllByChapterTitle(String chapterTitle);

    PageInfo<Page> findByPageWithChapterId(Integer chapterId,
                                           Integer pageSize,
                                           Integer pageNum);
}
