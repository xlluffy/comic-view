package com.luffy.comic.mapper;

import com.luffy.comic.model.CommentReply;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentReplyMapper {

    @Select("select * from comment_reply where comment_id = #{commentId}")
    @Results(value = {
            @Result(property = "user", column = "user_id",
                one = @One(select = "com.luffy.comic.mapper.UserMapper.findByIdSecurity")),
            @Result(property = "comment.id", column = "comment_id"),
            @Result(property = "reply", column = "reply_id",
                one = @One(select = "com.luffy.comic.mapper.CommentReplyMapper.findById"))
    })
    List<CommentReply> findByCommentId(Integer commentId);

    @Select("select * from comment_reply where id = #{id}")
    @Results(value = {
            @Result(property = "user", column = "user_id",
                    one = @One(select = "com.luffy.comic.mapper.UserMapper.findByIdSecurity"))
    })
    CommentReply findById(Integer id);

    @Insert("insert into comment_reply (text, user_id, comment_id, reply_id) values " +
            "(#{text}, #{user.id}, #{comment.id}, #{reply.id})")
    @Options(useGeneratedKeys = true)
    void insert(CommentReply commentReply);

    @Delete("delete from comment_reply where id = #{id} and user_id = #{userId}")
    void deleteById(@Param("id") Integer id, @Param("userId") Integer userId);
}
