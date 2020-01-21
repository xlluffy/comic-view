package com.luffy.comic.dto;

import com.luffy.comic.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminUserDetailsTest {
    @Autowired
    UserService userService;

    @Test
    public void getAuthorities() {
        userService.login("admin", "123456");
        SecurityContextHolder.getContext().getAuthentication().getAuthorities().forEach(System.out::println);
    }
}