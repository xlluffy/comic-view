package com.luffy.comic.controller;

import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.common.utils.SecurityUtil;
import com.luffy.comic.model.Chapter;
import com.luffy.comic.model.User;
import com.luffy.comic.service.ChapterService;
import com.luffy.comic.service.ComicService;
import com.luffy.comic.service.RecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Api(tags = "ChapterController", description = "漫画章节管理")
@RestController
@RequestMapping("/chapter")
public class ChapterController {
//    private static final Log logger = LogFactory.getLog(ChapterMapper.class);
    private ComicService comicService;
    private ChapterService chapterService;

    public ChapterController(ComicService comicService, ChapterService chapterService, RecordService recordService) {
        this.comicService = comicService;
        this.chapterService = chapterService;
    }

    @ApiOperation("获取章节信息")
    @GetMapping("/{id}")
    public CommonResult getChapter(@PathVariable int id, @RequestParam(name = "page", defaultValue = "-1") int page) {
        Chapter chapter = chapterService.findById(id);
        HashMap<String, Object> data = new HashMap<>();
        data.put("chapter", chapter);
        data.put("prevChapter", chapterService.findPrevIdById(id));
        data.put("nextChapter", chapterService.findNextIdById(id));
        User user = SecurityUtil.getCurrentUser();
        if (user != null) {
            /*if (page == -1) {
                Record record = recordService.findByChapterId(user.getId(), id);
                if (record != null)
                    response.setHeader("page", record.getPage());
            }*/
            data.put("favourite", comicService.hasFavouriteByUser(user.getId(), chapter.getComic().getId()));
        }
        return CommonResult.success(data);
    }

    @ApiOperation("获取所有章节信息")
    @GetMapping("/all")
    public List<String> allChapters() {
        List<String> result = new LinkedList<>();
//        for (Chapter chapter : chapterMapper.findAll()) {
//            result.add(chapter.getTitle() + ": " + chapter.getPages());
//        }
        return result;
    }
}
