package com.luffy.comic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luffy.comic.mapper.UserMapper;
import com.luffy.comic.mapper.UserRoleRelationMapper;
import com.luffy.comic.model.Permission;
import com.luffy.comic.model.User;
import com.luffy.comic.service.UserAdminService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserAdminServiceImpl implements UserAdminService {

    private UserMapper userMapper;
    private UserRoleRelationMapper userRoleRelationMapper;

    public UserAdminServiceImpl(UserMapper userMapper, UserRoleRelationMapper userRoleRelationMapper) {
        this.userMapper = userMapper;
        this.userRoleRelationMapper = userRoleRelationMapper;
    }

    @Override
    public PageInfo<User> findUserByPage(Integer pagNum, Integer pagSize) {
        PageHelper.startPage(pagNum, pagSize);
        List<User> users = userMapper.findAll();
        return new PageInfo<>(users);
    }

    /*@Override
    public Map<Integer, List<String>> getPermissionsByUsers(List<User> users) {
        return users.stream().collect(Collectors.toMap(User::getId,
                user -> userRoleRelationMapper.findPermissionByUserId(user.getId())));
    }*/

    @Override
    public Map<Integer, List<String>> getRolesByUsers(List<User> users) {
        return users.stream().collect(Collectors.toMap(User::getId,
                user -> userRoleRelationMapper.findRolesByUserId(user.getId())));
    }

    @Override
    public List<Permission> getPermissionList(int adminId) {
        return userRoleRelationMapper.findPermissionByUserId(adminId);
    }

    @Override
    public List<String> getAllRoles() {
        return userRoleRelationMapper.findAll();
    }

    @Override
    public List<String> getRoles(int userId) {
        return userRoleRelationMapper.findRolesByUserId(userId);
    }

    @Override
    public void addRole(int userId, String role) {
        userRoleRelationMapper.addRoleToUser(userId, role);
    }

    @Override
    public void removeRole(int userId, String role) {
        userRoleRelationMapper.deleteRoleOfUser(userId, role);
    }

    @Override
    public void addPermission(int userId, String permission) {

    }

    @Override
    public void removePermission(int userId, String permission) {

    }

    @Override
    public void disabledUser(Integer userId) {
        userMapper.disabledOrEnabledUser(userId, 0);
    }

    @Override
    public void enabledUser(Integer userId) {
        userMapper.disabledOrEnabledUser(userId, 1);
    }

    @Override
    public void deleteUser(Integer userId) {
        userMapper.deleteById(userId);
    }
}
