package com.luffy.comic.controller;

import com.luffy.comic.service.CategoryService;
import com.luffy.comic.service.ComicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(tags = "TagController")
@Controller
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
    public String index(@RequestParam(name = "categoryId", defaultValue = "1") int categoryId,
                        @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                        Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("pages", comicService.findByCategoryIdByPage(categoryId, pageNum, pageSize));
        return "category";
    }

    @ApiOperation("按tag.id搜索漫画")
    @GetMapping("/{id}")
    public String details(@PathVariable int id) {
        return "";
    }
}
