package com.luffy.comic.model;

import java.io.Serializable;
import java.util.Date;

public class UmsAdminLoginLog implements Serializable {
    private static final long serialVersionUID = 2120869894112984149L;

    private long id;
    private int adminId;
    private String ip;
    private String address;
    private String userAgent;
    private Date createTime;

    public UmsAdminLoginLog() {
    }

    public UmsAdminLoginLog(long id, int adminId, String ip, String address, String userAgent, Date createTime) {
        this.id = id;
        this.adminId = adminId;
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

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
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
        return "UmsAdminLoginLog{" +
                "id=" + id +
                ", adminId=" + adminId +
                ", ip='" + ip + '\'' +
                ", address='" + address + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
