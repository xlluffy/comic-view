package com.luffy.comic.dto;

import com.luffy.comic.model.User;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private static final long serialVersionUID = 2120869834112984048L;

    private int id;
    private String username;
    private String nickName;
    private String email;
    private String avatar;
    private String note;
    private int status;

    public UserInfo() {

    }

    public UserInfo(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickName = user.getNickName();
        this.avatar = user.getIcon();
        this.note = user.getNote();
        this.status = user.getStatus();
        setEmail(user.getEmail());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.substring(0, 2) + "***" + email.substring(email.indexOf('@'));
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", note='" + note + '\'' +
                ", status=" + status +
                '}';
    }
}
