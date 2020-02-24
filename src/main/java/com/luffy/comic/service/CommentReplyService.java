package com.luffy.comic.service;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.model.CommentReply;

public interface CommentReplyService {
    PageInfo<CommentReply> findByCommentIdByPage(Integer commentId, Integer pageNum, Integer pageSize);

    void insert(CommentReply commentReply);

    void update(CommentReply commentReply);

    void deleteById(Integer id);
}
