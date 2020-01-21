package com.luffy.comic.controller;

import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.dto.AdminUserDetails;
import com.luffy.comic.model.Permission;
import com.luffy.comic.model.User;
import com.luffy.comic.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Api(tags = "UserAdminController", description = "后台用户管理")
@RequestMapping("/admin")
public class UserAdminController {
    private static final Logger logger = LoggerFactory.getLogger(UserAdminController.class);

    private UserService adminService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    public UserAdminController(UserService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/register")
    public String register() {
        return "admin/register";
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    @ResponseBody
    public CommonResult register(@Valid User admin/*, BindingResult result*/) {
        User user = adminService.register(admin);
        if (user == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(user);
    }

    @ApiOperation("登陆界面")
    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @ApiOperation("登陆以后将token写入cookie")
    @PostMapping("/login")
    public String login(String username, String password) {
        adminService.login(username, password);
        User admin = getAdmin();
        if (admin != null)
            logger.info("USER: " + admin.getUsername() + " LOGIN SUCCESS.");
        return "redirect:/comic/index";
    }

    @ApiOperation("登陆以后返回token")
    @PostMapping("/loginAuth")
    @ResponseBody
    public CommonResult loginAuth(HttpServletResponse response, String username, String password/*, BindingResult result*/) {
        String token = adminService.loginAuth(username, password);
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        response.setHeader(tokenHeader, tokenHead + token);
        return CommonResult.success(tokenMap);
    }

    /*@ApiOperation("登出")
    @GetMapping("/logout")
    public String logout() {
        adminService.logout();

//        CookieUtil.removeCookie(request, response, tokenHeader);
        return "redirect:/admin/login";
    }*/

    @ApiOperation("获取用户所有权限（包括+-权限）")
    @GetMapping("/permission/{adminId}")
    @ResponseBody
    public CommonResult getPermissionList(@PathVariable int adminId) {
        List<Permission> permissionList = adminService.getPermissionList(adminId);
        return CommonResult.success(permissionList);
    }

    private User getAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AdminUserDetails) {
            return (User)principal;
        }
        return null;
    }
}
