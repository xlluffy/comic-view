package com.luffy.comic.mapper;

import com.luffy.comic.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    @Select("select * from user where id = #{id}")
    User findById(Integer id);

    @Select("select id, username, nick_name, icon, note, create_time from user where id = #{id}")
    User findByIdSecurity(Integer id);

    @Select("select * from user where username = #{username}")
    User findByUsername(String username);

    @Select("select * from user where username = #{username} and password = #{password}")
    User findByUsernameAndPassword(@Param("username")String username, @Param("password")String password);

    @Select("select * from user where email = #{email}")
    User findByEmail(String email);

    @Select("select * from user where status = #{status}")
    List<User> findByStatus(int status);

    @Select("select * from user")
    List<User> findAll();

    void update(User user);

    @Update("update user set status = #{status} where id = #{id}")
    void updateStatusById(@Param("id") int id, @Param("status")int status);

    @Update("update user set status = #{status} where username = #{username}")
    void updateStatusByUsername(@Param("username") String username, @Param("status") int status);

    @Delete("delete from user where id = #{id}")
    void deleteById(int id);

    @Delete("delete from user where username = #{username}")
    void deleteByUsername(String username);

    @Insert("insert into user (username, password, email, nick_name, icon, note, status)" +
            " values (#{username}, #{password}, #{email}, #{nickName}, #{icon}, #{note}, #{status})")
    @Options(useGeneratedKeys = true)
    void insert(User user);

    @Update("update user set status = #{status} where id = #{userId}")
    void disabledOrEnabledUser(Integer userId, Integer status);

    @Select("select status from user where id = #{userId}")
    int findStatus(Integer userId);
}
