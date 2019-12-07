package com.luffy.comic.service;

import com.luffy.comic.model.UmsAdmin;
import com.luffy.comic.model.UmsPermission;

import java.util.List;

/**
 * 后台管理员service
 */
public interface UmsAdminService {
    /**
     * 根据用户名获取后台管理员
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 注册功能
     */
    UmsAdmin register(UmsAdmin umsAdmin);

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String login(String username, String password);

    /**
     * 登陆
     */
    void logout();

    void update(UmsAdmin umsAdmin);

    /**
     * 获取用户所有权限（包括角色权限和+-权限）
     */
    List<UmsPermission> getPermissionList(int adminId);
}
