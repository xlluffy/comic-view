package com.luffy.comic.service;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.model.Comic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ComicService {
    Comic findById(Integer id);

    Comic findByIdWithCategories(Integer id);

    Comic findByTitle(String title);

    List<Comic> findAll();

    Comic findNextComic(Integer id);

    PageInfo<Comic> findByPage(String orderBy, boolean asc, Integer pageNum, Integer pageSize);

    PageInfo<Comic> findByUserByPage(Integer userId, String orderBy, boolean asc, Integer pageNum, Integer pageSize);

    PageInfo<Comic> findByAuthorByPage(String author, Integer pageNum, Integer pageSize);

    PageInfo<Comic> findByCategoryIdByPage(Integer categoryId, Integer pageNum, Integer pageSize);

    Map<String, Integer> findAddableLocalComics();

    Comic findByChapterId(Integer chapterId);

    int count();

    int countByAuthor(String author);

    void insert(Comic comic);

    void addToFavourite(Integer userId, Integer comicId);

    void deleteFavourite(Integer userId, Integer comicId);

    Boolean hasFavouriteByUser(Integer userId, Integer comicId);

    HashMap<Integer, Boolean> findFavouriteFromComics(Integer userId, List<Comic> comics);

    void insertByLocalTitle(String title);

    void insertOrUpdate(Comic comic);

    void insertOrUpdateBatch(List<Comic> comics);

    void deleteById(Integer id);

    void deleteByTitle(String title);

    void update(Comic comic);

    void updateLocalAll();

    void updateByTitle(Comic comic);
}
