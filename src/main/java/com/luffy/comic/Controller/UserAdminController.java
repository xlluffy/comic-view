package com.luffy.comic.controller;

import com.github.pagehelper.PageInfo;
import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.model.User;
import com.luffy.comic.service.UserAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "UserAdminController")
@RestController
@RequestMapping("/admin/account")
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminController {
    private static final Log logger = LogFactory.getLog(UserAdminController.class);

    private UserAdminService userAdminService;

    public UserAdminController(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    @ApiOperation("获取用户列表")
    @GetMapping("/index")
    public CommonResult index(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        HashMap<String, Object> data = new HashMap<>();
        PageInfo<User> pages = userAdminService.findUserByPage(pageNum, pageSize);
        data.put("users", pages);
        Map<Integer, List<String>> roles = userAdminService.getRolesByUsers(pages.getList());
        data.put("all_roles", userAdminService.getAllRoles());
//        model.addAttribute("status", pages.getList().stream().map(o -> userMapper.findStatus(o.getId())));
        data.put("roles", roles);
        /*model.addAttribute("others_roles", pages.getList().stream()
                .collect(Collectors.toMap(User::getId,
                        o -> allRoles.stream().filter(r -> !roles.get(o.getId()).contains(r)).collect(Collectors.toList()))));*/
        return CommonResult.success(data);
    }

    @ApiOperation("更新用户role")
    @PostMapping("/addRole")
    @PreAuthorize("#role == 'ROLE_LADMIN' and hasRole('LADMIN') or #role != 'ROLE_LADMIN'")
    public CommonResult addRole(@RequestParam("userId") int userId, @RequestParam("role") String role) {
        logger.info(userId + role);
        userAdminService.addRole(userId, role);
        return CommonResult.success("已为用户添加role");
    }

    @ApiOperation("更新用户role")
    @PostMapping("/removeRole")
    @PreAuthorize("#role == 'ROLE_LADMIN' and hasRole('LADMIN') or #role != 'ROLE_LADMIN'")
    public CommonResult removeRole(@RequestParam("userId") int userId, @RequestParam("role") String role) {
        userAdminService.removeRole(userId, role);
        return CommonResult.success("已删除用户role");
    }

    @ApiOperation("禁言")
    @PostMapping("/mutedUser")
    public CommonResult mutedUser(int userId) {
        userAdminService.removePermission(userId, "comic:comment:comment");
        return CommonResult.success("已禁言用户");
    }

    @ApiOperation("禁用用户")
    @PostMapping("/disabledUser")
    public CommonResult disabledUser(@RequestParam int userId) {
        userAdminService.disabledUser(userId);
        return CommonResult.success("已禁用用户");
    }

    @ApiOperation("启用用户")
    @PostMapping("/enabledUser")
    public CommonResult enabledUser(@RequestParam int userId) {
        userAdminService.enabledUser(userId);
        return CommonResult.success("已启用用户");
    }

    @ApiOperation("删除")
    @PostMapping("/deleteUser")
    public CommonResult deleteUser(@RequestParam int userId) {
        userAdminService.deleteUser(userId);
        return CommonResult.success("已删除用户");
    }
}
