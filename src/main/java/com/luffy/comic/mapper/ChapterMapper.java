package com.luffy.comic.mapper;

import com.luffy.comic.model.Chapter;
import org.apache.ibatis.annotations.*;

import java.util.List;

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

    @Select("select * from chapter where comic_id = #{comicId}")
//    @ResultMap("chapterMapper")
    List<Chapter> findByComicId(Integer comicId);

    @Select("select title from chapter order by id")
    List<String> findAllTitles();

    @Select("select id from chapter where id < #{id} order by id desc limit 1")
    Integer findPrevIdById(Integer id);

    @Select("select id from chapter where id > #{id} order by id limit 1")
    Integer findNextIdById(Integer id);

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
