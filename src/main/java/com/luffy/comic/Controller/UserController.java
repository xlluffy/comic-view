package com.luffy.comic.controller;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.common.utils.SecurityUtil;
import com.luffy.comic.dto.UserProfileParamForm;
import com.luffy.comic.model.Comic;
import com.luffy.comic.model.Record;
import com.luffy.comic.model.User;
import com.luffy.comic.service.ComicService;
import com.luffy.comic.service.RecordService;
import com.luffy.comic.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "UserController")
@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
public class UserController {
    private static final Log logger = LogFactory.getLog(UserController.class);

    private ComicService comicService;
    private RecordService recordService;
    private UserService userService;

    public UserController(ComicService comicService, RecordService recordService, UserService userService) {
        this.comicService = comicService;
        this.recordService = recordService;
        this.userService = userService;
    }

    @ApiOperation("获取订阅信息")
    @GetMapping("/{userId}/favourite")
    public String myFavourite(@PathVariable int userId,
                              @RequestParam(name = "orderBy", defaultValue = "createTime") String orderBy,
                              @RequestParam(name = "asc", defaultValue = "false") boolean asc,
                            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                            Model model) {
        User user = SecurityUtil.getCurrentUserNotNull();
        if (user.getId() != userId) {
            return "redirect:/user/" + userId + "/otherFavourite";
        }
        PageInfo<Comic> pages = comicService.findByUserByPage(user.getId(), orderBy, asc, pageNum, pageSize);
        Record lastRecord = recordService.findLastOne(user.getId());
        model.addAttribute("pages", pages);
        model.addAttribute("last_record", lastRecord);
        model.addAttribute("all_records", recordService.findAllByComics(user.getId(), pages.getList()));
        return "/user/favourite";
    }

    @ApiOperation("获取他人订阅信息")
    @GetMapping("/{userId}/otherFavourite")
    public String otherFavourite(@PathVariable int userId,
                                 @RequestParam(name = "sortBy", defaultValue = "createTime") String orderBy,
                                 @RequestParam(name = "asc", defaultValue = "false") boolean asc,
                              @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                              Model model) {
        PageInfo<Comic> pages = comicService.findByUserByPage(userId, orderBy, asc, pageNum, pageSize);
        model.addAttribute("pages", pages);
        return "/user/favourite";
    }

    @ApiOperation("获取历史记录")
    @GetMapping("/record")
    public String myRecord(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                           @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                           Model model) {
        int userId = SecurityUtil.getCurrentUserNotNull().getId();
        model.addAttribute("pages", recordService.findAllLastOneOfComicsByPage(userId, pageNum, pageSize));
        return "/user/record";
    }

    @ApiOperation("用户信息设置界面")
    @GetMapping("/profile")
    public String profile() {
        return "/user/profile";
    }

    @ApiOperation("用户信息设置界面")
    @GetMapping("/profile/safety")
    public String profileSafety() {
        return "/user/profile-safety";
    }

    @ApiOperation("用户设置修改")
    @PostMapping("/profile/update")
    @ResponseBody
    public CommonResult updateProfile(@Valid @RequestBody UserProfileParamForm user, BindingResult result) {
        if (!result.hasErrors()) {
            user.setId(SecurityUtil.getCurrentUserNotNull().getId());
            logger.info(user.toString());
            return CommonResult.success(null, "修改信息成功");
        }
        return CommonResult.failed("信息格式错误");
    }

    @ApiOperation("邮箱修改")
    @PostMapping("/profile/updateEmail")
    @ResponseBody
    public CommonResult updateEmail(@RequestParam("oldEmail") String oldEmail,
                                    @RequestParam("newEmail") String newEmail) {
        if (userService.updateEmail(oldEmail, newEmail)) {
            return CommonResult.success(null, "修改邮箱成功");
        }
        return CommonResult.failed("邮箱不匹配");
    }

    @ApiOperation("密码修改")
    @PostMapping("/profile/updatePwd")
    @ResponseBody
    public CommonResult updatePwd(@RequestParam("oldPwd") String oldPwd,
                                  @RequestParam("newPwd") String newPwd) {
        if (userService.updatePwd(oldPwd, newPwd)) {
            return CommonResult.success(null, "修改密码成功");
        }
        return CommonResult.failed("密码修改失败");
    }
}
