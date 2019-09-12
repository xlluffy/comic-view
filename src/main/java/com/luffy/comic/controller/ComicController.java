package com.luffy.comic.controller;

import com.luffy.comic.model.Chapter;
import com.luffy.comic.model.Comic;
import com.luffy.comic.model.Record;
import com.luffy.comic.service.ChapterService;
import com.luffy.comic.service.ComicService;
import com.luffy.comic.service.RecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "ComicController", description = "漫画管理")
@Controller
@RequestMapping("/comic")
public class ComicController {

    private static final Log logger = LogFactory.getLog(ComicController.class);
//    private static final String root = "D:\\Comic";
    @Autowired
    private ComicService comicService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private RecordService recordService;

    @ApiOperation("获取漫画列表")
    @GetMapping("/index")
//    @PreAuthorize("hasAuthority('comic:comic:read')")
    public String index(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                        Model model) {
        List<Comic> comics = comicService.findByPage(pageNum, pageSize).getList();
        model.addAttribute("comics", comics);
        Record lastRecord = recordService.findLastOne();
        model.addAttribute("last_record", lastRecord);
        model.addAttribute("last_comic", comicService.findByChapterId(lastRecord.getChapter().getId()));
        model.addAttribute("all_records", recordService.findAllByComics(comics));
        return "index";
    }

    @ApiOperation("添加漫画")
    @GetMapping("/add")
    @ResponseBody
//    @PreAuthorize("hasAuthority('comic:comic:create')")
    public String addComic(@RequestParam String title) {
        comicService.insertByLocalTitle(title);
        return "Add comic " + title + " succeed";
    }

    @ApiOperation("删除漫画")
    @DeleteMapping("/delete/{id}")
    @ResponseBody
//    @PreAuthorize("hasAuthority('comic:comic:delete')")
    public String deleteComic(@PathVariable int id) {
        if (id > 0) {
            comicService.deleteById(id);
        }
        return "";
    }

    @ApiOperation("根据本地目录更新漫画")
    @GetMapping("/update")
    @ResponseBody
//    @PreAuthorize("hasAuthority('comic:comic:update')")
    public String updateComics() {
        comicService.updateLocalAll();
        return "Update succeed.";
    }

    @ApiOperation("根据漫画id获取漫画章节")
    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('comic:comic:read')")
    public String getComic(@PathVariable int id,
                           @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                           @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                           Model model) {
        Comic comic = comicService.findById(id);
        if (comic == null)
            return "error/404";
        model.addAttribute("comic", comic);

        List<Chapter> chapters = chapterService.findByComicIdByPage(id, pageNum, pageSize).getList();
        model.addAttribute("chapters", chapters);
        model.addAttribute("all_records", recordService.findAllByChapters(chapters));
        model.addAttribute("last_record", recordService.findLastOneByComicId(comic.getId()));
        return "comic";
    }

    @ApiOperation("为漫画添加指定章节")
    @GetMapping("/{id}/addChapter")
    @ResponseBody
//    @PreAuthorize("hasAuthority('comic:chapter:create')")
    public String addChapter(@PathVariable int id, @RequestParam String title) {
        chapterService.insertByLocalTitle(id, title);
        return "Add chapter " + title + "succeed.";
    }

    @ApiOperation("删除指定章节")
    @DeleteMapping("/{id}/deleteChapter")
    @ResponseBody
//    @PreAuthorize("hasAuthority('comic:chapter:delete')")
    public String deleteChapter(@PathVariable int id, @RequestParam String title) {
        if (title != null) {
            chapterService.deleteByComicIdAndTitle(id, title);
        }
        return "Delete chapter " + title + "succeed.";
    }

    @ApiOperation("测试方法")
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        Comic comic = new Comic(0, "Hayate2", "fesf", null);
//        comicService.insertOrUpdate(comic);
//        logger.info("insert:" + comic);
        comic.setFullTitle("Hayate2");
//        comic.setId(0);
        comicService.insertOrUpdate(comic);
        logger.info("update" + comic);
        return "";
    }
}
