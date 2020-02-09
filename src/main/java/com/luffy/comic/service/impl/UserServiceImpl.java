package com.luffy.comic.service.impl;

import com.luffy.comic.common.utils.JwtTokenUtil;
import com.luffy.comic.common.utils.SecurityUtil;
import com.luffy.comic.mapper.UserLoginLogMapper;
import com.luffy.comic.mapper.UserMapper;
import com.luffy.comic.mapper.UserRoleRelationMapper;
import com.luffy.comic.model.User;
import com.luffy.comic.model.UserLoginLog;
import com.luffy.comic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * UmsAdminService的实现类
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserMapper userMapper;
    private UserLoginLogMapper userLoginLogMapper;
    private UserDetailsService userDetailService;
    private UserRoleRelationMapper userRoleRelationMapper;
    private JwtTokenUtil jwtTokenUtil;
    private PasswordEncoder passwordEncoder;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    public UserServiceImpl(UserMapper userMapper, UserLoginLogMapper userLoginLogMapper,
                           UserDetailsService userDetailService,
                           UserRoleRelationMapper userRoleRelationMapper,
                           JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userLoginLogMapper = userLoginLogMapper;
        this.userDetailService = userDetailService;
        this.userRoleRelationMapper = userRoleRelationMapper;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public User register(User user) {
        if (userMapper.findByUsername(user.getUsername()) != null) {
            return null;
        }
        user.setStatus(1);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        userRoleRelationMapper.addRoleToUser(user.getId(), "ROLE_USER");
//        logger.warn("You cannot register an account now.");
        return user;
    }

    @Override
    public void login(String username, String password) {
        try {
            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (AuthenticationException e) {
            logger.warn("登陆异常: {}", e.getMessage());
        }
    }

    @Override
    public void loginLog(HttpServletRequest request, User user) {
        if (!"0:0:0:0:0:0:0:1".equals(request.getRemoteAddr())) {
            UserLoginLog loginLog = new UserLoginLog();
            loginLog.setUserId(user.getId());
            loginLog.setIp(request.getRemoteAddr());
            loginLog.setUserAgent(request.getHeader("User-Agent"));
            userLoginLogMapper.insert(loginLog);
        }
    }

    @Override
    public String loginAuth(String username, String password) {
        String token = null;
        try {
            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            logger.warn("登陆异常: {}", e.getMessage());
        }
        return token;
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public void update(HttpServletRequest request, User user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userMapper.update(user);

        // 这方法也太笨了，什么时候修一下
        User currentUser = SecurityUtil.getCurrentUserNotNull();
        if (user.getNickName() != null) {
            currentUser.setNickName(user.getNickName());
        }
        if (user.getNote() != null) {
            currentUser.setNote(user.getNote());
        }
        if (user.getIcon() != null) {
            currentUser.setIcon(user.getNote());
        }
    }

    @Override
    public void updateEmail(HttpServletRequest request, String oldEmail, String newEmail) {
        if (oldEmail.equals(""))
            oldEmail = null;
        if (oldEmail == null || !oldEmail.equals(newEmail)) {
            User user = SecurityUtil.getCurrentUserNotNull();
            if (oldEmail == null ||  user.getEmail().equals("") || oldEmail.equals(user.getEmail())) {
                user.setEmail(newEmail);
                userMapper.update(user); // 似乎有数据库更新失败，但session修改成功的风险
            }
        }
    }

    @Override
    public boolean updatePwd(HttpServletRequest request, String oldPwd, String newPwd) {
        User user = SecurityUtil.getCurrentUserNotNull();
        if ((oldPwd != null && !oldPwd.equals(newPwd)) && passwordEncoder.matches(oldPwd, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPwd));
            userMapper.update(user);
            return true;
        }
        return false;
    }
}
