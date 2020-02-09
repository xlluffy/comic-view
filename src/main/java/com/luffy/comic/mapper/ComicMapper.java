package com.luffy.comic.mapper;

import com.luffy.comic.model.Comic;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComicMapper {

    @Select("select * from comic where id = #{id}")
    Comic findById(Integer id);

    @Select("select * from comic where title = #{title}")
    Comic findByTitle(String title);

    @Select("select * from comic where author = #{author}")
    List<Comic> findByAuthor(String author);

    @Select("select count(1) from comic where author = #{author}")
    int countByAuthor(String author);

    @Select("select comic.* from comic, chapter where chapter.id = #{chapterId} and chapter.comic_id = comic.id")
    Comic findByChapterId(Integer chapterId);

    @Select("select * from comic where id > #{comicId} order by id limit 1")
    Comic findNextComic(Integer comicId);

    @Select("select * from comic order by ${orderBy} ${style}")
    List<Comic> findAll(@Param("orderBy") String oderBy, @Param("style") String style);

    @Select("select comic.* from comic, user_comic_relation uc where uc.user_id = #{userId} and comic.id = uc.comic_id order by uc.create_time ${style}")
    List<Comic> findAllByUserOrderByCreateTime(@Param("userId") Integer userId, @Param("style") String style);

    @Select("select comic.* from comic, user_comic_relation uc where uc.user_id = #{userId} and comic.id = uc.comic_id order by comic.title ${style}")
    List<Comic> findAllByUserOrderByTitle(@Param("userId") Integer userId, @Param("style") String style);

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
