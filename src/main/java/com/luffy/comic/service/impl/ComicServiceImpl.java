package com.luffy.comic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luffy.comic.mapper.ComicMapper;
import com.luffy.comic.model.Chapter;
import com.luffy.comic.model.Comic;
import com.luffy.comic.service.ChapterService;
import com.luffy.comic.service.ComicService;
import com.luffy.comic.tools.Tools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.luffy.comic.tools.Tools.transToPath;

@Service("comicService")
@Transactional
public class ComicServiceImpl implements ComicService {
    private static final Log logger = LogFactory.getLog(ComicServiceImpl.class);
    private static final String root = "D:\\Comic";

    @Autowired
    private ComicMapper comicMapper;
    @Autowired
    private ChapterService chapterService;

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
    public Comic findByChapterId(Integer chapterId) {
        return comicMapper.findByChapterId(chapterId);
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
    public void insertByLocalTitle(String title) {
        Comic comic = new Comic(0, title, title, "");
        this.insertOrUpdate(comic);

        String[] chapters = new File(transToPath(root, title)).list();
        if (chapters != null) {
            List<Chapter> chapterList = new ArrayList<>();
            for (String chapterTitle : chapters) {
                String[] pages = new File(transToPath(root, title, chapterTitle)).list();
                if (pages != null && pages.length > 0) {
                    chapterList.add(new Chapter(comic, chapterTitle, pages.length, Tools.splitSuffix(pages[0])));
                }
            }
            chapterService.insertOrUpdateBatch(chapterList);
        }
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
//        List<Chapter> chapters = chapterService.findByComicId(id);
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
    public void updateLocalAll() {
        String[] comics = new File(root).list();
        if (comics != null) {
            for (String title : comics) {
                this.insertByLocalTitle(title);
            }
        }
    }

    @Override
    public void updateByTitle(Comic comic) {
        comicMapper.updateByTitle(comic);
    }
}
