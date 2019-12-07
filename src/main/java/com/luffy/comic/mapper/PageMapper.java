package com.luffy.comic.mapper;

import com.luffy.comic.model.Page;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageMapper {

    @Select("select * from page where id = #{id}")
    @Results(id = "pageMapper", value = {
            @Result(property = "chapter", column = "chapter_id",
                    one = @One(select = "com.luffy.comic.mapper.ChapterMapper.findById"))
    })
    Page findById(Integer id);


    @Select("select * from page where title = #{title}")
    @ResultMap("pageMapper")
    Page findByTitle(String title);

    @Select("select * from page where chapter_id = #{chapterId}")
    @ResultMap("pageMapper")
    List<Page> findAllByChapterId(Integer chapterId);

    @Select("select * from page where chapter_title = #{chapterTitle}")
    @ResultMap("pageMapper")
    List<Page> findAllByChapterTitle(String chapterTitle);
}
