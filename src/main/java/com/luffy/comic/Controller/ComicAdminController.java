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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Api(tags = "ComicAdminController", description = "漫画管理")
@Controller
@RequestMapping("/admin/comic")
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
    public String list(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                        Model model) {
        PageInfo<Comic> pages = comicService.findByPage(pageNum, pageSize);
        List<Comic> comics = pages.getList();
        HashMap<Integer, Integer> pageMap = new HashMap<>();
        for (Comic comic : comics) {
            pageMap.put(comic.getId(), chapterService.countByComicId(comic.getId()));
        }
        model.addAttribute("pages", pages);
        model.addAttribute("comics", comics);
        model.addAttribute("pageMap", pageMap);
//        model.addAttribute("count", comicService.count());
        return "admin/comic/index";
    }

    @ApiOperation("漫画章节")
    @GetMapping("/{comicId}")
    public String chapterList(@PathVariable int comicId,
                            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                            Model model) {
        model.addAttribute("comic", comicService.findById(comicId));
        PageInfo<Chapter> pages = chapterService.findByComicIdByPage(comicId, pageNum, pageSize);
        model.addAttribute("pages", pages);
        model.addAttribute("chapters", pages.getList());
        model.addAttribute("count", chapterService.countByComicId(comicId));
        return "admin/comic/comic";
    }

    @ApiOperation("添加漫画")
    @PutMapping("/add")
    @ResponseBody
//    @PreAuthorize("hasAuthority('comic:comic:create')")
    public CommonResult addComic(@RequestParam String title) {
        comicService.insertByLocalTitle(title);
        return CommonResult.success("", "Add comic " + title + " succeed.");
    }

    @ApiOperation("获取可添加漫画列表")
    @GetMapping("/addList")
//    @PreAuthorize("hasAuthority('comic:comic:read')")
    public String getAddableComicList(Model model) {
//        return CommonResult.success(comicService.findAddableLocalComics(), "Get comics list succeed.");
        model.addAttribute("comicsMap", comicService.findAddableLocalComics());
        return "/admin/comic/comic-local";
    }

    @ApiOperation("通过id删除漫画")
    @DeleteMapping("/delete")
    @ResponseBody
//    @PreAuthorize("hasAuthority('comic:comic:delete')")
    public CommonResult deleteComic(@RequestParam int id) {
        // TODO: 添加数据库备份功能
        if (id > 0) {
            comicService.deleteById(id);
        }
        return CommonResult.success("", "Delete comic with id = " + id + " succeed.");
//        return "redirect:/admin/comic/";
    }

    /*@ApiOperation("通过title删除漫画")
    @DeleteMapping("/delete")
    @ResponseBody
//    @PreAuthorize("hasAuthority('comic:comic:delete')")
    public CommonResult deleteComic(@RequestParam String title) {
        if (title != null) {
            comicService.deleteByTitle(title);
        }
        return CommonResult.success("", "Delete comic with title = " + title + " succeed.");
    }*/

    @ApiOperation("根据本地目录更新漫画")
    @GetMapping("/update")
    @ResponseBody
//    @PreAuthorize("hasAuthority('comic:comic:update')")
    public CommonResult updateComics() {
        comicService.updateLocalAll();
        return CommonResult.success("", "Update succeed.");
    }

    @ApiOperation("为漫画添加指定章节")
    @PutMapping("/{comicId}/add")
    @ResponseBody
//    @PreAuthorize("hasAuthority('comic:chapter:create')")
    public CommonResult addChapter(@PathVariable int comicId, @RequestParam String title) {
        chapterService.insertByLocalTitle(comicId, title);
        return CommonResult.success("", "Add chapter " + title + "succeed.");
    }

    @ApiOperation("获取可添加漫画章节列表")
    @GetMapping("/{comicId}/addList")
//    @PreAuthorize("hasAuthority('comic:chapter:read')")
    public String getAddableChapterList(@PathVariable int comicId, Model model) {
        Comic comic = comicService.findById(comicId);
        model.addAttribute("comic", comicService.findById(comicId));
        model.addAttribute("chapters", chapterService.findAddableLocalChapter(comicId));
        return "admin/comic/chapter-local";
        /*return CommonResult.success(chapterService.findAddableLocalChapter(comicId),
                "Get chapter of " + comicId + " succeed.");*/
    }

    @ApiOperation("删除指定章节")
    @DeleteMapping("/{comicId}/delete")
    @ResponseBody
//    @PreAuthorize("hasAuthority('comic:chapter:delete')")
    public CommonResult deleteChapter(@PathVariable int comicId, @RequestParam String title) {
        if (title != null) {
            chapterService.deleteByComicIdAndTitle(comicId, title);
        }
        return CommonResult.success("", "Delete chapter " + title + "succeed.");
    }
}
