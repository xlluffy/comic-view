package com.luffy.comic.controller;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.mapper.UserMapper;
import com.luffy.comic.model.User;
import com.luffy.comic.service.UserAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "UserAdminController")
@Controller
@RequestMapping("/admin/account")
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminController {
    private static final Log logger = LogFactory.getLog(UserAdminController.class);

    private UserMapper userMapper;
    private UserAdminService userAdminService;

    public UserAdminController(UserMapper userMapper, UserAdminService userAdminService) {
        this.userMapper = userMapper;
        this.userAdminService = userAdminService;
    }

    @ApiOperation("获取用户列表")
    @GetMapping("/index")
    public String index(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                        Model model) {
        PageInfo<User> pages = userAdminService.findUserByPage(pageNum, pageSize);
        model.addAttribute("pages", pages);
        Map<Integer, List<String>> roles = userAdminService.getRolesByUsers(pages.getList());
        List<String> allRoles = userAdminService.getAllRoles();
//        model.addAttribute("status", pages.getList().stream().map(o -> userMapper.findStatus(o.getId())));
        model.addAttribute("roles", roles);
        model.addAttribute("others_roles", pages.getList().stream()
                .collect(Collectors.toMap(User::getId,
                        o -> allRoles.stream().filter(r -> !roles.get(o.getId()).contains(r)).collect(Collectors.toList()))));
        return "/admin/account/account";
    }

    @ApiOperation("更新用户role")
    @PostMapping("/addRole")
    @ResponseBody
    public CommonResult addRole(@RequestParam("userId") int userId, @RequestParam("role") String role) {
        logger.info(userId + role);
        userAdminService.addRole(userId, role);
        return CommonResult.success("已为用户添加role");
    }

    @ApiOperation("更新用户role")
    @PostMapping("/removeRole")
    @ResponseBody
    public CommonResult removeRole(@RequestParam("userId") int userId, @RequestParam("role") String role) {
        userAdminService.removeRole(userId, role);
        return CommonResult.success("已删除用户role");
    }

    @ApiOperation("更新用户role")
    @PostMapping("/mutedUser")
    @ResponseBody
    public CommonResult mutedUser(int userId) {
        userAdminService.removePermission(userId, "comic:comment:comment");
        return CommonResult.success("已禁言用户");
    }

    @ApiOperation("禁用用户")
    @PostMapping("/disabledUser")
    @ResponseBody
    public CommonResult disabledUser(@RequestParam int userId) {
        userAdminService.disabledUser(userId);
        return CommonResult.success("已禁用用户");
    }

    @ApiOperation("启用用户")
    @PostMapping("/enabledUser")
    @ResponseBody
    public CommonResult enabledUser(@RequestParam int userId) {
        userAdminService.enabledUser(userId);
        return CommonResult.success("已启用用户");
    }

    @ApiOperation("删除")
    @PostMapping("/deleteUser")
    @ResponseBody
    public CommonResult deleteUser(@RequestParam int userId) {
        userAdminService.deleteUser(userId);
        return CommonResult.success("已删除用户");
    }
}
