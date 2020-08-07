package com.luffy.comic.controller;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.nosql.elasticsearch.document.EsComic;
import com.luffy.comic.service.ComicService;
import com.luffy.comic.service.EsComicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "EsComicController")
@RequestMapping("/esComic")
//@RestController
public class EsComicController {
    private static final Logger logger = LoggerFactory.getLogger(EsComicController.class);
    private EsComicService esComicService;
    private ComicService comicService;

    public EsComicController(EsComicService esComicService, ComicService comicService) {
        this.esComicService = esComicService;
        this.comicService = comicService;
    }

    @ApiOperation("导入所有数据库中的comic数据到es")
    @PostMapping("/importAll")
    @PreAuthorize("hasRole('USER')")
    public CommonResult importAll() {
        esComicService.importAll();
        return CommonResult.success("数据全部导入成功");
    }

    @ApiOperation("根据id删除comic")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public CommonResult delete(@PathVariable Long id) {
        esComicService.delete(id);
        return CommonResult.success("comic删除成功");
    }

    @ApiOperation("根据id批量删除comic")
    @DeleteMapping("/delete/batch")
    @PreAuthorize("hasRole('USER')")
    public CommonResult deleteAll(@RequestParam("ids")List<Long> ids) {
        esComicService.delete(ids);
        return CommonResult.success("comics删除成功");
    }

    @ApiOperation("根据id添加数据库中数据到es")
    @PostMapping("/create/{id}")
    @PreAuthorize("hasRole('USER')")
    public CommonResult create(@PathVariable Long id) {
        esComicService.create(id);
        return CommonResult.success("comic导入成功");
    }

    @ApiOperation("简单搜索")
    @GetMapping("/search/simple")
    public CommonResult searchSimple(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                               @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        HashMap<String, Object> data = new HashMap<>();
        List<EsComic> comics = esComicService.searchByTitle(keyword, pageNum, pageSize).getList();
        if (comics.isEmpty()) {
            comics = esComicService.searchByAuthor(keyword, pageNum, pageSize).getList();
            data.put("keyword", false);
            data.put("data", comics.stream().map(EsComic::getAuthor).collect(Collectors.toList()));
        } else {
            data.put("keyword", true);
            data.put("data", comics);
        }
        return CommonResult.success(data);
    }

    @ApiOperation("按关键词或者作者名搜索")
    @GetMapping("/search")
    public CommonResult search(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false) String author,
                             @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                             @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                             Model model) {
        // 太蠢了，现在author表未分离，而作者页面仅有count一个额外的信息，不如用findByTitleOrByFullTitleOrAuthor来得直接...
        HashMap<String, Object> data = new HashMap<>();
        if (keyword != null && author != null) {
//            data.put("keyword", keyword + " - " + author);
            data.put("comics", esComicService.searchByKeywordAndAuthor(keyword, author, pageNum, pageSize));
        } else if (keyword != null) {
//            data.put("keyword", keyword);
            PageInfo<EsComic> pages = esComicService.searchByTitle(keyword, pageNum, pageSize);
            if (pages.getList().isEmpty()) {
                pages = esComicService.searchByAuthor(keyword, pageNum, pageSize);
                if (!pages.getList().isEmpty()) {
                    data.put("count", comicService.countByAuthor(keyword));
                } else {
                    data.put("count", 0);
                }
            }
            data.put("comics", pages);
        } else if (author != null){
            // 避免es搜索结果出现相似作者的作品
//            data.put("keyword", author);
            data.put("count", comicService.countByAuthor(author));
            data.put("comics", comicService.findByAuthorByPage(author, pageNum, pageSize));
        } else {
            // something here...
            return CommonResult.failed("Not found");
        }
        return CommonResult.success(data);
    }
}
