package com.luffy.comic.model;

import java.io.Serializable;

public class UserRoleRelation implements Serializable {
    private static final long serialVersionUID = 2120869894112984126L;

    private int id;
    private int adminId;
    private int roleId;

    public UserRoleRelation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "UserRoleRelation{" +
                "id=" + id +
                ", adminId=" + adminId +
                ", roleId=" + roleId +
                '}';
    }
}
