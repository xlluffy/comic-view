package com.luffy.comic.mapper;

import com.luffy.comic.model.Category;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper {
    @Select("select * from category where id = #{id}")
    @Results(id = "categoryMapper", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "comics", column = "id",
            many = @Many(select = "com.luffy.comic.mapper.ComicMapper.findByCategoryId"))
    })
    Category findById(Integer id);

    @Select("select * from category where name = #{name}")
    Category findByName(String name);

    @Select("select * from category")
    List<Category> findAll();

    @Select("select category.* from category, comic_category_relation ct where ct.comic_id = #{comicId} and ct.category_id = category.id")
    List<Category> findByComicId(Integer comicId);

    @Insert("insert into category(name, alias) values (#{name}, #{alias})")
    @Options(useGeneratedKeys = true)
    void insert(Category category);

    void update(Category category);

    @Delete("delete from category where id = #{id}")
    void delete(int id);
}
