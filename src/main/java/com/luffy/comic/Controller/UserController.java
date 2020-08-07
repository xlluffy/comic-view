package com.luffy.comic.controller;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.common.utils.SafetyUser;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@Api(tags = "UserController")
@RestController
@RequestMapping("/user")
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
    @GetMapping({"/", "/favourite"})
    @PreAuthorize("hasRole('USER')")
    public CommonResult myFavourite(@RequestParam(name = "orderBy", defaultValue = "createTime") String orderBy,
                              @RequestParam(name = "asc", defaultValue = "false") boolean asc,
                              @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        User user = SecurityUtil.getCurrentUserNotNull();
        PageInfo<Comic> pages = comicService.findByUserByPage(user.getId(), orderBy, asc, pageNum, pageSize);
        Record lastRecord = recordService.findLastOne(user.getId());
        HashMap<String, Object> data = new HashMap<>();
        data.put("comics", pages);
        data.put("last_record", lastRecord);
        data.put("all_records", recordService.findAllByComics(user.getId(), pages.getList()));
        return CommonResult.success(data);
    }

    @ApiOperation("获取他人订阅信息")
    @GetMapping("/{userId}/favourite")
    public CommonResult otherFavourite(@PathVariable int userId,
                                 @RequestParam(name = "sortBy", defaultValue = "createTime") String orderBy,
                                 @RequestParam(name = "asc", defaultValue = "false") boolean asc,
                              @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        User user = userService.getById(userId);
        if (user == null) {
            return CommonResult.failed("Not Found");
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("user", SafetyUser.secretUser(user));
        data.put("comics", comicService.findByUserByPage(userId, orderBy, asc, pageNum, pageSize));
        return CommonResult.success(data);
    }

    @ApiOperation("获取用户基础信息")
    @GetMapping("/info")
    @PreAuthorize("hasRole('USER')")
    public CommonResult userInfo() {
        User user = SecurityUtil.getCurrentUserNotNull();
        return CommonResult.success(SafetyUser.safetyUser(user));
    }

    @ApiOperation("获取历史记录")
    @GetMapping("/record")
    @PreAuthorize("hasRole('USER')")
    public CommonResult myRecord(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        int userId = SecurityUtil.getCurrentUserNotNull().getId();
        return CommonResult.success(recordService.findAllLastOneOfComicsByPage(userId, pageNum, pageSize));
    }

    @ApiOperation("用户设置修改")
    @PostMapping("/profile/update")
    @PreAuthorize("hasRole('USER')")
    public CommonResult updateProfile(@Valid @RequestBody UserProfileParamForm user, BindingResult result) {
        if (!result.hasErrors()) {
            user.setId(SecurityUtil.getCurrentUserNotNull().getId());
            userService.update(user);
            return CommonResult.success("修改信息成功");
        }
        return CommonResult.failed("信息格式错误");
    }

    @ApiOperation("邮箱修改")
    @PostMapping("/profile/updateEmail")
    @PreAuthorize("hasRole('USER')")
    public CommonResult updateEmail(@RequestParam("oldEmail") String oldEmail,
                                    @RequestParam("newEmail") String newEmail) {
        if (userService.updateEmail(oldEmail, newEmail)) {
            return CommonResult.success("修改邮箱成功");
        }
        return CommonResult.failed("邮箱不匹配");
    }

    @ApiOperation("密码修改")
    @PostMapping("/profile/updatePass")
    @PreAuthorize("hasRole('USER')")
    public CommonResult updatePwd(@RequestParam("oldPass") String oldPass,
                                  @RequestParam("newPass") String newPass) {
        if (userService.updatePwd(oldPass, newPass)) {
            return CommonResult.success(null, "修改密码成功");
        }
        return CommonResult.failed("密码修改失败");
    }
}
