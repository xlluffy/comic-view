package com.luffy.comic.mapper;

import com.luffy.comic.model.Comic;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserComicRelationMapper {

    @Insert("insert into user_comic_relation(user_id, comic_id) values (#{userId}, #{comicId})")
    @Options(useGeneratedKeys = true)
    void insert(@Param("userId") Integer userId, @Param("comicId") Integer comicId);

    @Delete("delete from user_comic_relation where user_id = #{userId} and comic_id = #{comicId}")
    void delete(@Param("userId") Integer userId, @Param("comicId") Integer comicId);

    @Select("select count(1)=1 from user_comic_relation where user_id = #{userId} and comic_id = #{comicId}")
    boolean hasFavouriteByUser(@Param("userId") Integer userId, @Param("comicId") Integer comicId);

    List<Integer> findFavouriteFromComics(@Param("userId") Integer userId, @Param("comics") List<Comic> comics);
}
