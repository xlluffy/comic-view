package com.luffy.comic.service;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.model.Comment;

public interface CommentService {

    PageInfo<Comment> findByComicIdByPage(Integer comicId, Integer pageNum, Integer pageSize);

    PageInfo<Comment> findByUserIdByPage(Integer userId, Integer pageNum, Integer pageSize);

    void insert(Comment comment);

    void update(Comment comment);

    void deleteById(Integer id);
}
