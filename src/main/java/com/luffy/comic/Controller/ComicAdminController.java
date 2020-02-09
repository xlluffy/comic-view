package com.luffy.comic.controller;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.model.Chapter;
import com.luffy.comic.model.Comic;
import com.luffy.comic.service.ChapterService;
import com.luffy.comic.service.ComicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api(tags = "ComicAdminController")
@Controller
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
    public String list(@RequestParam(name = "orderBy", defaultValue = "createTime") String orderBy,
                       @RequestParam(name = "asc", defaultValue = "true") boolean asc,
                        @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                        Model model) {
        PageInfo<Comic> pages = comicService.findByPage(orderBy, asc, pageNum, pageSize);
        HashMap<Integer, Integer> comicsMap = new HashMap<>();
        for (Comic comic : pages.getList()) {
            comicsMap.put(comic.getId(), chapterService.countByComicId(comic.getId()));
        }
        model.addAttribute("pages", pages);
        model.addAttribute("comicsMap", comicsMap);
        return "admin/comic/index";
    }

    @ApiOperation("漫画章节")
    @GetMapping("/{comicId}")
    public String chapterList(@PathVariable int comicId,
                              @RequestParam(name = "asc", defaultValue = "true") boolean asc,
                            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                            Model model) {
        model.addAttribute("comic", comicService.findById(comicId));
        PageInfo<Chapter> pages = chapterService.findByComicIdByPage(comicId, asc, pageNum, pageSize);
        model.addAttribute("pages", pages);
        return "admin/comic/comic";
    }

    @ApiOperation("添加漫画")
    @PutMapping("/add")
    @ResponseBody
    @PreAuthorize("hasRole('LADMIN')")
    public CommonResult addComic(@RequestParam String title) {
        comicService.insertByLocalTitle(title);
        return CommonResult.success("", "Add comic " + title + " succeed.");
    }

    @ApiOperation("获取可添加漫画列表")
    @GetMapping("/addList")
    @PreAuthorize("hasRole('LADMIN')")
    public String getAddableComicList(Model model) {
//        return CommonResult.success(comicService.findAddableLocalComics(), "Get comics list succeed.");
        model.addAttribute("localComicsMap", comicService.findAddableLocalComics());
        return "/admin/comic/comic-local";
    }

    @ApiOperation("通过id删除漫画")
    @DeleteMapping("/delete")
    @ResponseBody
    public CommonResult deleteComic(@RequestParam int id) {
        // TODO: 添加数据库备份功能
        if (id > 0) {
            comicService.deleteById(id);
        }
        return CommonResult.success("", "Delete comic with id = " + id + " succeed.");
//        return "redirect:/admin/comic/";
    }

    @ApiOperation("根据本地目录更新漫画")
    @GetMapping("/update")
    @ResponseBody
    @PreAuthorize("hasRole('LADMIN')")
    public CommonResult updateComics() {
        comicService.updateLocalAll();
        return CommonResult.success("", "Update succeed.");
    }

    @ApiOperation("为漫画添加指定章节")
    @PutMapping("/{comicId}/add")
    @ResponseBody
    @PreAuthorize("hasRole('LADMIN')")
    public CommonResult addChapter(@PathVariable int comicId, @RequestParam String title) {
        chapterService.insertByLocalTitle(comicId, title);
        return CommonResult.success("", "Add chapter " + title + "succeed.");
    }

    @ApiOperation("获取可添加漫画章节列表")
    @GetMapping("/{comicId}/addList")
    @PreAuthorize("hasRole('LADMIN')")
    public String getAddableChapterList(@PathVariable int comicId, Model model) {
//        Comic comic = comicService.findById(comicId);
        model.addAttribute("comic", comicService.findById(comicId));
        model.addAttribute("localChapters", chapterService.findAddableLocalChapter(comicId));
        return "admin/comic/chapter-local";
        /*return CommonResult.success(chapterService.findAddableLocalChapter(comicId),
                "Get chapter of " + comicId + " succeed.");*/
    }

    @ApiOperation("删除指定章节")
    @DeleteMapping("/{comicId}/delete")
    @ResponseBody
    public CommonResult deleteChapter(@PathVariable int comicId, @RequestParam String title) {
        if (title != null) {
            chapterService.deleteByComicIdAndTitle(comicId, title);
        }
        return CommonResult.success("", "Delete chapter " + title + "succeed.");
    }
}
