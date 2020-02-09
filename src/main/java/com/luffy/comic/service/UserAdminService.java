package com.luffy.comic.service;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.model.Permission;
import com.luffy.comic.model.User;

import java.util.List;
import java.util.Map;

public interface UserAdminService {
    PageInfo<User> findUserByPage(Integer pagNum, Integer pagSize);

//    Map<Integer, List<String>> getPermissionsByUsers(List<User> users);

    Map<Integer, List<String>> getRolesByUsers(List<User> users);

    /**
     * 获取用户所有权限（包括角色权限和+-权限）
     */
    List<Permission> getPermissionList(int userId);

    List<String> getAllRoles();

    List<String> getRoles(int userId);

    void addRole(int userId, String role);

    void removeRole(int userId, String role);

    void addPermission(int userId, String permission);

    void removePermission(int userId, String permission);

    void disabledUser(Integer userId);

    void enabledUser(Integer userId);

    void deleteUser(Integer userId);
}
