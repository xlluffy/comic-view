package com.luffy.comic.mapper;

import com.luffy.comic.nosql.elasticsearch.document.EsComic;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EsComicMapper {
    @Select("select * from comic")
    List<EsComic> findAll();

    @Select("select * from comic where id = #{id}")
    EsComic findById(Long id);
}
