package com.luffy.comic.mapper;

import com.luffy.comic.model.Chapter;
import com.luffy.comic.model.Record;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RecordMapper {

    @Select("select * from record where chapter_id = #{chapterId}")
    @Results(id = "recordMapper", value = {
            @Result(property = "chapter", column = "chapter_id",
                    one = @One(select = "com.luffy.comic.mapper.ChapterMapper.findById2"))
    })
    Record findByChapterId(Integer chapterId);

    @Select("select *, max(last_update) from record")
    @ResultMap("recordMapper")
    Record findLastOne();

    @Select("select *, max(last_update) from record, chapter where record.chapter_id = chapter.id and " +
            "chapter.comic_id = #{comicId}")
    @ResultMap("recordMapper")
    Record findLastOneByComicId(Integer comicId);

    @Select("select * from record, chapter where record.chapter_id = chapter.id " +
            "and chapter.comic_id = #{comicId}")
    @ResultMap("recordMapper")
    List<Record> findByComicId(Integer comicId);

    List<Record> findByChapters(List<Chapter> chapters);

    @Select("select * from record where id in (select max(id) from record group by title)")
    @ResultMap("recordMapper")
    List<Record> findAllLastOne();

    @Select("select * from record")
    @ResultMap("recordMapper")
    List<Record> findAll();

    @Select("select count(*) from record")
    int count();

    @Select("select count(*) from record where title = #{title}")
    int countByTitle(String title);

    //    @Update("update record set page=#{page} where chapter_id=#{chapter.id}")
    boolean updateByChapterId(Record record);

    @Insert("insert into record(chapter_id, page, suffix) values" +
            "(#{chapter.id}, #{page}, #{suffix})")
    void insert(Record record);
}
