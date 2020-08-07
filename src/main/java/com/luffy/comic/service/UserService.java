package com.luffy.comic.service;

import com.luffy.comic.dto.UserProfileParamForm;
import com.luffy.comic.model.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台管理员service
 */
public interface UserService {
    User getById(Integer id);

    /**
     * 根据用户名获取用户
     */
    User getUserByUsername(String username);

    /**
     * 根据邮箱获取用户
     */
    User getUserByEmail(String email);

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

    void loginLog(HttpServletRequest request, User user);

    /**
     * 登录功能 - 使用jwt
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String loginAuth(String username, String password, boolean rememberMe);

    /**
     * 登出
     */
    void logout();

    void update(UserProfileParamForm user);

    boolean updateEmail(String oldEmail, String newEmail);

    boolean updatePwd(String oldPwd, String newPwd);
}
