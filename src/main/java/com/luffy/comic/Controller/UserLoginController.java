package com.luffy.comic.controller;

import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.model.User;
import com.luffy.comic.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@Api(tags = "UserLoginController")
public class UserLoginController {
    private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);

    private UserService userService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    public UserLoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "admin/register";
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User admin, BindingResult result) {
        if (!result.hasErrors()) {
            User user = userService.register(admin);
            if (user == null) {
                return "admin/register";
            }
            logger.info("USER: " + user.getUsername() + "REGISTER SUCCESS.");
            return "redirect:login?username=" + user.getUsername();
        }
        return "admin/register";
    }

    @ApiOperation("登陆界面")
    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @ApiOperation("查询用户名是否已被注册")
    @PostMapping("/hasUsername")
    @ResponseBody
    public CommonResult hasUsername(String username) {
        return CommonResult.success(userService.getUserByUsername(username) != null);
    }

    @ApiOperation("查询邮箱是否已被注册")
    @PostMapping("/hasEmail")
    @ResponseBody
    public CommonResult hasEmail(String email) {
        return CommonResult.success(userService.getUserByEmail(email) != null);
    }

    @ApiOperation("登陆以后返回token")
    @PostMapping("/loginAuth")
    @ResponseBody
    public CommonResult loginAuth(HttpServletResponse response, String username, String password/*, BindingResult result*/) {
        String token = userService.loginAuth(username, password);
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        response.setHeader(tokenHeader, tokenHead + token);
        return CommonResult.success(tokenMap);
    }

}
