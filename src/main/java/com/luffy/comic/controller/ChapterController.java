package com.luffy.comic.controller;

import com.luffy.comic.mapper.ChapterMapper;
import com.luffy.comic.model.Chapter;
import com.luffy.comic.service.ChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Api(tags = "ChapterController", description = "漫画章节管理")
@Controller
@RequestMapping("/chapter")
public class ChapterController {

    private static final Log logger = LogFactory.getLog(ChapterMapper.class);
    private static final String root = "D:\\Comic";
    @Autowired
    private ChapterService chapterService;

    private String splitSuffix(String filename) {
        return filename.substring(filename.indexOf('.'));
    }

    @ApiOperation("更新章节")
    @RequestMapping("/update")
    @ResponseBody
    public String updateChapters() {
        for (String dir : Objects.requireNonNull(new File(root).list())) {
            String[] pages = new File(root + "\\" + dir).list();
            System.out.println(pages[0]);
//            Chapter chapter = new Chapter(dir, Objects.requireNonNull(pages).length, splitSuffix(pages[0]));
//            chapterMapper.insert(chapter);
//            System.out.println(chapter.getSuffix());
        }
        return "update succeed.";
    }

    @ApiOperation("获取章节信息")
    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('comic:chapter:read')")
    public String getChapter(@PathVariable int id, @RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        Chapter chapter = chapterService.findById(id);
        model.addAttribute("comic_id", chapter.getComic().getId());
        model.addAttribute("chapter", chapter);
        model.addAttribute("prev_chapter", chapterService.findPrevIdById(id));
        model.addAttribute("next_chapter", chapterService.findNextIdById(id));
        return "chapter";
    }

    @ApiOperation("获取所有章节信息")
    @GetMapping("/all")
    @ResponseBody
    public List<String> allChapters() {
        List<String> result = new LinkedList<>();
//        for (Chapter chapter : chapterMapper.findAll()) {
//            result.add(chapter.getTitle() + ": " + chapter.getPages());
//        }
        return result;
    }
}
