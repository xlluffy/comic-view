package com.luffy.comic.model;

import java.io.Serializable;
import java.util.Date;

public class Role implements Serializable {
    private static final long serialVersionUID = 2120869894112984125L;

    private int id;
    private String name;
    private String description;
    private int adminCount;
    private Date createTime;
    private int status;
    private int sort;

    public Role() {
    }

    public Role(int id, String name, String description, int adminCount,
                Date createTime, int status, int sort) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.adminCount = adminCount;
        this.createTime = createTime;
        this.status = status;
        this.sort = sort;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAdminCount() {
        return adminCount;
    }

    public void setAdminCount(int adminCount) {
        this.adminCount = adminCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", adminCount=" + adminCount +
                ", createTime=" + createTime +
                ", status=" + status +
                ", sort=" + sort +
                '}';
    }
}
