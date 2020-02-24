package com.luffy.comic.mapper;

import com.luffy.comic.model.Chapter;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterMapper {
    @Select("select * from chapter where id=#{id}")
    @Results(id = "chapterMapper", value = {
            @Result(property = "comic", column = "comic_id",
                    one = @One(select = "com.luffy.comic.mapper.ComicMapper.findById"))
    })
    Chapter findById(Integer id);

    @Select("select * from chapter where id=#{id}")
    Chapter findById2(Integer id);

    @Select("select * from chapter where title=#{title}")
    @ResultMap("chapterMapper")
    Chapter findByTitle(String title);

    @Select("select * from chapter where comic_id = #{comicId} order by id ${style}")
//    @ResultMap("chapterMapper")
    List<Chapter> findByComicId(@Param("comicId") Integer comicId, @Param("style") String style);

    @Select("select * from chapter where comic_id = #{comicId} order by id limit 1")
    Chapter findFirstByComicId(Integer comicId);

    @Select("select title from chapter order by id")
    List<String> findAllTitles();

    @Select("select id from chapter where id < #{id} and comic_id = " +
            "(select comic_id from chapter where id=#{id})order by id desc limit 1")
    Integer findPrevIdById(Integer id);
    @Select("select id from chapter where id > #{id} and comic_id = " +
            "(select comic_id from chapter where id=#{id})order by id limit 1")
    Integer findNextIdById(Integer id);

    @Select("select count(1) from chapter where comic_id = #{comicId}")
    Integer countByComicId(Integer comicId);

    @Update("update chapter set pages=#{pages} where title=#{title}")
    void updatePagesByTitle(@Param("title") String title, @Param("pages") int pages);

    void update(Chapter chapter);

    @Insert("insert into chapter(comic_id, title, pages, suffix) values" +
            "(#{comic.id}, #{title}, #{pages}, #{suffix})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Chapter chapter);

    void insertOrUpdate(Chapter chapter);

    void insertOrUpdateBatch(List<Chapter> chapters);

    @Delete("delete from chapter where id=#{id}")
    void deleteById(Integer id);

    @Delete("delete from chapter where comic_id = #{comicId} and title = #{title}")
    void deleteByComicIdAndTitle(@Param("comicId") Integer comicId, @Param("title") String title);
}
