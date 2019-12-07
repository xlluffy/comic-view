package com.luffy.comic.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "LoginController", description = "登陆")
@Controller
@RequestMapping("/")
public class LoginController {
}
