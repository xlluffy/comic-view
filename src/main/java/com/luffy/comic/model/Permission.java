package com.luffy.comic.model;

import java.io.Serializable;
import java.util.Date;

public class Permission implements Serializable {
    private static final long serialVersionUID = 2120869894112984126L;

    private int id;
    private int pid;
    private String name;
    private String value;
    private String icon;
    private int type;
    private String uri;
    private int status;
    private int sort;
    private Date createTime;

    public Permission() {
    }

    public Permission(int id, int pid, String name, String value, String icon, int type, String uri,
                      int status, int sort, Date createTime) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.value = value;
        this.icon = icon;
        this.type = type;
        this.uri = uri;
        this.status = status;
        this.sort = sort;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", icon='" + icon + '\'' +
                ", type=" + type +
                ", uri='" + uri + '\'' +
                ", status=" + status +
                ", sort=" + sort +
                ", createTime=" + createTime +
                '}';
    }
}
