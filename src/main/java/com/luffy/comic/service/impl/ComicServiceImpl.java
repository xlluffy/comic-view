package com.luffy.comic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luffy.comic.mapper.ChapterMapper;
import com.luffy.comic.mapper.ComicMapper;
import com.luffy.comic.mapper.UserComicRelationMapper;
import com.luffy.comic.model.Chapter;
import com.luffy.comic.model.Comic;
import com.luffy.comic.service.ComicService;
import com.luffy.comic.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static com.luffy.comic.tools.Tools.transToPath;

@Service("comicService")
@Transactional
public class ComicServiceImpl implements ComicService {
//    private static final Log logger = LogFactory.getLog(ComicServiceImpl.class);
    @Value("${comic-root}")
    private String root;

    private ComicMapper comicMapper;
    private ChapterMapper chapterMapper;
    private UserComicRelationMapper userComicRelationMapper;

    @Override
    public Comic findById(Integer id) {
        return comicMapper.findById(id);
    }

    @Override
    public Comic findByIdWithCategories(Integer id) {
        return comicMapper.findByIdWithCategories(id);
    }

    @Override
    public Comic findByTitle(String title) {
        return comicMapper.findByTitle(title);
    }

    @Override
    public List<Comic> findAll() {
        return comicMapper.findAll("id", "asc");
    }

    @Override
    public Comic findNextComic(Integer id) {
        return comicMapper.findNextComic(id);
    }

    @Override
    public PageInfo<Comic> findByPage(String orderBy, boolean asc, Integer pageNum, Integer pageSize) {
        String style = asc ? "asc" : "desc";
        PageHelper.startPage(pageNum, pageSize);
        switch (orderBy) {
            case "title": return new PageInfo<>(comicMapper.findAll("title", style));
            case "lastUpdate": return new PageInfo<>(comicMapper.findAll("last_update", style));
            default:
                return new PageInfo<>(comicMapper.findAll("id", style));
        }
    }

    @Override
    public PageInfo<Comic> findByUserByPage(Integer userId, String orderBy, boolean asc, Integer pageNum, Integer pageSize) {
        String style = asc ? "asc" : "desc";
        PageHelper.startPage(pageNum, pageSize);
        if ("title".equals(orderBy)) {
            return new PageInfo<>(comicMapper.findAllByUserOrderByTitle(userId, style));
        } else {
            return new PageInfo<>(comicMapper.findAllByUserOrderByCreateTime(userId, style));
        }
    }

    @Override
    public PageInfo<Comic> findByAuthorByPage(String author, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(comicMapper.findByAuthor(author));
    }

    @Override
    public PageInfo<Comic> findByCategoryIdByPage(Integer categoryId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(comicMapper.findByCategoryId(categoryId));
    }

    @Override
    public Map<String, Integer> findAddableLocalComics() {
        String[] titles = new File(root).list();
        if (titles != null) {
            Set<String> remoteTitles = this.findAll().stream().map(Comic::getTitle).collect(Collectors.toSet());
            Set<String> localTitles = Arrays.stream(titles).
                    filter(o -> new File(transToPath(root, o)).isDirectory()).collect(Collectors.toSet());
            localTitles.removeAll(remoteTitles);
            HashMap<String, Integer> comics = new HashMap<>();
            for (String title : localTitles) {
                // 减去Cover.jpg
                comics.put(title, Objects.requireNonNull(new File(transToPath(root, title)).list()).length - 1);
            }
            return comics;
        }
        return null;
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
    public int countByAuthor(String author) {
        return comicMapper.countByAuthor(author);
    }

    @Override
    public void insert(Comic comic) {
        comicMapper.insert(comic);
    }

    @Override
    public void addToFavourite(Integer userId, Integer comicId) {
        userComicRelationMapper.insert(userId, comicId);
    }

    @Override
    public void deleteFavourite(Integer userId, Integer comicId) {
        userComicRelationMapper.delete(userId, comicId);
    }

    @Override
    public Boolean hasFavouriteByUser(Integer userId, Integer comicId) {
        return userComicRelationMapper.hasFavouriteByUser(userId, comicId);
    }

    @Override
    public HashMap<Integer, Boolean> findFavouriteFromComics(Integer userId, List<Comic> comics) {
        HashMap<Integer, Boolean> resultMap = new HashMap<>();
        if (comics != null && comics.size() > 0) {
            for (Integer x : userComicRelationMapper.findFavouriteFromComics(userId, comics)) {
                resultMap.put(x, true);
            }
        }
        return resultMap;
    }

    @Override
    public Comic insertByLocalTitle(String title) {
        Comic comic = new Comic(0, title, title, null);
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
            chapterMapper.insertOrUpdateBatch(chapterList);
        }
        return comic;
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
//        List<Chapter> chapters = chapterMapper.findByComicId(id);
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

    @Autowired
    public void setComicMapper(ComicMapper comicMapper) {
        this.comicMapper = comicMapper;
    }

    @Autowired
    public void setChapterMapper(ChapterMapper chapterMapper) {
        this.chapterMapper = chapterMapper;
    }

    @Autowired
    public void setUserComicRelationMapper(UserComicRelationMapper userComicRelationMapper) {
        this.userComicRelationMapper = userComicRelationMapper;
    }
}
