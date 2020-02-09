package com.luffy.comic.model;

import java.io.Serializable;
import java.util.Date;

public class UserLoginLog implements Serializable {
    private static final long serialVersionUID = 2120869894112984149L;

    private long id;
    private int userId;
    private String ip;
    private String address;
    private String userAgent;
    private Date createTime;

    public UserLoginLog() {
    }

    public UserLoginLog(long id, int userId, String ip, String address, String userAgent, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.ip = ip;
        this.address = address;
        this.userAgent = userAgent;
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UserLoginLog{" +
                "id=" + id +
                ", userId=" + userId +
                ", ip='" + ip + '\'' +
                ", address='" + address + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
