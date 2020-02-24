package com.luffy.comic.controller;

import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.common.utils.SecurityUtil;
import com.luffy.comic.model.Comment;
import com.luffy.comic.model.CommentReply;
import com.luffy.comic.service.CommentReplyService;
import com.luffy.comic.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@Api(tags = "CommentController")
@Controller
@RequestMapping("/comment")
public class CommentController {
    private static final Log logger = LogFactory.getLog(CommentController.class);

    private CommentService commentService;
    private CommentReplyService commentReplyService;

    public CommentController(CommentService commentService, CommentReplyService commentReplyService) {
        this.commentService = commentService;
        this.commentReplyService = commentReplyService;
    }

    @ApiOperation("")
    @GetMapping("/all")
    public String getComments() {
        return "";
    }

    @ApiOperation("评论漫画")
    @PutMapping("")
    @ResponseBody
    public CommonResult commentToComic(@RequestBody @Valid Comment comment, BindingResult result) {
        if (result.hasErrors()) {
            return CommonResult.failed("验证错误");
        }
        comment.setUser(SecurityUtil.getCurrentUserNotNull());
        commentService.insert(comment);
        comment.setCreateTime(new Date()); // comment表有默认值，懒得改了...
        return CommonResult.success(comment);
    }

    @ApiOperation("回复评论")
    @PutMapping("/reply")
    @ResponseBody
    public CommonResult reply(@RequestBody CommentReply commentReply, BindingResult result) {
        if (result.hasErrors()) {
            return CommonResult.failed("验证错误");
        }
        commentReply.setUser(SecurityUtil.getCurrentUserNotNull());
        commentReplyService.insert(commentReply);
        commentReply.setCreateTime(new Date()); // comment表有默认值，懒得改了...
        return CommonResult.success(commentReply);
    }

    @ApiOperation("")
    @GetMapping("{commentId}/replies")
    @ResponseBody
    public CommonResult getReplies(@PathVariable int commentId,
                                    @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                   @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return CommonResult.success(commentReplyService.findByCommentIdByPage(commentId, pageNum, pageSize));
    }

    @ApiOperation("")
    @PostMapping("/update")
    public CommonResult update() {
        return CommonResult.success("");
    }

    @ApiOperation("")
    @DeleteMapping("/delete")
    public CommonResult delete() {
        return CommonResult.success("");
    }
}
