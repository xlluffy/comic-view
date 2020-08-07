/*
package com.luffy.comic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luffy.comic.mapper.EsComicMapper;
import com.luffy.comic.nosql.elasticsearch.document.EsComic;
import com.luffy.comic.nosql.elasticsearch.repository.EsComicRepository;
import com.luffy.comic.service.EsComicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//@Service
public class EsComicServiceImpl implements EsComicService {
    private static final Logger logger = LoggerFactory.getLogger(EsComicServiceImpl.class);

    private EsComicMapper esComicMapper;
    private EsComicRepository comicRepository;

    public EsComicServiceImpl(EsComicMapper esComicMapper, EsComicRepository comicRepository) {
        this.esComicMapper = esComicMapper;
        this.comicRepository = comicRepository;
    }

    @Override
    public int importAll() {
        List<EsComic> esComics = esComicMapper.findAll();
        Iterator<EsComic> esComicIterator = comicRepository.saveAll(esComics).iterator();
        int count = 0;
        while (esComicIterator.hasNext()) {
            count++;
            esComicIterator.next();
        }
        return count;
    }

    @Override
    public void delete(Long id) {
        comicRepository.deleteById(id);
    }

    @Override
    public EsComic create(Long id) {
        EsComic esComic = esComicMapper.findById(id);
        if (esComic != null) {
            return comicRepository.save(esComic);
        }
        return null;
    }

    @Override
    public void delete(List<Long> ids) {
        if (!ids.isEmpty()) {
            List<EsComic> esComics = new ArrayList<>();
            for (Long id : ids) {
                EsComic esComic = new EsComic();
                esComic.setId(id);
                esComics.add(esComic);
            }
            comicRepository.deleteAll(esComics);
        }
    }

    @Override
    public PageInfo<EsComic> searchByTitle(String keyword, Integer pageNum, Integer pageSize) {
        */
/*Pageable pageable = PageRequest.of(pageNum, pageSize);
        return comicRepository.findByTitleOrFullTileAndAuthor(keyword, keyword, keyword, pageable);*//*


        PageHelper.startPage(pageNum, pageSize);
        List<EsComic> comics = comicRepository.findByTitleOrFullTile(keyword, keyword);
        return new PageInfo<>(comics);
    }

    @Override
    public PageInfo<EsComic> searchByAuthor(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EsComic> comics = comicRepository.findByAuthor(keyword);
        return new PageInfo<>(comics);
    }

    @Override
    public PageInfo<EsComic> searchByKeywordAndAuthor(String keyword, String author, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EsComic> comics = comicRepository.findByTitleOrFullTileAndAuthor(keyword, keyword, author);
        return new PageInfo<>(comics);
    }
}
*/
