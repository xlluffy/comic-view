package com.luffy.comic.mapper;

import com.luffy.comic.model.Permission;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRelationMapper {

    @Select("select name from role")
    List<String> findAll();

    @Select("select p.* from user_role_relation as ar, role_permission_relation as rp, permission as p " +
            "where ar.user_id = #{userId} and ar.role_id = rp.role_id and rp.permission_id = p.id")
    List<Permission> findPermissionByUserId(int userId);

    @Select("select r.name from user_role_relation as ar, role as r where ar.user_id = #{userId} and ar.role_id = r.id")
    List<String> findRolesByUserId(Integer userId);

    @Delete("delete from user_comic_relation where user_id = #{userId}")
    void deletePermissionByUserId(@Param("userId") int userId, @Param("permission") String permission);

    @Insert("insert into user_role_relation(user_id, role_id) values(#{userId}, (select id from role where name=#{role}))")
    @Options(useGeneratedKeys = true)
    void addRoleToUser(Integer userId, String role);

    /*@Insert("insert into ums_admin_role_relation(admin_id, role_id) select #{adminId}, id from role where value=#{role}")
    @Options(useGeneratedKeys = true)
    void addRoleToAdmin(Integer adminId, String role);*/

    @Delete("delete from user_role_relation where user_id = #{userId} and role_id = " +
            "(select id from role where name = #{role})")
    void deleteRoleOfUser(Integer userId, String role);

    /*@Delete("delete from ums_admin_role_relation where admin_id = #{adminId} and role_id = (select id from role where value = #{role})")
    void deleteRoleOfAdmin(Integer adminId, String role);*/
}
