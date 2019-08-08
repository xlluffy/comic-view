package com.luffy.comic.model;

import java.io.Serializable;

public class UmsRolePermissionRelation implements Serializable {
    private static final long serialVersionUID = 2120869894112984127L;

    private int id;
    private int roleId;
    private int permissionId;

    public UmsRolePermissionRelation() {
    }

    public UmsRolePermissionRelation(int id, int roleId, int permissionId) {
        this.id = id;
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public String toString() {
        return "UmsRolePermissionRelation{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", permissionId=" + permissionId +
                '}';
    }
}
