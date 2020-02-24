package com.luffy.comic.controller;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.common.utils.SecurityUtil;
import com.luffy.comic.model.*;
import com.luffy.comic.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "ComicController")
@Controller
public class ComicController {
    private static final Log logger = LogFactory.getLog(ComicController.class);

    private ComicService comicService;
    private ChapterService chapterService;
    private RecordService recordService;
    private CommentService commentService;
    private CommentReplyService commentReplyService;

    public ComicController(ComicService comicService, ChapterService chapterService,
                           RecordService recordService, CommentService commentService,
                           CommentReplyService commentReplyService) {
        this.comicService = comicService;
        this.chapterService = chapterService;
        this.recordService = recordService;
        this.commentService = commentService;
        this.commentReplyService = commentReplyService;
    }

    @ApiOperation("获取漫画列表信息")
    @GetMapping({"/", "/index"})
//    @PreAuthorize("hasAuthority('comic:comic:read')")
    public String index(@RequestParam(name = "orderBy", defaultValue = "createTime") String orderBy,
                        @RequestParam(name = "asc", defaultValue = "true") boolean asc,
                        @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                        Model model) {
        PageInfo<Comic> pages = comicService.findByPage(orderBy, asc, pageNum, pageSize);
        model.addAttribute("pages", pages);
        User user = SecurityUtil.getCurrentUser();
        if (user != null) {
            model.addAttribute("favourites", comicService.findFavouriteFromComics(user.getId(), pages.getList()));
        }
        return "index";
    }

    @ApiOperation("根据漫画id获取漫画章节")
    @GetMapping("/comic/{id}")
//    @PreAuthorize("hasAuthority('comic:comic:read')")
    public String getComic(@PathVariable int id,
                           @RequestParam(name = "asc", defaultValue = "true") boolean asc,
                           @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                           @RequestParam(name = "pageSize", defaultValue = "50") int pageSize,
                           @RequestParam(name = "cPageNum", defaultValue = "1") int cPageNum,
                           @RequestParam(name = "cPageSize", defaultValue = "20") int cPageSize,
                           Model model) {
        Comic comic = comicService.findByIdWithCategories(id);
        if (comic == null) {
            return "error/404";
        }
        model.addAttribute("comic", comic);
        PageInfo<Chapter> pages = chapterService.findByComicIdByPage(id, asc, pageNum, pageSize);
        model.addAttribute("pages", pages);

        PageInfo<Comment> comments = commentService.findByComicIdByPage(id, cPageNum, cPageSize);
        HashMap<Integer, Object> repliesMap = new HashMap<>(comments.getSize());
        for (Comment comment : comments.getList()) {
            repliesMap.put(comment.getId(), commentReplyService.findByCommentIdByPage(comment.getId(), 1, 10));
        }
        model.addAttribute("comments", comments);
        model.addAttribute("repliesMap", repliesMap);

        User user = SecurityUtil.getCurrentUser();
        Record lastRecord = null;
        if (user != null) {
            lastRecord = recordService.findLastOneByComicId(user.getId(), comic.getId());
            model.addAttribute("all_records", recordService.findAllByChapters(user.getId(), pages.getList()));
            model.addAttribute("favourite", comicService.hasFavouriteByUser(user.getId(), comic.getId()));
        } else {
            model.addAttribute("all_records", new HashMap<>());
            model.addAttribute("favourite", null);
        }
        model.addAttribute("last_record", lastRecord);
        if (lastRecord == null) {
            model.addAttribute("first_chapter", chapterService.findFirstByComicId(id));
        }
        return "comic";
    }

    @ApiOperation("订阅漫画")
    @PostMapping("/comic/{id}/addFavourite")
    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    public CommonResult addFavourite(@PathVariable int id) {
        comicService.addToFavourite(SecurityUtil.getCurrentUserNotNull().getId(), id);
        return CommonResult.success("已订阅漫画");
    }

    @ApiOperation("订阅漫画")
    @PostMapping("/comic/{id}/deleteFavourite")
    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    public CommonResult deleteFavourite(@PathVariable int id) {
        comicService.deleteFavourite(SecurityUtil.getCurrentUserNotNull().getId(), id);
        return CommonResult.success("漫画订阅已取消");
    }

    @ApiOperation("根据漫画id获取下一个漫画")
    @GetMapping("/comic/nextComic")
//    @PreAuthorize("hasAuthority('comic:comic:read')")
    @ResponseBody
    public CommonResult getNextComic(@RequestParam int id) {
//        Comic nextComic
        HashMap<String, Object> data = new HashMap<>();
        Comic comic = comicService.findNextComic(id);
        data.put("comic", comic);
        data.put("chapterCount", chapterService.countByComicId(id));
        data.put("comicCount", comicService.count());
        return CommonResult.success(data);
    }

    @ApiOperation("测试方法")
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        List<Comic> comics = comicService.findByPage("", true, 1, 20).getList();
        Record lastRecord = recordService.findLastOne(2);
        int count = comicService.count();
        Comic comic = comicService.findByChapterId(lastRecord.getChapter().getId());
        Map<Integer, Record> record = recordService.findAllByComics(2, comics);
        return "";
    }
}
