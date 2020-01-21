package com.luffy.comic.service.impl;

import com.luffy.comic.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @Test
    public void update() {
        /*User admin = new User();
        admin.setId(1);
        admin.setPassword("missyou123.");
        userService.update(admin);*/
    }

    @Test
    public void login() {
        String username = "test";
        String password = "123456";
        userService.login(username, password);
    }
}