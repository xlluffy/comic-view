package com.luffy.comic.mapper;

import com.luffy.comic.model.UmsPermission;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UmsAdminRoleRelationMapper {

    @Select("select p.* from ums_admin_role_relation as ar, ums_permission as p where " +
            "ar.admin_id = #{adminId} and ar.role_id = p.id")
    List<UmsPermission> findPermissionByAdminId(int adminId);
}
