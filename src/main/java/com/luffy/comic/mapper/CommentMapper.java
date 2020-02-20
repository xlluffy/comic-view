package com.luffy.comic.mapper;

import com.luffy.comic.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {
    @Select("select * from comment where id = #{id}")
    @Results(id = "commentMapper", value = {
            @Result(property = "user", column = "user_id",
            one = @One(select = "com.luffy.comic.mapper.UserMapper.findById")),
            @Result(property = "replyUser", column = "reply_user_id",
            one = @One(select = "com.luffy.comic.mapper.UserMapper.findById")),
            @Result(property = "comic", column = "comic_id",
            one = @One(select = "com.luffy.comic.mapper.ComicMapper.findById"))
    })
    Comment findById(Integer id);

    @Select("select * from comment where id = #{id}")
    Comment findById2(Integer id);

    @Select("select * from comment where comic_id = #{comicId}")
    @Results(id = "comicCommentMapper", value = {
            @Result(property = "user", column = "user_id",
                    one = @One(select = "com.luffy.comic.mapper.UserMapper.findById")),
            @Result(property = "replyUser", column = "reply_user_id",
                    one = @One(select = "com.luffy.comic.mapper.UserMapper.findById"))
    })
    List<Comment> findByComicId(Integer comicId);

    @Select("select * from comment where user_id = #{userId}")
    @Results(id = "userCommentMapper", value = {
            @Result(property = "replyUser", column = "reply_user_id",
                    one = @One(select = "com.luffy.comic.mapper.UserMapper.findById")),
            @Result(property = "comic", column = "comic_id",
                    one = @One(select = "com.luffy.comic.mapper.ComicMapper.findById"))
    })
    List<Comment> findByUserId(Integer userId);

    @Insert("insert into comment(text, user_id, reply_user_id, comic_id) values " +
            "(#{text}, #{user.id}, #{replyUser.id}, #{comic.id})")
    @Options(useGeneratedKeys = true)
    void insert(Comment comment);

    void update(Comment comment);

    @Delete("delete from comment where id = #{id}")
    void deleteById(Integer id);
}
