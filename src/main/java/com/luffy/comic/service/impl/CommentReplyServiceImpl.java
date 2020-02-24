package com.luffy.comic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luffy.comic.common.utils.SecurityUtil;
import com.luffy.comic.mapper.CommentReplyMapper;
import com.luffy.comic.model.CommentReply;
import com.luffy.comic.service.CommentReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentReplyServiceImpl implements CommentReplyService {

    private CommentReplyMapper replyMapper;

    @Override
    public PageInfo<CommentReply> findByCommentIdByPage(Integer commentId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(replyMapper.findByCommentId(commentId));
    }

    @Override
    public void insert(CommentReply commentReply) {
        replyMapper.insert(commentReply);
    }

    @Override
    public void update(CommentReply commentReply) {
    }

    @Override
    public void deleteById(Integer id) {
        replyMapper.deleteById(SecurityUtil.getCurrentUserNotNull().getId(), id);
    }

    @Autowired
    public void setReplyMapper(CommentReplyMapper replyMapper) {
        this.replyMapper = replyMapper;
    }
}
