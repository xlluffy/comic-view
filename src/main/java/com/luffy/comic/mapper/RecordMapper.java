package com.luffy.comic.mapper;

import com.luffy.comic.model.Chapter;
import com.luffy.comic.model.Record;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordMapper {

    @Select("select * from record where user_id = #{userId} and chapter_id = #{chapterId}")
    @Results(id = "recordMapper", value = {
            @Result(property = "chapter", column = "chapter_id",
                    one = @One(select = "com.luffy.comic.mapper.ChapterMapper.findById2")),
            @Result(property = "comic", column = "comic_id",
                    one = @One(select = "com.luffy.comic.mapper.ComicMapper.findById")),
            @Result(property = "user.id", column = "user_id")
    })
    Record findByChapterId(@Param("userId") Integer userId, @Param("chapterId") Integer chapterId);

    @Select("select * from record where user_id = #{userId} and last_update = (select max(last_update) from record where user_id = #{userId}) limit 1")
    @ResultMap("recordMapper")
    Record findLastOne(Integer userId);

    /*@Select("select * from record as a where a.user_id = #{userId} and a.last_update = " +
            "(select max(r.last_update) from record as r, chapter as c " +
            "where c.comic_id = #{comicId} and r.user_id = #{userId} and r.chapter_id = c.id) group by a.last_update limit 1")*/
    @Select("select * from record where user_id = #{userId} and comic_id = #{comicId} and last_update =" +
            "        (select max(last_update) from record where user_id = #{userId} and comic_id = #{comicId}) limit 1")
    @Results(id = "recordMapper2", value = {
            @Result(property = "chapter", column = "chapter_id",
                    one = @One(select = "com.luffy.comic.mapper.ChapterMapper.findById2")),
            @Result(property = "user.id", column = "user_id")
    })
    Record findLastOneByComicId(@Param("userId") Integer userId, @Param("comicId") Integer comicId);

    @Select("select * from record where user_id = #{userId} and last_update in " +
            "(select max(last_update) from record where user_id = #{userId} group by comic_id)" +
            "order by last_update desc")
    @ResultMap("recordMapper")
    List<Record> findAllLastOneOfComics(Integer userId);

    @Select("select * from record where user_id = #{userId} order by last_update desc")
    @ResultMap("recordMapper")
    List<Record> findAllLastOne(Integer userId);

    @Select("select * from record, where user_id = #{userId} and comic_id = #{comicId}")
    @ResultMap("recordMapper")
    List<Record> findByComicId(@Param("userId") Integer userId, @Param("comicId") Integer comicId);

    List<Record> findByChapters(@Param("userId") Integer userId, @Param("chapters") List<Chapter> chapters);

    @Select("select * from record")
    @ResultMap("recordMapper")
    List<Record> findAll();

    @Select("select count(1) from record")
    int count();

    @Select("select count(1) from record where title = #{title}")
    int countByTitle(String title);

    boolean updateByChapterId(Record record);

    @Insert("insert into record(user_id, comic_id, chapter_id, page, suffix) values" +
            "(#{user.id}, #{comic.id}, #{chapter.id}, #{page}, #{suffix})")
    void insert(Record record);

    @Delete("delete from record where id = #{id}")
    void deleteById(Integer id);
}
