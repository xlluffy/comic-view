package com.luffy.comic.controller;

import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.common.utils.SafetyUser;
import com.luffy.comic.common.utils.SecurityUtil;
import com.luffy.comic.model.User;
import com.luffy.comic.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@Api(tags = "UserLoginController")
public class UserLoginController {
    private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);

    private UserService userService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public CommonResult register(@Valid User admin, BindingResult result) {
        if (!result.hasErrors()) {
            User user = userService.register(admin);
            if (user == null) {
                return CommonResult.failed("用户注册失败");
            }
            logger.info("USER: " + user.getUsername() + " REGISTER SUCCESS.");
            return CommonResult.success("");
        }
        return CommonResult.failed("用户信息不合法");
    }

    /*@ApiOperation("登陆界面")
    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }*/

    @ApiOperation("登陆成功后的处理方法")
    @PostMapping("/loginSuccess")
    public CommonResult loginSuccessHandler(HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = SecurityUtil.getCurrentUserNotNull();
            logger.info("USER: " + user.getUsername() + " LOGIN SUCCESS.");
            userService.loginLog(request, user);
            response.sendRedirect("/index");
            return CommonResult.success(user);
        } catch (Exception e) {
            logger.info("LOGIN EXCEPTION, e :" + e.getMessage());
            return CommonResult.failed();
        }
    }

    @ApiOperation("登陆以后返回token")
    @PostMapping("/loginAuth")
    public CommonResult loginAuth(HttpServletRequest request, HttpServletResponse response, boolean rememberMe) {
        User user = SecurityUtil.getCurrentUserNotNull();
//        logger.info(user.toString());
        String token = userService.loginAuth(user.getUsername(), user.getPassword(), rememberMe);
        if (token == null) {
            return CommonResult.unauthorized("用户名或密码错误");
        }
        logger.info("USER: " + user.getUsername() + " LOGIN SUCCESS.");
//        logger.info("token: " + token);
        userService.loginLog(request, user);
        response.setHeader(tokenHeader, tokenHead + token);
        return CommonResult.success(SafetyUser.safetyUser(user));
    }

    @ApiOperation("")
    @PostMapping("/loginFailure")
    public CommonResult loginFailure() {
        return CommonResult.unauthorized("用户名或密码错误");
    }

    @ApiOperation("查询用户名是否已被注册")
    @GetMapping("/hasUsername")
    public CommonResult hasUsername(String username) {
        return CommonResult.success(userService.getUserByUsername(username) != null);
    }

    @ApiOperation("查询邮箱是否已被注册")
    @GetMapping("/hasEmail")
    public CommonResult hasEmail(String email) {
        return CommonResult.success(userService.getUserByEmail(email) != null);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
