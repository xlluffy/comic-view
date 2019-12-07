package com.luffy.comic.mapper;

import com.luffy.comic.model.UmsAdmin;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UmsAdminMapper {
    @Select("select * from ums_admin where id = #{id}")
    UmsAdmin findById(Integer id);

    @Select("select * from ums_admin where username = #{username}")
    UmsAdmin findByUsername(String username);

    @Select("select * from ums_admin where username = #{username} and password = #{password}")
    UmsAdmin findByUsernameAndPassword(@Param("username")String username, @Param("password")String password);

    @Select("select * from ums_admin where email = #{email}")
    UmsAdmin findByEmail(String email);

    @Select("select * from ums_admin where status = #{status}")
    List<UmsAdmin> findByStatus(int status);

    @Select("select * from ums_admin")
    List<UmsAdmin> findAll();

    void update(UmsAdmin umsAdmin);

    @Update("update ums_admin set status = #{status} where id = #{id}")
    void updateStatusById(@Param("id") int id, @Param("status")int status);

    @Update("update ums_admin set status = #{status} where username = #{username}")
    void updateStatusByUsername(@Param("username") String username, @Param("status") int status);

    @Delete("delete from ums_admin where id = #{id}")
    void deleteById(int id);

    @Delete("delete from ums_admin where username = #{username}")
    void deleteByUsername(String username);

    @Insert("insert into ums_admin (username, password, email, nick_name, icon, note, status)" +
            " values (#{username}, #{password}, #{email}, #{nickName}, #{icon}, #{note}, #{state})")
    @Options(useGeneratedKeys = true)
    void insert(UmsAdmin umsAdmin);
}
