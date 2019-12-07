package com.luffy.comic.controller;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.common.api.CommonResult;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "ComicController", description = "漫画主页")
@Controller
@RequestMapping("/comic")
public class ComicController {
    private static final Log logger = LogFactory.getLog(ComicController.class);

    private ComicService comicService;
    private ChapterService chapterService;
    private RecordService recordService;

    public ComicController(ComicService comicService, ChapterService chapterService, RecordService recordService) {
        this.comicService = comicService;
        this.chapterService = chapterService;
        this.recordService = recordService;
    }

    @ApiOperation("获取漫画列表信息")
    @GetMapping("/index")
//    @PreAuthorize("hasAuthority('comic:comic:read')")
    public String index(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                        Model model) {
        PageInfo<Comic> pages = comicService.findByPage(pageNum, pageSize);
        Record lastRecord = recordService.findLastOne();
        model.addAttribute("pages", pages);
        model.addAttribute("comics", pages.getList());
        model.addAttribute("last_record", lastRecord);
        model.addAttribute("last_comic", comicService.findByChapterId(lastRecord.getChapter().getId()));
        model.addAttribute("all_records", recordService.findAllByComics(pages.getList()));
        return "index";
    }

   /* @ApiOperation("获取漫画列表")
    @GetMapping("/getComicsList")
    @ResponseBody
//    @PreAuthorize("hasAuthority('comic:comic:read')")
    public Map<String, Object> getComicsList(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                             @RequestParam(name = "pageSize", defaultValue = "20") int pageSize){
        List<Comic> comics = comicService.findByPage(pageNum, pageSize).getList();
        Map<String, Object> map = new HashMap<>();
        map.put("count", comicService.count());
        map.put("comics", comics);
        map.put("all_records", recordService.findAllByComics(comics));
        return map;
    }*/

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

        PageInfo<Chapter> pages = chapterService.findByComicIdByPage(id, pageNum, pageSize);
        model.addAttribute("pages", pages);
        model.addAttribute("chapters", pages.getList());
        model.addAttribute("all_records", recordService.findAllByChapters(pages.getList()));
        model.addAttribute("last_record", recordService.findLastOneByComicId(comic.getId()));
        return "comic";
    }

    @ApiOperation("根据漫画id获取下一个漫画")
    @GetMapping("/nextComic")
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
        List<Comic> comics = comicService.findByPage(1, 20).getList();
        Record lastRecord = recordService.findLastOne();
        int count = comicService.count();
        Comic comic = comicService.findByChapterId(lastRecord.getChapter().getId());
        Map<Integer, Record> record = recordService.findAllByComics(comics);
        return "";
    }
}
