package com.luffy.comic.mapper;

import com.luffy.comic.model.UserLoginLog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLoginLogMapper {
    @Select("select * from user_login_log where id = #{id}")
    UserLoginLog findById(long id);

    @Select("select * from user_login_log where user_id = #{userId}")
    List<UserLoginLog> findByUserId(int userId);

    @Select("select * from user_login_log where ip = #{ip}")
    List<UserLoginLog> findByIp(String ip);

    @Insert("insert into user_login_log (user_id, ip, address, user_agent) values " +
            "(#{userId}, #{ip}, #{address}, #{userAgent})")
    @Options(useGeneratedKeys = true)
    void insert(UserLoginLog userLoginLog);

    @Delete("delete from user_login_log where id = #{id}")
    void deleteById(long id);

    @Delete("delete from user_login_log where user_id = #{userId}")
    void deleteByUserId(int userId);

    @Delete("delete from user_login_log where ip = #{ip}")
    void deleteByIp(String ip);
}
