package com.luffy.comic.mapper;

import com.luffy.comic.model.UmsAdminLoginLog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UmsAdminLoginLogMapper {
    @Select("select * from ums_admin_login_log where id = #{id}")
    UmsAdminLoginLog findById(long id);

    @Select("select * from ums_admin_login_log where admin_id = #{adminId}")
    List<UmsAdminLoginLog> findByAdminId(int adminId);

    @Select("select * from ums_admin_login_log where ip = #{ip}")
    List<UmsAdminLoginLog> findByIp(String ip);

    @Insert("insert into ums_admin_login_log (admin_id, ip, address, user_agent) values " +
            "(#{adminId}, #{ip}, #{address}, #{userAgent})")
    @Options(useGeneratedKeys = true)
    void insert(UmsAdminLoginLog umsAdminLoginLog);

    @Delete("delete from ums_admin_login_log where id = #{id}")
    void deleteById(long id);

    @Delete("delete from ums_admin_login_log where admin_id = #{adminId}")
    void deleteByAdminId(int adminId);

    @Delete("delete from ums_admin_login_log where ip = #{ip}")
    void deleteByIp(String ip);
}
