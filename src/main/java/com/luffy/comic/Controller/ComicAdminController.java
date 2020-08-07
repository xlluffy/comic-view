package com.luffy.comic.controller;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.model.Comic;
import com.luffy.comic.service.ChapterService;
import com.luffy.comic.service.ComicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api(tags = "ComicAdminController")
@RestController
@RequestMapping("/admin/comic")
@PreAuthorize("hasRole('ADMIN')")
public class ComicAdminController {
    private static final Log logger = LogFactory.getLog(ComicAdminController.class);

    private ComicService comicService;
    private ChapterService chapterService;

    public ComicAdminController(ComicService comicService, ChapterService chapterService) {
        this.comicService = comicService;
        this.chapterService = chapterService;
    }

    @ApiOperation("漫画列表")
    @GetMapping("/index")
    public CommonResult list(@RequestParam(name = "orderBy", defaultValue = "createTime") String orderBy,
                       @RequestParam(name = "asc", defaultValue = "true") boolean asc,
                        @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        PageInfo<Comic> pages = comicService.findByPage(orderBy, asc, pageNum, pageSize);
        HashMap<Integer, Integer> comicsMap = new HashMap<>();
        for (Comic comic : pages.getList()) {
            comicsMap.put(comic.getId(), chapterService.countByComicId(comic.getId()));
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("comics", pages);
        data.put("comicsMap", comicsMap);
        return CommonResult.success(data);
    }

    @ApiOperation("漫画章节")
    @GetMapping("/{comicId}")
    public CommonResult chapterList(@PathVariable int comicId,
                              @RequestParam(name = "asc", defaultValue = "true") boolean asc,
                            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("comic", comicService.findById(comicId));
        data.put("chapters", chapterService.findByComicIdByPage(comicId, asc, pageNum, pageSize));
        return CommonResult.success(data);
    }

    @ApiOperation("添加漫画")
    @PutMapping("/add")
    @PreAuthorize("hasRole('LADMIN')")
    public CommonResult addComic(@RequestParam String title) {
        Comic comic = comicService.insertByLocalTitle(title);
        return CommonResult.success(chapterService.countByComicId(comic.getId()));
    }

    @ApiOperation("获取可添加漫画列表")
    @GetMapping("/addList")
    @PreAuthorize("hasRole('LADMIN')")
    public CommonResult getAddableComicList(Model model) {
        return CommonResult.success(comicService.findAddableLocalComics());
    }

    @ApiOperation("通过id删除漫画")
    @DeleteMapping("/delete")
    @ResponseBody
    public CommonResult deleteComic(@RequestParam int id) {
        // TODO: 添加数据库备份功能
        if (id > 0) {
//            logger.info("delete comic with id = " + id);
            comicService.deleteById(id);
        }
        return CommonResult.success("", "Delete comic with id = " + id + " succeed.");
//        return "redirect:/admin/comic/";
    }

    @ApiOperation("根据本地目录更新漫画")
    @GetMapping("/update")
    @PreAuthorize("hasRole('LADMIN')")
    public CommonResult updateComics() {
        comicService.updateLocalAll();
        return CommonResult.success("", "Update success.");
    }

    @ApiOperation("为漫画添加指定章节")
    @PutMapping("/{comicId}/add")
    @PreAuthorize("hasRole('LADMIN')")
    public CommonResult addChapter(@PathVariable int comicId, @RequestParam String title) {
        chapterService.insertByLocalTitle(comicId, title);
        return CommonResult.success("", "Add chapter " + title + "succeed.");
    }

    @ApiOperation("获取可添加漫画章节列表")
    @GetMapping("/{comicId}/addList")
    @PreAuthorize("hasRole('LADMIN')")
    public CommonResult getAddableChapterList(@PathVariable int comicId, Model model) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("comic", comicService.findById(comicId));
        data.put("chapters", chapterService.findAddableLocalChapter(comicId));
        return CommonResult.success(data);
    }

    @ApiOperation("删除指定章节")
    @DeleteMapping("/{comicId}/delete")
    public CommonResult deleteChapter(@PathVariable int comicId, @RequestParam String title) {
        if (title != null) {
            chapterService.deleteByComicIdAndTitle(comicId, title);
        }
        return CommonResult.success("", "Delete chapter " + title + "success.");
    }

    @ApiOperation("根据id删除指定章节")
    @DeleteMapping("/deleteChapter")
    public CommonResult deleteChapterById(@RequestParam int chapterId) {
        chapterService.deleteById(chapterId);
        return CommonResult.success("Delete chapter " + chapterId + "success.");
    }
}
