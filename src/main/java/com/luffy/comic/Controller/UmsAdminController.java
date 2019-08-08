package com.luffy.comic.controller;

import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.model.UmsAdmin;
import com.luffy.comic.model.UmsPermission;
import com.luffy.comic.service.UmsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Api(tags = "UmsAdminController", description = "后台用户管理")
@RequestMapping("/admin")
public class UmsAdminController {
    @Autowired
    private UmsAdminService adminService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

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

    @ApiOperation("登陆以后返回token")
    @PostMapping("/login")
    @ResponseBody
    public CommonResult login(@RequestParam(defaultValue = "test") String username, @RequestParam(defaultValue = "123456") String password/*, BindingResult result*/) {
        String token = adminService.login(username, password);
        if (token == null) {
            // Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0IiwiY3JlYXRlZCI6MTU2NTI1MTYxOTQyOCwiZXhwIjoxNTY1MjUyMjI1fQ.OgdV2nROheAgPzLZTc7xufuzUvgD9602g4FbwbsJYQl0qcbYEwRRyRBjb0mEGO05eoef1TVTC03HZTNbG3vxzA
            return CommonResult.validateFailed("用户名或者密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation("获取用户所有权限（包括+-权限）")
    @GetMapping("/permission/{adminId}")
    @ResponseBody
    public CommonResult getPermissionList(@PathVariable int adminId) {
        List<UmsPermission> permissionList = adminService.getPermissionList(adminId);
        return CommonResult.success(permissionList);
    }
}
