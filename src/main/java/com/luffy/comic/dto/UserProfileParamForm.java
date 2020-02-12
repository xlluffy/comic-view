package com.luffy.comic.dto;

import com.luffy.comic.common.utils.validation.constrations.CodePointSize;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserProfileParamForm {
    private static final long serialVersionUID = 2120869894112984048L;

    private int id;

    @CodePointSize(min = 4, max = 16)
    @Pattern(regexp = "^(?![0-9_])[a-zA-Z0-9_\\u4e00-\\u9fa5]+$")
    private String username;

    @Size(min = 6, max = 16)
    @Pattern(regexp = "^[\\x21-\\x7e]{6,16}$")
    private String password;

    @Email(regexp = "^\\w\\u4e00-\\u9fa5]+@\\w+\\.\\w+$")
    private String email;

    @CodePointSize(min = 4, max = 16)
    @Pattern(regexp = "^(?![0-9_])[a-zA-Z0-9_\\u4e00-\\u9fa5]+$")
    private String nickName;

    private String icon;

    @Size(max = 500)
    private String note;

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

    @Override
    public String toString() {
        return "UserProfileParamForm{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", nickName='" + nickName + '\'' +
                ", icon='" + icon + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
