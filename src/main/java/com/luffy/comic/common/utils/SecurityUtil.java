package com.luffy.comic.common.utils;

import com.luffy.comic.dto.AdminUserDetails;
import com.luffy.comic.model.User;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AdminUserDetails) {
            return (User) principal;
        }
        return null;
    }

    public static User getCurrentUserNotNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof AdminUserDetails) {
                return (User) principal;
            }
            throw new AccountExpiredException("账号未登录或登录信息已过期");
        }
        throw new AccountExpiredException("账号未登录或登录信息已过期");
    }
}
