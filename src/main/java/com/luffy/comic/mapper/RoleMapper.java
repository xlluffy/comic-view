package com.luffy.comic.mapper;

import com.luffy.comic.model.Role;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {

    @Select("select r.* from user_role_relation as ar, role as r where ar.admin_id = #{adminId} and ar.role_id = r.id")
    List<Role> findRolesByAdminId(Integer adminId);
}
