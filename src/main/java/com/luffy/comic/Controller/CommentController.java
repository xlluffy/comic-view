package com.luffy.comic.controller;

import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(tags = "CommentController")
@Controller
@RequestMapping("/comment")
public class CommentController {
    private static final Log logger = LogFactory.getLog(CommentController.class);

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation("")
    @GetMapping("/all")
    public String getComments() {
        return "";
    }

    @ApiOperation("")
    @PutMapping("/")
    public CommonResult comment() {
        return CommonResult.success("");
    }

    @ApiOperation("")
    @PutMapping("/reply")
    public CommonResult reply() {
        return CommonResult.success("");
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
