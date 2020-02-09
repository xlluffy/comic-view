package com.luffy.comic.controller;

import com.luffy.comic.common.utils.SecurityUtil;
import com.luffy.comic.model.Chapter;
import com.luffy.comic.model.User;
import com.luffy.comic.service.ChapterService;
import com.luffy.comic.service.ComicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@Api(tags = "ChapterController", description = "漫画章节管理")
@Controller
@RequestMapping("/chapter")
public class ChapterController {
//    private static final Log logger = LogFactory.getLog(ChapterMapper.class);
    private ComicService comicService;
    private ChapterService chapterService;

    public ChapterController(ComicService comicService, ChapterService chapterService) {
        this.comicService = comicService;
        this.chapterService = chapterService;
    }

    @ApiOperation("获取章节信息")
    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('comic:comic:read')")
    public String getChapter(@PathVariable int id,
                             @RequestParam(name = "page", defaultValue = "1") int page,
                             Model model) {
        Chapter chapter = chapterService.findById(id);
        model.addAttribute("chapter", chapter);
        model.addAttribute("prev_chapter", chapterService.findPrevIdById(id));
        model.addAttribute("next_chapter", chapterService.findNextIdById(id));
        User user = SecurityUtil.getCurrentUser();
        if (user != null) {
            model.addAttribute("favourite", comicService.hasFavouriteByUser(user.getId(), chapter.getComic().getId()));
        }
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
