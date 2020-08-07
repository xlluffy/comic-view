package com.luffy.comic.common.utils;

import com.luffy.comic.model.User;

public class SafetyUser {

    public static User safetyUser(User user) {
        // 深拷贝，然后处理敏感信息
        String email = user.getEmail();
        user.setEmail(email.substring(0, 2) + "***" + email.substring(email.indexOf('@')));
        user.setPassword(null);
        return user;
    }

    public static User secretUser(User user) {
        user.setPassword(null);
        user.setEmail(null);
        return user;
    }
}
