package com.luffy.comic.mapper;

import com.luffy.comic.model.Permission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRelationMapper {

    @Select("select p.* from user_role_relation as ar, role_permission_relation as rp, permission as p " +
            "where ar.admin_id = #{adminId} and ar.role_id = rp.role_id and rp.permission_id = p.id")
    List<Permission> findPermissionByAdminId(int adminId);

    @Insert("insert into user_role_relation(admin_id, role_id) values(#{adminId}, #{roleId})")
    @Options(useGeneratedKeys = true)
    void addRoleToAdmin(Integer adminId, Integer roleId);

    /*@Insert("insert into ums_admin_role_relation(admin_id, role_id) select #{adminId}, id from role where value=#{role}")
    @Options(useGeneratedKeys = true)
    void addRoleToAdmin(Integer adminId, String role);*/

    @Delete("delete from user_role_relation where admin_id = #{adminId} and role_id = #{roleId}")
    void deleteRoleOfAdmin(Integer adminId, Integer roleId);

    /*@Delete("delete from ums_admin_role_relation where admin_id = #{adminId} and role_id = (select id from role where value = #{role})")
    void deleteRoleOfAdmin(Integer adminId, String role);*/
}
