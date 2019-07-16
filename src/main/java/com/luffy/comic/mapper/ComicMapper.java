package com.luffy.comic.mapper;

import com.luffy.comic.model.Comic;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ComicMapper {

    @Select("select * from comic where id = #{id}")
    @Results(id = "comicMapper", value = {
            @Result(property = "fullTitle", column = "full_title")
    })
    Comic findById(Integer id);

    @Select("select * from comic where title = #{title}")
    @ResultMap("comicMapper")
    Comic findByTitle(String title);

    @Select("select * from comic")
    @ResultMap("comicMapper")
    List<Comic> findAll();

    @Select("select count(*) from comic")
    int count();

    @Insert("insert into comic(title, full_title, author) values (#{title}, #{fullTitle}, #{author})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Comic comic);

    @Delete("delete from comic where id = #{id}")
    void deleteById(Integer id);

    @Delete("delete from comic where title = #{title}")
    void deleteByTitle(String title);

    void update(Comic comic);

    void updateByTitle(Comic comic);

    void insertOrUpdate(Comic comic);

    void insertOrUpdateBatch(List<Comic> comics);
}
