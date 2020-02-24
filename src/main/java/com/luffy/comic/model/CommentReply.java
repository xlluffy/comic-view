package com.luffy.comic.model;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

public class CommentReply implements Serializable {
    private static final long serialVersionUID = 2120849894113612147L;
    @Size(min = 1, max = 500)
    String text;
    User user;
    Comment comment;
    // 目前似乎用replyUser更好，先保留...
    CommentReply reply;
    Date createTime;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public CommentReply getReply() {
        return reply;
    }

    public void setReply(CommentReply reply) {
        this.reply = reply;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "CommentReply{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", user=" + user +
                ", comment=" + comment +
                ", reply=" + reply +
                ", createTime=" + createTime +
                '}';
    }
}
