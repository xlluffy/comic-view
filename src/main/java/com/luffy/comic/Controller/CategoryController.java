package com.luffy.comic.controller;

import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.service.CategoryService;
import com.luffy.comic.service.ComicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api(tags = "TagController")
@RestController
@RequestMapping("/category")
public class CategoryController {
    private static final Log logger = LogFactory.getLog(CategoryController.class);

    private ComicService comicService;
    private CategoryService categoryService;

    public CategoryController(ComicService comicService, CategoryService categoryService) {
        this.comicService = comicService;
        this.categoryService = categoryService;
    }

    @ApiOperation("分类主页")
    @GetMapping({"", "/index"})
    public CommonResult index(@RequestParam(name = "categoryId", defaultValue = "1") int categoryId,
                              @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("categories", categoryService.findAll());
        data.put("comics", comicService.findByCategoryIdByPage(categoryId, pageNum, pageSize));
        return CommonResult.success(data);
    }

    @ApiOperation("按tag.id搜索漫画")
    @GetMapping("/{id}")
    public String details(@PathVariable int id) {
        return "";
    }
}
