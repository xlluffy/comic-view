package com.luffy.comic.controller;

import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.common.utils.CookieUtil;
import com.luffy.comic.model.UmsAdmin;
import com.luffy.comic.model.UmsPermission;
import com.luffy.comic.service.UmsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Api(tags = "UmsAdminController", description = "后台用户管理")
@RequestMapping("/admin")
public class UmsAdminController {
    private UmsAdminService adminService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    public UmsAdminController(UmsAdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/register")
    @ResponseBody
    public CommonResult register() {
        return CommonResult.success("");
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    @ResponseBody
    public CommonResult register(@RequestBody UmsAdmin admin, BindingResult result) {
        UmsAdmin umsAdmin = adminService.register(admin);
        if (umsAdmin == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(umsAdmin);
    }

    @ApiOperation("登陆界面")
    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @ApiOperation("登陆以后将token写入cookie")
    @PostMapping("/login")
    public String login(HttpServletResponse response, String username, String password/*, BindingResult result*/) {
        String token = adminService.login(username, password);
        if (token == null) {
            return "admin/login";
        }
        CookieUtil.setCookie(response, tokenHeader, tokenHead + token);
        return "redirect:/comic/index";
    }

    @ApiOperation("登陆以后返回token")
    @PostMapping("/loginAuth")
    @ResponseBody
    public CommonResult loginAuth(HttpServletResponse response, String username, String password/*, BindingResult result*/) {
        String token = adminService.login(username, password);
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        response.setHeader(tokenHeader, tokenHead + token);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation("登出")
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        adminService.logout();
        CookieUtil.removeCookie(request, response, tokenHeader);
        return "redirect:/admin/login";
    }

    @ApiOperation("获取用户所有权限（包括+-权限）")
    @GetMapping("/permission/{adminId}")
    @ResponseBody
    public CommonResult getPermissionList(@PathVariable int adminId) {
        List<UmsPermission> permissionList = adminService.getPermissionList(adminId);
        return CommonResult.success(permissionList);
    }
}
