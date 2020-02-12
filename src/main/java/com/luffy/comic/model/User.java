package com.luffy.comic.model;

import com.luffy.comic.common.utils.validation.constrations.CodePointSize;
import com.luffy.comic.dto.UserProfileParamForm;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private static final long serialVersionUID = 2120869894112984148L;

    private int id;

    @CodePointSize(min = 4, max = 16)
    @Pattern(regexp = "^(?![0-9_])[a-zA-Z0-9_\\u4e00-\\u9fa5]+$")
    @NotNull
    private String username;

    @Size(min = 6, max = 16)
    @Pattern(regexp = "^[\\x21-\\x7e]{6,16}$")
    @NotNull
    private String password;

    @NotNull
    @Email(regexp = "^[\\w\\u4e00-\\u9fa5]+@\\w+\\.\\w+$")
    private String email;

    @CodePointSize(min = 4, max = 16)
    @Pattern(regexp = "^(?![0-9_])[a-zA-Z0-9_\\u4e00-\\u9fa5]+$")
    @NotNull
    private String nickName;

    private String icon;

    @Size(max = 500)
    private String note;
    private Date createTime;
    private Date loginTime;
    private int status;

    public User() {
    }

    public User(int id, String username, String password, String email, String nickName,
                String icon, String note, Date createTime, Date loginTime, int status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickName = nickName;
        this.icon = icon;
        this.note = note;
        this.createTime = createTime;
        this.loginTime = loginTime;
        this.status = status;
    }

    public User(User old) {
        this(old.getId(), old.getUsername(), old.getPassword(), old.getEmail(), old.getNickName(),
                old.getIcon(), old.getNote(), old.getCreateTime(), old.getLoginTime(), old.getStatus());
    }

    public User(UserProfileParamForm old) {
        this(old.getId(), old.getUsername(), old.getPassword(), old.getEmail(), old.getNickName(),
                old.getIcon(), old.getNote(), null, null, 1);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", nickName='" + nickName + '\'' +
                ", icon='" + icon + '\'' +
                ", note='" + note + '\'' +
                ", createTime=" + createTime +
                ", loginTime=" + loginTime +
                ", status=" + status +
                '}';
    }
}
