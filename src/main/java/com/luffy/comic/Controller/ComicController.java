package com.luffy.comic.controller;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.common.utils.SecurityUtil;
import com.luffy.comic.model.Chapter;
import com.luffy.comic.model.Comic;
import com.luffy.comic.model.Record;
import com.luffy.comic.model.User;
import com.luffy.comic.service.ChapterService;
import com.luffy.comic.service.ComicService;
import com.luffy.comic.service.RecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api(tags = "ComicController")
@RestController
public class ComicController {
    private static final Log logger = LogFactory.getLog(ComicController.class);

    private ComicService comicService;
    private ChapterService chapterService;
    private RecordService recordService;

    public ComicController(ComicService comicService, ChapterService chapterService,
                           RecordService recordService) {
        this.comicService = comicService;
        this.chapterService = chapterService;
        this.recordService = recordService;
    }

    @ApiOperation("获取漫画列表信息")
    @GetMapping({"/", "/index"})
    public CommonResult index(@RequestParam(name = "orderBy", defaultValue = "createTime") String orderBy,
                        @RequestParam(name = "asc", defaultValue = "true") boolean asc,
                        @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        HashMap<String, Object> data = new HashMap<>();
        PageInfo<Comic> pages = comicService.findByPage(orderBy, asc, pageNum, pageSize);
        data.put("pages", pages);
        User user = SecurityUtil.getCurrentUser();
        if (user != null) {
            data.put("favourites", comicService.findFavouriteFromComics(user.getId(), pages.getList()));
        }
        return CommonResult.success(data);
    }

    @ApiOperation("根据漫画id获取漫画章节")
    @GetMapping("/comic/{id}")
    public CommonResult getComic(@PathVariable int id,
                           @RequestParam(name = "asc", defaultValue = "true") boolean asc,
                           @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                           @RequestParam(name = "pageSize", defaultValue = "50") int pageSize) {
        Comic comic = comicService.findByIdWithCategories(id);
        if (comic == null) {
            return CommonResult.validateFailed();
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("comic", comic);
        PageInfo<Chapter> pages = chapterService.findByComicIdByPage(id, asc, pageNum, pageSize);
        data.put("pages", pages);

        /*PageInfo<Comment> comments = commentService.findByComicIdByPage(id, cPageNum, cPageSize);
        HashMap<Integer, Object> repliesMap = new HashMap<>(comments.getSize());
        for (Comment comment : comments.getList()) {
            repliesMap.put(comment.getId(), commentReplyService.findByCommentIdByPage(comment.getId(), 1, 10));
        }
        model.addAttribute("comments", comments);
        model.addAttribute("repliesMap", repliesMap);*/

        User user = SecurityUtil.getCurrentUser();
        Record lastRecord = null;
        if (user != null) {
            lastRecord = recordService.findLastOneByComicId(user.getId(), comic.getId());
            data.put("all_records", recordService.findAllByChapters(user.getId(), pages.getList()));
            data.put("favourite", comicService.hasFavouriteByUser(user.getId(), comic.getId()));
        } else {
            data.put("all_records", new HashMap<>());
            data.put("favourite", null);
        }
        data.put("last_record", lastRecord);
        if (lastRecord == null) {
            data.put("first_chapter", chapterService.findFirstByComicId(id));
        }
        return CommonResult.success(data);
    }

    @ApiOperation("")
    @GetMapping("/comic/{id}/chapters")
    public CommonResult getChaptersOfComic(@PathVariable int id,
                                           @RequestParam(name = "asc", defaultValue = "true") boolean asc,
                                           @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                           @RequestParam(name = "pageSize", defaultValue = "50") int pageSize) {
        Comic comic = comicService.findById(id);
        if (comic == null) {
            return CommonResult.validateFailed();
        }
        return CommonResult.success(chapterService.findByComicIdByPage(id, asc, pageNum, pageSize));
    }

    @ApiOperation("订阅漫画")
    @PostMapping("/comic/{id}/addFavourite")
    @PreAuthorize("hasRole('USER')")
    public CommonResult addFavourite(@PathVariable int id) {
        comicService.addToFavourite(SecurityUtil.getCurrentUserNotNull().getId(), id);
        return CommonResult.success("已订阅漫画");
    }

    @ApiOperation("订阅漫画")
    @PostMapping("/comic/{id}/deleteFavourite")
    @PreAuthorize("hasRole('USER')")
    public CommonResult deleteFavourite(@PathVariable int id) {
        comicService.deleteFavourite(SecurityUtil.getCurrentUserNotNull().getId(), id);
        return CommonResult.success("漫画订阅已取消");
    }

    @ApiOperation("根据漫画id获取下一个漫画")
    @GetMapping("/comic/nextComic")
//    @PreAuthorize("hasAuthority('comic:comic:read')")
    public CommonResult getNextComic(@RequestParam int id) {
//        Comic nextComic
        HashMap<String, Object> data = new HashMap<>();
        Comic comic = comicService.findNextComic(id);
        data.put("comic", comic);
        data.put("chapterCount", chapterService.countByComicId(id));
        data.put("comicCount", comicService.count());
        return CommonResult.success(data);
    }
}
