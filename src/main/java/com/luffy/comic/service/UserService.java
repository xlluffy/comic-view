package com.luffy.comic.service;

import com.luffy.comic.model.Permission;
import com.luffy.comic.model.User;

import java.util.List;

/**
 * 后台管理员service
 */
public interface UserService {
    /**
     * 根据用户名获取后台管理员
     */
    User getAdminByUsername(String username);

    /**
     * 注册功能
     */
    User register(User user);

    /**
     * 登陆功能 - 使用session
     * @param username 登录名
     * @param password 密码
     * @return 登陆用户
     */
    void login(String username, String password);

    /**
     * 登录功能 - 使用jwt
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String loginAuth(String username, String password);

    /**
     * 登出
     */
    void logout();

    void update(User user);

    /**
     * 获取用户所有权限（包括角色权限和+-权限）
     */
    List<Permission> getPermissionList(int adminId);
}
