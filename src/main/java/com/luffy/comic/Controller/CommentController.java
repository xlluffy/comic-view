package com.luffy.comic.controller;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.common.utils.SecurityUtil;
import com.luffy.comic.model.Comment;
import com.luffy.comic.model.CommentReply;
import com.luffy.comic.model.User;
import com.luffy.comic.service.CommentReplyService;
import com.luffy.comic.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;

@Api(tags = "CommentController")
@RestController
@RequestMapping("/comment")
public class CommentController {
    private static final Log logger = LogFactory.getLog(CommentController.class);

    private CommentService commentService;
    private CommentReplyService commentReplyService;

    public CommentController(CommentService commentService, CommentReplyService commentReplyService) {
        this.commentService = commentService;
        this.commentReplyService = commentReplyService;
    }

    @ApiOperation("获取评论")
    @GetMapping("/{comicId}")
    public CommonResult getComments(@PathVariable int comicId,
                              @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        PageInfo<Comment> comments = commentService.findByComicIdByPage(comicId, pageNum, pageSize);
        HashMap<Integer, Object> repliesMap = new HashMap<>(comments.getSize());
        for (Comment comment : comments.getList()) {
            repliesMap.put(comment.getId(), commentReplyService.findByCommentIdByPage(comment.getId(), 1, 10));
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("comments", comments);
        data.put("repliesMap", repliesMap);
        return CommonResult.success(data);
    }

    @ApiOperation("评论漫画")
    @PutMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CommonResult commentToComic(@RequestBody @Valid Comment comment, BindingResult result) {
        if (result.hasErrors()) {
            return CommonResult.failed("验证错误");
        }
        comment.setUser(userDetails(SecurityUtil.getCurrentUserNotNull()));
        commentService.insert(comment);
        comment.setCreateTime(new Date()); // comment表有默认值，懒得改了...
        return CommonResult.success(comment);
    }

    @ApiOperation("回复评论")
    @PutMapping("/reply")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CommonResult reply(@RequestBody CommentReply commentReply, BindingResult result) {
        if (result.hasErrors()) {
            return CommonResult.failed("验证错误");
        }
        commentReply.setUser(userDetails(SecurityUtil.getCurrentUserNotNull()));
        commentReplyService.insert(commentReply);
        commentReply.setCreateTime(new Date()); // comment表有默认值，懒得改了...
        return CommonResult.success(commentReply);
    }

    @ApiOperation("获取评论下的所有回复")
    @GetMapping("{commentId}/replies")
    public CommonResult getReplies(@PathVariable int commentId,
                                    @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                   @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return CommonResult.success(commentReplyService.findByCommentIdByPage(commentId, pageNum, pageSize));
    }

    @ApiOperation("更新评论")
    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CommonResult update() {
        return CommonResult.success("");
    }

    @ApiOperation("删除评论")
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CommonResult delete() {
        return CommonResult.success("");
    }

    private User userDetails(User user) {
        User result = new User();
        result.setId(user.getId());
        result.setUsername(user.getUsername());
        result.setIcon(user.getIcon());
        return result;
    }
}
